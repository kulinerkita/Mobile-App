package com.capstone.kulinerkita.ui.news

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.kulinerkita.R
import com.capstone.kulinerkita.data.model.NewsItem
import com.capstone.kulinerkita.databinding.FragmentNewsBinding

class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dummyNews = listOf(
            NewsItem(
                "Berita 1",
                "Deskripsi singkat berita pertama.",
                R.drawable.food_1,
                "2 jam yang lalu"
            ),
            NewsItem(
                "Berita 2",
                "Deskripsi singkat berita kedua.",
                R.drawable.food_1,
                "1 hari yang lalu"
            )
        )

        val newsAdapter = NewsAdapter(dummyNews)
        binding.recyclerViewNews.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = newsAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}