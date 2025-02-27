package by.bsuir.rssreaderapp.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.bsuir.rssreaderapp.common.RetrofitServiceGenerator
import by.bsuir.rssreaderapp.model.Item
import by.bsuir.rssreaderapp.model.RSSObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _news = MutableLiveData<List<Item>>()
    val news: LiveData<List<Item>> = _news

    private val rssLink = "https://www.onliner.by/feed"

    init {
        loadRSS() // Загружаем RSS сразу при создании ViewModel
    }

    private fun loadRSS() {
        val call = RetrofitServiceGenerator.createService().getFeed(rssLink)
        call.enqueue(object : Callback<RSSObject> {
            override fun onFailure(call: Call<RSSObject>, t: Throwable) {
                Log.d("ResponseError", "Failed to load RSS: ${t.message}")
            }

            override fun onResponse(call: Call<RSSObject>, response: Response<RSSObject>) {
                if (response.isSuccessful) {
                    response.body()?.let { rssObject ->
                        val newsList = rssObject.items.map { item ->
                            Item(
                                title = item.title,
                                pubDate = item.pubDate,
                                link = item.link,
                                guid = item.guid,
                                author = item.author,
                                thumbnail = item.thumbnail,
                                description = item.description,
                                content = item.content,
                                enclosure = item.enclosure,
                                categories = item.categories
                            )
                        }
                        _news.postValue(newsList)
                    }
                }
            }
        })
    }
}
