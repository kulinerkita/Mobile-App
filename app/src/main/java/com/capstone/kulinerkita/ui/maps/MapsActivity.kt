package com.capstone.kulinerkita.ui.maps

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.capstone.kulinerkita.API.RestaurantApiClient
import com.capstone.kulinerkita.MapsViewModelFactory
import com.capstone.kulinerkita.R
import com.capstone.kulinerkita.data.model.RestaurantDescription
import com.capstone.kulinerkita.data.repository.RestaurantRepository
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.capstone.kulinerkita.databinding.ActivityMapsBinding
import com.capstone.kulinerkita.ui.detailResto.DetailRestoActivity
import com.capstone.kulinerkita.ui.home.HomeAdapter

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var mapsViewModel: MapsViewModel
    private lateinit var homeAdapter: HomeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tombol kembali
        binding.backButtonMaps.setOnClickListener { finish() }

        // Inisialisasi ViewModel
        val apiService = RestaurantApiClient.RestaurantApi
        val repository = RestaurantRepository(apiService)
        val factory = MapsViewModelFactory(repository)
        mapsViewModel = ViewModelProvider(this, factory)[MapsViewModel::class.java]

        setupRecyclerView()

        mapsViewModel.restaurants.observe(this) { restaurants ->
            // ambil hanya 4 restoran pertama
            homeAdapter.updateData(restaurants.take(4))
        }

        // Ambil data dari Intent
        val latitude = intent.getDoubleExtra("LATITUDE", 0.0)
        val longitude = intent.getDoubleExtra("LONGITUDE", 0.0)
        val restaurantName = intent.getStringExtra("RESTAURANT_NAME") ?: "Lokasi Restoran"
        val restaurantAddress = intent.getStringExtra("RESTAURANT_ADDRESS") ?: "Alamat tidak tersedia"
        val ecoFriendly = intent.getIntExtra("RESTAURANT_ECO_FRIENDLY", 0)
        val categoryId = intent.getIntExtra("CATEGORY_ID", -1)
        val weather = intent.getStringExtra("RESTAURANT_WEATHER") ?: "Cuaca tidak tersedia"
        val rating = intent.getStringExtra("RESTAURANT_RATING")

        // Tampilkan data di layout
        binding.tvRestaurantMaps.text = restaurantName
        binding.tvMapsAddress.text = restaurantAddress
        binding.tvCategoriSuhuMaps.text = weather
        binding.tvratingsMaps.text = rating
        binding.tvCategoriEcoMaps.text =
            if (ecoFriendly == 1) "Eco-Friendly" else "Non-Eco-Friendly"

        // Tampilkan deskripsi berdasarkan category_id
        if (categoryId != -1) {
            mapsViewModel.fetchRestaurants(categoryId)
            displayDescription(categoryId)
        } else {
            binding.tvMapsdesc.text = "Deskripsi tidak tersedia."
        }

        binding.ButtonCekLokasi.setOnClickListener {
            val intent = Intent(this, MapsNavigationActivity::class.java)
            intent.putExtra("RESTAURANT_LOCATION", LatLng(latitude, longitude)) // Data restoran
            intent.putExtra("RESTAURANT_NAME", restaurantName)
            intent.putExtra("RESTAURANT_ADDRESS", restaurantAddress) // Alamat restoran
            startActivity(intent)
        }

        // Inisialisasi MapFragment
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync {
            onMapReady(it, LatLng(latitude, longitude), restaurantName)
        }
    }

    private fun setupRecyclerView() {
        homeAdapter = HomeAdapter(emptyList()) { restaurant ->
            val intent = Intent(this, DetailRestoActivity::class.java)
            intent.putExtra("SELECTED_RESTAURANT_ID", restaurant)
            startActivity(intent)
        }

        binding.itemRestoran.apply {
            layoutManager = GridLayoutManager(this@MapsActivity, 2)
            adapter = homeAdapter
        }
    }

    private fun getDummyDescriptions(): List<RestaurantDescription> {
        return listOf(
            RestaurantDescription(1, "Sebagai minuman tradisional khas Jawa, Wedang Uwuh memiliki komposisi rempah-rempah alami seperti jahe, serai, daun pandan, dan kayu manis, yang dipadu dengan gula merah. Minuman ini dikenal dengan khasiatnya untuk menghangatkan tubuh dan menjaga stamina, serta memberikan rasa manis dan pedas yang menenangkan."),
            RestaurantDescription(2, "Timlo adalah sup khas Solo yang kaya rasa dengan isian daging sapi, telur, dan sayuran. Kuahnya yang gurih dengan bumbu rempah yang khas, membuat hidangan ini begitu nikmat dan lezat. Timlo sering disajikan dalam porsi kecil atau sedang dan menjadi pilihan yang populer di kalangan pecinta kuliner tradisional."),
            RestaurantDescription(3, "Tahok adalah makanan ringan tradisional yang terbuat dari tahu dengan siraman kuah manis. Tekstur tahu yang lembut dipadukan dengan rasa manis kuah gula kelapa memberikan sensasi yang pas di lidah."),
            RestaurantDescription(4, "Soto adalah hidangan sup berkuah yang sangat populer di seluruh Indonesia, dengan variasi yang berbeda-beda di setiap daerah. Biasanya terdiri dari daging sapi atau ayam, sayuran, dan rempah-rempah yang menyatu dalam kuah yang kaya rasa. Soto ini adalah pilihan yang tepat untuk menikmati rasa pedas dan segar dengan nasi hangat."),
            RestaurantDescription(5, "Serabi adalah kue tradisional yang terbuat dari tepung beras, disajikan dengan berbagai topping manis maupun gurih. Dari serabi manis dengan gula merah cair hingga serabi gurih dengan daun bawang, serabi memberikan rasa yang pas di lidah dengan tekstur kenyal yang khas."),
            RestaurantDescription(6, "Selat adalah kuliner khas Solo yang mirip dengan steak, namun dengan bumbu dan kuah yang khas. Daging sapi yang empuk dan sayuran segar disajikan dengan saus manis gurih, memberikan perpaduan rasa yang unik dan menggugah selera."),
            RestaurantDescription(7, "Nasi liwet adalah sajian nasi gurih yang dimasak dengan santan dan rempah-rempah, kemudian disajikan bersama lauk-pauk seperti ayam, ikan, dan sambal. Hidangan ini adalah pilihan tepat bagi mereka yang menyukai makanan yang kaya rasa dengan perpaduan rasa pedas dan gurih."),
            RestaurantDescription(8, "Sate Kere adalah sate khas Jawa yang terbuat dari daging sapi atau tempe yang dipadukan dengan sambal kacang pedas. Rasa sate ini gurih dan sedikit manis, memberikan cita rasa yang memuaskan bagi penggemar sate tradisional Indonesia."),
            RestaurantDescription(9, "Sate Buntel adalah sate dengan daging kambing atau sapi yang dibungkus dengan lemak kambing, memberikan cita rasa gurih dan berlemak. Disajikan dengan sambal kecap atau sambal terasi, sate ini merupakan hidangan yang lezat dan memuaskan."),
            RestaurantDescription(10, "Lenjongan adalah hidangan berbahan dasar tempe atau tahu yang dibungkus daun pisang, memberikan rasa gurih dengan tambahan bumbu rempah yang khas. Lenjongan sering kali dijadikan lauk pendamping nasi atau camilan sore."),
            RestaurantDescription(11, "Gudeg adalah hidangan legendaris khas Solo yang terbuat dari nangka muda yang dimasak dengan santan dan rempah-rempah hingga empuk dan kaya rasa. Biasanya disajikan dengan ayam, telur, atau sambal goreng, Gudeg adalah hidangan yang kaya akan cita rasa manis, gurih, dan pedas."),
            RestaurantDescription(12, "Gendar Pecel adalah hidangan nasi ketan yang disajikan dengan sambal pecel yang pedas dan sayuran segar. Sambal pecel yang khas, terbuat dari kacang tanah, memberikan rasa pedas dan gurih yang menambah kenikmatan hidangan ini"),
            RestaurantDescription(13, "Minuman segar yang terbuat dari air kelapa muda dengan tambahan rempah-rempah pilihan, Wedang Asle menawarkan kesegaran yang berbeda dengan rasa yang lebih ringan dan alami. Cocok sebagai pelepas dahaga di siang hari, minuman ini juga sering dianggap memiliki manfaat kesehatan bagi tubuh."),
            RestaurantDescription(14, "Es Kapal adalah minuman dingin yang terbuat dari bahan-bahan segar, seperti es serut, sirup manis, dan potongan buah tropis. Hidangan ini menjadi pilihan populer, terutama ketika cuaca panas, dengan kombinasi rasa manis dan asam yang menyegarkan."),
            RestaurantDescription(15, "Es Gempol Pleret adalah minuman tradisional yang menggabungkan gempol (beras ketan yang dibentuk bulat) dengan bahan-bahan lainnya, seperti kelapa muda dan gula merah, menciptakan rasa manis yang lezat. Segar dan nikmat, minuman ini cocok untuk merayakan momen santai bersama teman-teman atau keluarga."),
            RestaurantDescription(16, "Salah satu minuman khas Indonesia yang terkenal, Es Dawet Telasih menggabungkan dawet atau cendol dengan biji telasih (sejenis biji selasih). Rasanya yang manis dan kenyal memberikan sensasi rasa yang menyegarkan di setiap tegukan, menjadikannya pilihan sempurna untuk menikmati hari yang terik."),
            RestaurantDescription(17, "Brambang Asem adalah makanan khas yang menggabungkan rasa asam dan pedas dari bahan-bahan segar, memberikan sensasi rasa yang unik. Hidangan ini sering dijadikan sebagai lauk pendamping nasi dan cocok untuk mereka yang menyukai makanan dengan rasa segar dan pedas.")
        )
    }

    private fun displayDescription(categoryId: Int) {
        val descriptions = getDummyDescriptions()
        val description = descriptions.find { it.idDesc == categoryId }?.description
            ?: "Deskripsi tidak tersedia untuk kategori ini."
        binding.tvMapsdesc.text = description
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
}
