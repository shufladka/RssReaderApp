package by.bsuir.rssreaderapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import by.bsuir.rssreaderapp.adapter.NewsAdapter
import by.bsuir.rssreaderapp.databinding.FragmentHomeBinding
import by.bsuir.rssreaderapp.ui.pages.OneNewsActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Настройка RecyclerView
        newsAdapter = NewsAdapter { item ->
            // Переход на активность для просмотра новости
            val intent = Intent(requireContext(), OneNewsActivity::class.java).apply {
                putExtra("TITLE", item.title)
                putExtra("DATE", item.pubDate)
                putExtra("LINK", item.link)
                putExtra("HTML_CONTENT", item.content) // Или другой HTML-контент
            }
            startActivity(intent)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = newsAdapter

        // Обновляем список новостей в адаптере при изменении данных
        homeViewModel.news.observe(viewLifecycleOwner) { newsList ->
            newsAdapter.submitList(newsList)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
