package by.bsuir.rssreaderapp.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import by.bsuir.rssreaderapp.adapter.NewsAdapter
import by.bsuir.rssreaderapp.databinding.FragmentFavoriteBinding
import by.bsuir.rssreaderapp.DatabaseHelper
import by.bsuir.rssreaderapp.ui.pages.OneNewsActivity

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Использование фабрики для создания ViewModel с передачей контекста
        favoriteViewModel =
            ViewModelProvider(this, FavoriteViewModelFactory(requireContext()))[FavoriteViewModel::class.java]

        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Инициализация DatabaseHelper
        databaseHelper = DatabaseHelper(requireContext())

        // Настройка RecyclerView
        newsAdapter = NewsAdapter(
            showReadLater = false,
            onItemClick = { item ->
                val intent = Intent(requireContext(), OneNewsActivity::class.java).apply {
                    putExtra("TITLE", item.title)
                    putExtra("DATE", item.pubDate)
                    putExtra("LINK", item.link)
                    putExtra("HTML_CONTENT", item.content)
                }
                startActivity(intent)
            },
            onReadLaterClick = {},
            onRemoveFavoriteClick = { item ->
                databaseHelper.deleteItem(item)
                favoriteViewModel.loadFavoriteNews()
            }
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = newsAdapter

        // Наблюдаем за изменениями в favoriteItems
        favoriteViewModel.favoriteItems.observe(viewLifecycleOwner) { favoriteItems ->
            newsAdapter.submitList(favoriteItems)
            binding.swipeRefreshLayout.isRefreshing = false
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            favoriteViewModel.loadFavoriteNews()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
