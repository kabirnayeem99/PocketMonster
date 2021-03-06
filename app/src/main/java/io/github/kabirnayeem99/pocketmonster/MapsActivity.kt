package io.github.kabirnayeem99.pocketmonster

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

const val ACCESS_LOCATION_REQUEST_CODE = 1

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    fun checkPermission() {
        if (Build.VERSION.SDK_INT >= 23 && (ActivityCompat
                        .checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), ACCESS_LOCATION_REQUEST_CODE)
            return
        }

        getUserLocation()

    }

    private fun getUserLocation() {
        Toast.makeText(this, "You granted the location permission", Toast.LENGTH_SHORT).show()
        TODO("Not yet implemented")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            ACCESS_LOCATION_REQUEST_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getUserLocation()
                } else {
                    Toast.makeText(this, "You didn't grant the permission.", Toast.LENGTH_SHORT).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(
                MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.mario))
                        .position(sydney)
                        .title("Me")
                        .snippet(" here is my location")
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 18f))
    }
}