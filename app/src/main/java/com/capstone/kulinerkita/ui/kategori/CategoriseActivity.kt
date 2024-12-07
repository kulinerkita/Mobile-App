package com.capstone.kulinerkita.ui.kategori

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.capstone.kulinerkita.R
import com.capstone.kulinerkita.data.model.Categorise
import com.capstone.kulinerkita.databinding.ActivityKategoriBinding

class CategoriseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKategoriBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityKategoriBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mendapatkan kategori yang dipilih dari Intent
        val category = intent.getStringExtra("CATEGORY") ?: "Makanan"

        val kategori = listOf(
            Categorise(
                idCategorise = 1,
                namaCategorise = "Wedang Uwuh",
                typeCategorise = "Minuman",
                gamblerCategorise = R.drawable.wedang_uwuh
            ),
            Categorise(
                idCategorise = 2,
                namaCategorise = "Timlo",
                typeCategorise = "Makanan",
                gamblerCategorise = R.drawable.timlo
            ),
            Categorise(
                idCategorise = 3,
                namaCategorise = "Tahok",
                typeCategorise = "Makanan",
                gamblerCategorise = R.drawable.tahok
            ),
            Categorise(
                idCategorise = 4,
                namaCategorise = "Soto",
                typeCategorise = "Makanan",
                gamblerCategorise = R.drawable.soto
                ),
            Categorise(
                idCategorise = 5,
                namaCategorise = "Serabi",
                typeCategorise = "Makanan",
                gamblerCategorise = R.drawable.serabi
            ),
            Categorise(
                idCategorise = 6,
                namaCategorise = "Selat",
                typeCategorise = "Makanan",
                gamblerCategorise = R.drawable.selat
            ),
            Categorise(
                idCategorise = 7,
                namaCategorise = "Sego Liwet",
                typeCategorise = "Makanan",
                gamblerCategorise = R.drawable.sego_liwet
            ),
            Categorise(
                idCategorise = 8,
                namaCategorise = "Sate Kere",
                typeCategorise = "Makanan",
                gamblerCategorise = R.drawable.sate_kere
            ),
            Categorise(
                idCategorise = 9,
                namaCategorise = "Sate Buntel",
                typeCategorise = "Makanan",
                gamblerCategorise = R.drawable.sate_buntel
            ),
            Categorise(
                idCategorise = 10,
                namaCategorise = "Lenjongan",
                typeCategorise = "Makanan",
                gamblerCategorise = R.drawable.lenjongan
            ),
            Categorise(
                idCategorise = 11,
                namaCategorise = "Gudeg",
                typeCategorise = "Makanan",
                gamblerCategorise = R.drawable.gudeg
            ),
            Categorise(
                idCategorise = 12,
                namaCategorise = "Gendar Pecel",
                typeCategorise = "Makanan",
                gamblerCategorise = R.drawable.gendar_pecel
            ),
            Categorise(
                idCategorise = 13,
                namaCategorise = "Wedang Asle",
                typeCategorise = "Minuman",
                gamblerCategorise = R.drawable.wedang_asle
            ),
            Categorise(
                idCategorise = 14,
                namaCategorise = "Es Kapal",
                typeCategorise = "Minuman",
                gamblerCategorise = R.drawable.es_kapal
            ),
            Categorise(
                idCategorise = 15,
                namaCategorise = "Es Gempol Pleret",
                typeCategorise = "Minuman",
                gamblerCategorise = R.drawable.es_gempol_pleret
            ),
            Categorise(
                idCategorise = 16,
                namaCategorise = "Es Dawet Telasih",
                typeCategorise = "Minuman",
                gamblerCategorise = R.drawable.es_dawet_telasih
            ),
            Categorise(
                idCategorise = 17,
                namaCategorise = "Brambang Asem",
                typeCategorise = "Makanan",
                gamblerCategorise = R.drawable.brambang_asem
            )
        )

        // Memfilter kategori berdasarkan pilihan kategori yang dipilih (Makanan atau Minuman)
        val filteredCategory = kategori.filter { it.typeCategorise == category }

        // Adapter untuk RecyclerView
        val adapter = CategoriseAdapter(filteredCategory) { selectedCategorise ->
            // Aksi ketika item diklik, kirim categoryId ke CategoriesKulinerActivity
            val intent = Intent(this, CategoriesKulinerActivity::class.java).apply {
                putExtra("CATEGORY_ID", selectedCategorise.idCategorise)
            }
            startActivity(intent)
        }

        binding.recyclerViewKategori.apply {
            layoutManager = GridLayoutManager(this@CategoriseActivity, 2)
            this.adapter = adapter
        }

        binding.backButtonKategori.setOnClickListener {
            onBackPressed()
        }
    }
}