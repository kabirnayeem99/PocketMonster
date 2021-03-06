package io.github.kabirnayeem99.pocketmonster

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.location.LocationManager.GPS_PROVIDER
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
const val TAG = "MapsActivity"

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    var playerPower = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        getUserLocation()
        loadPockeMon()
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= 23 && (ActivityCompat
                .checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED)
        ) {
            requestPermissions(
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                ACCESS_LOCATION_REQUEST_CODE
            )
            return
        }

    }

    private fun getUserLocation() {
        Toast.makeText(this, "You granted the location permission", Toast.LENGTH_SHORT).show()

        val locationListner = CustomerLocationListener()
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        checkPermission()
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            3000,
            3f,
            locationListner
        )

        val locationThread = LocationThread()
        locationThread.start()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            ACCESS_LOCATION_REQUEST_CODE -> {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "You didn't grant the permission.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    var assignedLocation: Location? = null


    inner class CustomerLocationListener() : LocationListener {


        init {
            assignedLocation = Location(GPS_PROVIDER)
            assignedLocation!!.longitude = 0.0
            assignedLocation!!.latitude = 0.0
        }

        override fun onLocationChanged(location: Location) {
            assignedLocation = location
        }
    }

    inner class LocationThread() : Thread() {
        override fun run() {
            while (true) {
                try {

                    runOnUiThread {
                        mMap.clear()
                        val userLocation =
                            LatLng(assignedLocation!!.latitude, assignedLocation!!.longitude)
                        mMap.addMarker(
                            MarkerOptions()
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mario))
                                .position(userLocation)
                                .title("Me")
                                .snippet(" here is my location")
                        )
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 14f))

                        for ((index, pockemon) in listOfPockeMon.withIndex()) {
                            if (pockemon.isCatch == false) {
                                val pockemonLocation =
                                    LatLng(
                                        pockemon.latitude,
                                        pockemon.longtitude,
                                    )
                                mMap.addMarker(
                                    MarkerOptions()
                                        .icon(BitmapDescriptorFactory.fromResource(pockemon.imageResource))
                                        .position(pockemonLocation)
                                        .title(pockemon.name)
                                        .snippet(pockemon.description)
                                )

                            }
                            if (assignedLocation!!.distanceTo(
                                    pockemon.location
                                ) < 10f
                            ) {
                                pockemon.isCatch = true
                                playerPower += pockemon.power
                                listOfPockeMon[index] = pockemon
                                Toast.makeText(
                                    this@MapsActivity,
                                    "Your current power is $playerPower",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                        }

                    }

                    sleep(100)

                } catch (e: Exception) {
                    Log.e(TAG, "run: $e")
                }
            }
        }
    }

    var listOfPockeMon = ArrayList<PockeMon>()

    fun loadPockeMon() {

        val bulbasaur = PockeMon(
            "Bulbasaur",
            "small, squat amphibian and plant Pokémon",
            R.drawable.bulbasaur,
            20.0,
            22.3247,
            91.8114,
        )


        val charmander = PockeMon(
            "Charmander",
            "small, bipedal lizard-like Pokémon native to Kanto.",
            R.drawable.charmander,
            25.0,
            22.4317,
            91.7455,
        )

        var squirtle = PockeMon(
            "Squirtle",
            "half squirrel, half turtle.",
            R.drawable.squirtle,
            50.0,
            22.4345,
            91.8182,
        )

        listOfPockeMon.add(bulbasaur)
        listOfPockeMon.add(charmander)
        listOfPockeMon.add(squirtle)
    }
}