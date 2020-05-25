package me.alberto.agrofarm.screens.map

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.location.places.PlaceDetectionClient
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.activity_map_acitvity.*
import me.alberto.agrofarm.R
import me.alberto.agrofarm.screens.add_farmer.RC_MAP

const val RC_LOCATION_PERMISSION = 22

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var gMap: GoogleMap
    private lateinit var drawPolygon: MaterialButton
    private var polygon: Polygon? = null
    private val latLngList = arrayListOf<LatLng>()
    private val markerList = arrayListOf<Marker>()

    private lateinit var geoDataClient: GeoDataClient
    private lateinit var placeDetectionClient: PlaceDetectionClient
    private lateinit var fusedLocationProvider: FusedLocationProviderClient

    private var locationPermissionGranted = false

    private var lastKnownLocation: Location? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_acitvity)

        geoDataClient = Places.getGeoDataClient(this, null)
        placeDetectionClient = Places.getPlaceDetectionClient(this, null)
        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this)

        getLocationPermission()
        drawPolygon = draw_polygon

        val supportMapFragment = supportFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment
        supportMapFragment.getMapAsync(this)

        setupClickListeners()

    }


    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap?.let { gMap = it }

        updateLocationUI()
        getDeviceLocation()

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
            if ( latLngList.size < 3 ) return@setOnClickListener
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
            val returnIntent = Intent()
            returnIntent.putExtra("coordinates", latLngList)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
    }

    private fun getDeviceLocation() {
        try {

            if (locationPermissionGranted) {
                val locationResult = fusedLocationProvider.lastLocation
                locationResult.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        lastKnownLocation = task.result
                        gMap.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(lastKnownLocation!!.latitude, lastKnownLocation!!.longitude),
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

        locationPermissionGranted = false
        when (requestCode) {
            RC_LOCATION_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true
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
                getLocationPermission()

            }
        } catch (error: SecurityException) {
            Log.d("security", error.message.toString())
        }
    }
}
