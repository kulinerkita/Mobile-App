package com.capstone.kulinerkita

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

        // Periksa status layanan lokasi
        if (!isLocationEnabled()) {
            showLocationSettingsDialog()
        } else {
            // Ambil data dari Intent
            val latitude = intent.getDoubleExtra("LATITUDE", 0.0)
            val longitude = intent.getDoubleExtra("LONGITUDE", 0.0)
            val restaurantName = intent.getStringExtra("RESTAURANT_NAME") ?: "Lokasi Restoran"

            // Inisialisasi MapFragment
            val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync {
                onMapReady(it, LatLng(latitude, longitude), restaurantName)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        // Fungsi ini tetap digunakan untuk inisialisasi GoogleMap
        mMap = googleMap
    }

    private fun onMapReady(map: GoogleMap, location: LatLng, title: String) {
        mMap = map

        // Tambahkan marker pada lokasi restoran
        mMap.addMarker(MarkerOptions().position(location).title(title))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))

        // Konfigurasi UI tambahan
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true
    }

    // Periksa apakah lokasi diaktifkan
    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    // Tampilkan dialog untuk mengarahkan pengguna ke pengaturan lokasi
    private fun showLocationSettingsDialog() {
        AlertDialog.Builder(this)
            .setTitle("Location Services Disabled")
            .setMessage("Location is required to view the map. Please enable location services.")
            .setPositiveButton("Go to Settings") { _, _ ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
                Toast.makeText(this, "Location services are required to use this feature.", Toast.LENGTH_SHORT).show()
                finish() // Tutup aktivitas jika lokasi tidak diaktifkan
            }
            .setCancelable(false)
            .show()
    }
}
