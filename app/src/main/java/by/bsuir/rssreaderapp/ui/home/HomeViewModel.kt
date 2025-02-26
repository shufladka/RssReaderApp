package by.bsuir.rssreaderapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.bsuir.rssreaderapp.News
import by.bsuir.rssreaderapp.R

class HomeViewModel : ViewModel() {

//    private val _text = MutableLiveData<String>().apply {
//        value = "This is home Fragment"
//    }
//    val text: LiveData<String> = _text

    private val _news = MutableLiveData<List<News>>().apply {
        value = listOf(
            News("Title 1", "2017-05-13 12:30:00", R.drawable.ic_launcher_background),
            News("Title 2", "2023-02-15 08:45:00", R.drawable.ic_launcher_background)
        )
    }
    val news: LiveData<List<News>> = _news
}