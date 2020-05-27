package me.alberto.agrofarm.screens.map

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.activity_map_acitvity.*
import me.alberto.agrofarm.R
import java.util.*

const val RC_LOCATION_PERMISSION = 22

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var gMap: GoogleMap
    private lateinit var drawPolygon: MaterialButton
    private var polygon: Polygon? = null
    private val latLngList = arrayListOf<LatLng>()
    private val markerList = arrayListOf<Marker>()
    private var locationPermissionGranted = false

    private var lastKnownLocation: Location? = null
    private var lat: Double? = null
    private var long: Double? = null

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_acitvity)

        init()
        setupClickListeners()


    }

    private fun init() {
        Places.initialize(applicationContext, getString(R.string.map_key))
        fusedLocationProviderClient = FusedLocationProviderClient(this)

        getLocationPermission()
        drawPolygon = draw_polygon

        val supportMapFragment =
            supportFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment
        supportMapFragment.getMapAsync(this)

        val autocompleteFragment = supportFragmentManager
            .findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment

        autocompleteFragment.setPlaceFields(
            listOf(
                Place.Field.ID,
                Place.Field.NAME, Place.Field.ADDRESS,
                Place.Field.ADDRESS_COMPONENTS, Place.Field.LAT_LNG
            )
        )


        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                lat = place.latLng!!.latitude
                long = place.latLng!!.longitude

                gMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(lat!!, long!!),
                        14.0F
                    )
                )
                gMap.addMarker(MarkerOptions().position(place.latLng!!))
                gMap.animateCamera(CameraUpdateFactory.zoomTo(15F))
                gMap.uiSettings.isZoomControlsEnabled = true
            }

            override fun onError(place: Status) {
                Log.d("operation", "operation canceled")
            }
        })
    }


    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap?.let { gMap = it }
        updateLocationUI()


        gMap.setOnMapClickListener { latLng ->
            //create Marker Options
            val markerOptions = MarkerOptions().position(latLng)
            val marker = gMap.addMarker(markerOptions)

            //Add LatLng and marker
            latLngList.add(latLng)
            markerList.add(marker)
        }
    }

    private fun setupClickListeners() {
        drawPolygon.setOnClickListener {
            polygon?.remove()
            if (latLngList.size < 3) return@setOnClickListener
            //Create polygon
            val polygonOptions = PolygonOptions().addAll(latLngList).clickable(true)
            polygon = gMap.addPolygon(polygonOptions)

        }

        clear_map.setOnClickListener {
            //clear polygon
            if (polygon != null) polygon?.remove()
            for (marker in markerList) marker.remove()
            latLngList.clear()
            markerList.clear()
        }

        save_coordinates.setOnClickListener {
            polygon ?: return@setOnClickListener


            sendIntentBack(lat!!, long!!)
        }
    }

    private fun sendIntentBack(latitude: Double, longitude: Double) {
        val returnIntent = Intent()
        val geoCoder = Geocoder(this, Locale.getDefault())
        val addresses = geoCoder.getFromLocation(
            latitude,
            longitude, 1
        )
        val farmAddress = addresses[0].getAddressLine(0)
        val bundle = Bundle()
        bundle.putString("address", farmAddress)
        bundle.putSerializable("coordinates", latLngList)
        bundle.putParcelable("location", lastKnownLocation)
        returnIntent.putExtra("farm_details", bundle)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }


    private fun getDeviceLocation() {
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        lastKnownLocation = task.result

                        lat = lastKnownLocation!!.latitude
                        long = lastKnownLocation!!.longitude

                        gMap.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(lat!!, long!!),
                                14.0F
                            )
                        )

                        gMap.animateCamera(CameraUpdateFactory.zoomTo(15F))
                        gMap.uiSettings.isZoomControlsEnabled = true
                    }

                }
            } else {
                Toast.makeText(this, "Location is null", Toast.LENGTH_SHORT).show()
            }

        } catch (error: SecurityException) {
            Log.d("security", error.message.toString())
        }
    }


    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
            getDeviceLocation()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                RC_LOCATION_PERMISSION
            )
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            RC_LOCATION_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true
                    getDeviceLocation()
                }
            }
        }

        updateLocationUI()
    }

    private fun updateLocationUI() {
        if (gMap == null) return
        try {
            if (locationPermissionGranted) {
                gMap.isMyLocationEnabled = true
                gMap.uiSettings.isMyLocationButtonEnabled = true
            } else {
                gMap.isMyLocationEnabled = false
                gMap.uiSettings.isMyLocationButtonEnabled = false
                Toast.makeText(this, "Allow app to access device location", Toast.LENGTH_LONG)
                    .show()

            }
        } catch (error: SecurityException) {
            Log.d("security", error.message.toString())
        }
    }

}


