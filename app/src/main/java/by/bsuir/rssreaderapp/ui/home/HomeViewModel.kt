package by.bsuir.rssreaderapp.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.bsuir.rssreaderapp.R
import by.bsuir.rssreaderapp.common.RetrofitServiceGenerator
import by.bsuir.rssreaderapp.model.News
import by.bsuir.rssreaderapp.model.RSSObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _news = MutableLiveData<List<News>>()
    val news: LiveData<List<News>> = _news

    private val rssLink = "https://www.onliner.by/feed"

    init {
        loadRSS() // Загружаем RSS сразу при создании ViewModel
    }

    fun loadRSS() {
        val call = RetrofitServiceGenerator.createService().getFeed(rssLink)
        call.enqueue(object : Callback<RSSObject> {
            override fun onFailure(call: Call<RSSObject>, t: Throwable) {
                Log.d("ResponseError", "Failed to load RSS: ${t.message}")
            }

            override fun onResponse(call: Call<RSSObject>, response: Response<RSSObject>) {
                if (response.isSuccessful) {
                    response.body()?.let { rssObject ->
                        val newsList = rssObject.items.map { item ->
                            News(item.title, item.pubDate,
                                R.drawable.ic_launcher_background.toString()
                            ) // Используем существующий ресурс
                        }
                        _news.postValue(newsList)
                    }
                }
            }
        })
    }
}
