package com.example.mapcurrentlocation

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private val places: ArrayList<Place> = ArrayList()
    private var rCode = 129
    private lateinit var googlemap: GoogleMap
    private lateinit var currentLatLng: LatLng
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        places.add(Place("Jammu and Kashmir", latLng = LatLng(34.0802, 74.8088), LatLng(34.0802, 74.8088), 4.5F))
        places.add(Place("Gujarat", latLng = LatLng(23.118, 70.7103), LatLng(23.118, 70.7103), 4.5F))
        places.add(Place("Samastipur", latLng = LatLng(25.85076, 85.774176), LatLng(25.85076, 85.774176), 4.5F))
        places.add(Place("Arunachal Pradesh", latLng = LatLng(28.447699,96.20711953), LatLng(28.447699, 96.20711953), 4.5F))
        places.add(Place("Tamil Nadu", latLng = LatLng(8.21380, 77.52703), LatLng(8.21380, 77.52703), 4.5F))
        val currentLocationFab = findViewById<FloatingActionButton>(R.id.currentLocationFab)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        currentLocationFab.setOnClickListener{
            checkPer()
        }

        val mapView = supportFragmentManager.findFragmentById(R.id.map_fragment) as? SupportMapFragment
        mapView?.getMapAsync{  googleMap ->
            googlemap = googleMap

            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                checkPer()
            }
            else{
                googlemap.isMyLocationEnabled = true
                googlemap.uiSettings.isMyLocationButtonEnabled = true
                googlemap.uiSettings.isZoomControlsEnabled = true
                googlemap.uiSettings.isZoomGesturesEnabled = true
                googlemap.uiSettings.isScrollGesturesEnabled = true
                googlemap.uiSettings.isTiltGesturesEnabled = true
                googlemap.uiSettings.isRotateGesturesEnabled = true
                googlemap.uiSettings.isCompassEnabled = true
                googlemap.uiSettings.isMapToolbarEnabled = true
                googlemap.uiSettings.isMyLocationButtonEnabled = true
                googlemap.uiSettings.isZoomGesturesEnabled = true
                googlemap.uiSettings.isScrollGesturesEnabled = true
                googlemap.uiSettings.isTiltGesturesEnabled = true
                googleMap.uiSettings.isCompassEnabled = true
                googleMap.uiSettings.isMapToolbarEnabled = true
                googleMap.isTrafficEnabled = true

                googleMap.isBuildingsEnabled = true
                googleMap.isMyLocationEnabled = true
                googleMap.uiSettings.isMyLocationButtonEnabled = true
                googleMap.uiSettings.isZoomControlsEnabled = true


                googleMap.uiSettings.isCompassEnabled = true
                googleMap.uiSettings.isMapToolbarEnabled = true
                googleMap.isTrafficEnabled = true

                googleMap.isBuildingsEnabled = true



            }
        }
    }
    private  fun checkPer(){
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getCurrentUserLocation()

        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                rCode
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentUserLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { place->
            if (place != null) {
                val geocoder = Geocoder(this, Locale.getDefault())
                val address = geocoder.getFromLocation(place.latitude, place.longitude, 1)
                val location = address?.get(0)?.getAddressLine(0)
                currentLatLng = LatLng(place.latitude, place.longitude)
                places.add(
                    Place(
                        location.toString(),
                        latLng = currentLatLng,
                        address = currentLatLng,
                        4.5F
                    )
                )
                val marker = googlemap.addMarker(
                    MarkerOptions().title(location).position(currentLatLng)
                )

                googlemap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 14f))
                googlemap.addCircle(
                    CircleOptions()
                        .center(currentLatLng)
                        .clickable(true)

                        .radius(1000.0)
                        .strokeColor(R.color.white)
                        .strokeWidth(3.0F)
                        .visible(true)
                )
                addMarker(googlemap)
            } else {
                Toast.makeText(this, "Please enable Location", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == rCode) {
            getCurrentUserLocation()
        } else {
            checkPer()
        }

    }

    fun addMarker(googleMap: GoogleMap){
        places.forEach {place->
            val marker = googleMap.addMarker(
                MarkerOptions().title(place.name).position(place.latLng)
            )
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 14f))
            googleMap.addCircle(
                CircleOptions()
                    .center(currentLatLng)
                    .clickable(true)

                    .radius(1000.0)
                    .strokeColor(R.color.white)
                    .strokeWidth(3.0F)
                    .visible(true)
            )
            googlemap.addPolyline(
                PolylineOptions().add(
                    places[places.lastIndex - 3].latLng,
                    places[places.lastIndex - 4].latLng,
                    places[places.lastIndex - 2].latLng,
                    places[places.lastIndex - 5].latLng,
                    places[places.lastIndex - 1].latLng,
                    places[places.lastIndex].latLng,
                    places[places.lastIndex - 1].latLng,

                )
                    .clickable(true)
                    .color(R.color.black)
                    .width(5F)
                    .geodesic(true)

            )
        }

    }
}