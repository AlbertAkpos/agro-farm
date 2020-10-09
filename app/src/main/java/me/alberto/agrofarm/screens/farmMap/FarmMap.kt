package me.alberto.agrofarm.screens.farmMap

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import me.alberto.agrofarm.R
import me.alberto.agrofarm.database.Farm
import me.alberto.agrofarm.database.FarmLocation

class FarmMap : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private var farmLocation: FarmLocation? = null
    private var farmCoordinates: List<LatLng>? = null
    private lateinit var farmLatLng: LatLng
    private var farmName: String? = null
    private var polygon: Polygon? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_farm_map)

        farmName = intent.getParcelableExtra<Farm>("farmer")?.name
        farmLocation = intent.getParcelableExtra<Farm>("farmer")?.location
        farmCoordinates = intent.getParcelableExtra<Farm>("farmer")?.coordinates
        farmLatLng = LatLng(farmLocation!!.lat, farmLocation!!.long)

      Log.d("farm", "farmCordinates: $farmCoordinates")
        val supportMapFragment =
            supportFragmentManager.findFragmentById(R.id.farm_map) as SupportMapFragment
        supportMapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap?.let { mMap = it }
        updateLocationUI()
        getFarmLocation()

    }

    private fun getFarmLocation() {

        mMap.addMarker(MarkerOptions().position(farmLatLng)).title = farmName

        mMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                farmLatLng,
                14.0F
            )
        )


        val polygonOptions = PolygonOptions().addAll(farmCoordinates).clickable(true)
        polygon = mMap.addPolygon(polygonOptions)
        polygon?.fillColor = Color.GREEN

        val builder = LatLngBounds.builder()
        for (point in farmCoordinates!!) {
            builder.include(point)
        }
        val bounds = builder.build()
        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 400, 400, 5)
        mMap.animateCamera(cameraUpdate)
        mMap.uiSettings.isZoomControlsEnabled = true
    }


    private fun updateLocationUI() {
        try {
            mMap.isBuildingsEnabled = true
            mMap.isMyLocationEnabled = true
            mMap.uiSettings.isMyLocationButtonEnabled = true

        } catch (error: SecurityException) {
            Log.d("security", error.message.toString())
        }
    }
}
