package com.capstone.kulinerkita.data.model

import com.capstone.kulinerkita.R
import java.util.Date
import java.text.SimpleDateFormat
import java.util.Locale

data class Article(
    val img: Int,
    val title: String,
    val description: String,
    val url: String,
    val time: Date
)


val sdf = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
val articles = listOf(
    Article(
        img = R.drawable.news1,
        title = "Aktivitas Eco Friendly, Upaya Merawat Bumi",
        description = "Isu pemanasan global marak disuarakan di berbagai belahan dunia...",
        url = "https://www.idntimes.com/opinion/social/dewi-septiyani/aktivitas-eco-friendly-upaya-merawat-bumi-c1c2",
        time = sdf.parse("20 Sep 2022")!!
    ),
    Article(
        img = R.drawable.news2,
        title = "Survei: Konsumen Indonesia Makin Peduli Produk Ramah Lingkungan",
        description = "Jumlah konsumen yang lebih peduli terhadap produk yang ramah lingkungan...",
        url = "https://lifestyle.kompas.com/read/2022/09/20/184205220/survei-konsumen-indonesia-makin-peduli-produk-ramah-lingkungan",
        time = sdf.parse("21 Sep 2022")!!
    ),
    Article(
        img = R.drawable.news3,
        title = "Edible Straw: Sedotan Eco-Friendly di Masa Kini",
        description = "Hingga saat ini permasalahan sampah plastik masih menjadi tantangan...",
        url = "https://www.kompasiana.com/zahrahal/629d755ed263450d0877d932/edible-straw-sedotan-eco-friendly-di-masa-kini",
        time = sdf.parse("6 Jun 2022")!!
    ),
    Article(
        img = R.drawable.news4,
        title = "Eco-chop, Inovasi Wadah Makanan Ramah Lingkungan Buatan Mahasiswa Unair",
        description = "Tim mahasiswa Universitas Airlangga atau Unair mencetak prestasi...",
        url = "https://www.tempo.co/politik/eco-chop-inovasi-wadah-makanan-ramah-lingkungan-buatan-mahasiswa-unair-117000",
        time = sdf.parse("26 Nov 2023")!!
    ),
    Article(
        img = R.drawable.news5,
        title = "Tren Eco Friendly: Tips Memilih Daun Pisang untuk Sajian Makanan",
        description = "Daun pisang semakin dicari. Selain digunakan di dapur...",
        url = "https://www.kompas.com/tren/read/2021/03/04/170500165/tren-eco-friendly-ini-tips-memilih-daun-pisang-untuk-sajian-makanan?page=all",
        time = sdf.parse("4 Mar 2021")!!
    ),
    Article(
        img = R.drawable.news6,
        title = "96,7% Millennial dan Gen Z Indonesia Pilih Produk Ramah Lingkungan",
        description = "Tren gaya hidup berkelanjutan semakin berkembang di Indonesia...",
        url = "https://tirto.id/967-millennial-dan-gen-z-indonesia-pilih-produk-ramah-lingkungan-gwrD",
        time = sdf.parse("21 Sep 2022")!!
    ),
    Article(
        img = R.drawable.news7,
        title = "5 Cara Menerapkan Gaya Hidup Eco-Friendly, Bijak dalam Berkonsumsi!",
        description = "Di tengah meningkatnya kesadaran akan isu lingkungan...",
        url = "https://www.idntimes.com/life/inspiration/salma-syifa-azizah/cara-menerapkan-gaya-hidup-eco-friendly-c1c2",
        time = sdf.parse("27 Oct 2024")!!
    ),
    Article(
        img = R.drawable.news8,
        title = "Eco-friendly ‘Jelly Ice Cube’ Could Transform Cold Storage: No Plastic and Doesn’t Melt",
        description = "Researchers at the University of California, Davis,...",
        url = "https://www.goodnewsnetwork.org/eco-friendly-jelly-ice-cube-uc-davis/",
        time = sdf.parse("8 Dec 2021")!!
    ),
    Article(
        img = R.drawable.news9,
        title = "5 Eco-Friendly Reasons Why Custom Compostable Packaging is the Future of Sustainable Business",
        description = "In today's increasingly eco-conscious world,...",
        url = "https://zerowasteco.com.au/blogs/latest-eco-friendly-news/5-eco-friendly-reasons-why-custom-compostable-packaging-is-the-future-of-sustainable-business",
        time = sdf.parse("13 Nov 2024")!!
    ),
    Article(
        img = R.drawable.news10,
        title = "Make a Difference: Start Your Zero Waste Journey Today with These Simple Steps",
        description = "Embarking on a zero waste journey might seem daunting at first, but with the right approach...",
        url = "https://zerowasteco.com.au/blogs/going-zero-waste/make-a-difference-start-your-zero-waste-journey-today-with-these-simple-steps",
        time = sdf.parse("9 Sep 2024")!!
    )
).sortedByDescending { it.time }
