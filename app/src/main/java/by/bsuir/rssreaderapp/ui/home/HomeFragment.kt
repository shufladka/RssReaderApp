package by.bsuir.rssreaderapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import by.bsuir.rssreaderapp.DatabaseHelper
import by.bsuir.rssreaderapp.adapter.NewsAdapter
import by.bsuir.rssreaderapp.databinding.FragmentHomeBinding
import by.bsuir.rssreaderapp.ui.pages.OneNewsActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        databaseHelper = DatabaseHelper(requireContext())

        // Настройка RecyclerView с параметром showReadLater = true
        newsAdapter = NewsAdapter(
            showReadLater = true,
            onItemClick = { item ->
                val intent = Intent(requireContext(), OneNewsActivity::class.java).apply {
                    putExtra("TITLE", item.title)
                    putExtra("DATE", item.pubDate)
                    putExtra("LINK", item.link)
                    putExtra("HTML_CONTENT", item.content)
                }
                startActivity(intent)
            },
            onReadLaterClick = { item ->
                databaseHelper.insertItem(item)
                homeViewModel.loadRSS()
            },
            onRemoveFavoriteClick = {}
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = newsAdapter

        // Обновляем список новостей в адаптере при изменении данных
        homeViewModel.news.observe(viewLifecycleOwner) { newsList ->
            newsAdapter.submitList(newsList)
            binding.swipeRefreshLayout.isRefreshing = false
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            homeViewModel.loadRSS()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
