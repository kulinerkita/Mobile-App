package com.capstone.kulinerkita.ui.maps

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capstone.kulinerkita.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.capstone.kulinerkita.databinding.ActivityMapsNavigationBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import android.location.Location
import android.location.Geocoder
import android.net.Uri
import java.util.Locale
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.PolylineOptions

class MapsNavigationActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsNavigationBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tombol kembali
        binding.backMaps.setOnClickListener { finish() }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map_navigation) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.ButtonMulaiLokasi.setOnClickListener {
            val restaurantLocation = intent.getParcelableExtra<LatLng>("RESTAURANT_LOCATION")
            restaurantLocation?.let {
                val gmmIntentUri = Uri.parse("google.navigation:q=${it.latitude},${it.longitude}")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                if (mapIntent.resolveActivity(packageManager) != null) {
                    sendBroadcast(Intent("com.capstone.kulinerkita.ACTION_NAVIGATION_STARTED")) // Broadcast
                    startActivity(mapIntent)
                } else {
                    // Tampilkan pesan kepada pengguna bahwa Google Maps tidak tersedia
                }
            }
        }

    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation(onLocationRetrieved: (LatLng) -> Unit) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                val currentLatLng = LatLng(it.latitude, it.longitude)
                onLocationRetrieved(currentLatLng)
            } ?: run {
                // Lokasi tidak tersedia, tambahkan fallback atau pemberitahuan ke user
            }
        }
    }

    private fun getAddressFromLatLng(latLng: LatLng): String {
        return try {
            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (addresses?.isNotEmpty() == true) {
                addresses?.get(0)?.getAddressLine(0) ?: "Alamat tidak ditemukan"
            } else {
                "Alamat tidak ditemukan"
            }
        } catch (e: Exception) {
            "Alamat tidak tersedia"
        }
    }

    private fun getScaledBitmap(resourceId: Int, width: Int, height: Int): Bitmap {
        val bitmap = BitmapFactory.decodeResource(resources, resourceId)
        return Bitmap.createScaledBitmap(bitmap, width, height, false)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true

        // Lokasi restoran dari Intent
        val restaurantLocation = intent.getParcelableExtra<LatLng>("RESTAURANT_LOCATION")
        val restaurantName = intent.getStringExtra("RESTAURANT_NAME") ?: "Restoran"
        val restaurantAddress = intent.getStringExtra("RESTAURANT_ADDRESS") ?: "Alamat tidak tersedia"

        // Tambahkan marker lokasi restoran
        val smallIcon = BitmapDescriptorFactory.fromBitmap(getScaledBitmap(R.drawable.ic_lokasi_resto, 100, 100))
        restaurantLocation?.let {
            mMap.addMarker(
                MarkerOptions()
                    .position(it)
                    .title(restaurantName)
                    .icon(smallIcon)
            )
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 15f))

            // Tampilkan alamat restoran
            binding.tvLokasiResto.text = "$restaurantName\n$restaurantAddress"
        }

        // Tambahkan lokasi saat ini
        val smallIconMaps = BitmapDescriptorFactory.fromBitmap(getScaledBitmap(R.drawable.ic_lokasi_saat, 100, 100))
        getCurrentLocation { currentLocation ->
            mMap.addMarker(
                MarkerOptions()
                    .position(currentLocation)
                    .title("Lokasi Saya")
                    .icon(smallIconMaps)
            )
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12f))

            // Tampilkan nama lokasi saat ini
            val currentAddress = getAddressFromLatLng(currentLocation)
            binding.tvLokasiSaat.text = currentAddress

            // Gambar Polyline (Garis)
            restaurantLocation?.let { destination ->
                val polylineOptions = PolylineOptions()
                    .add(currentLocation) // Lokasi saat ini
                    .add(destination) // Lokasi restoran
                    .color(resources.getColor(R.color.color_green)) // Warna garis
                    .width(8f) // Ketebalan garis

                mMap.addPolyline(polylineOptions)
            }
        }
    }
}