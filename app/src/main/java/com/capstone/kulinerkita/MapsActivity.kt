package com.capstone.kulinerkita

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.capstone.kulinerkita.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val latitude = intent.getDoubleExtra("LATITUDE", 0.0)
        val longitude = intent.getDoubleExtra("LONGITUDE", 0.0)

        Log.d("MapsActivity", "Received Coordinates: Lat = $latitude, Lng = $longitude")

        // Periksa apakah koordinat valid
        if (latitude != 0.0 && longitude != 0.0) {
            val location = LatLng(latitude, longitude)
            mMap.addMarker(MarkerOptions().position(location).title("Lokasi Restoran"))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))  // Zoom level 15
        } else {
            Log.e("MapsActivity", "Invalid Coordinates Received.")
            // Tampilkan lokasi default atau pesan kesalahan
            val defaultLocation = LatLng(0.0, 0.0)
            mMap.addMarker(MarkerOptions().position(defaultLocation).title("Lokasi Tidak Diketahui"))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 5f))
        }

        // Enable map controls
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
    }
}