package by.bsuir.rssreaderapp.ui.favorite

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.bsuir.rssreaderapp.DatabaseHelper
import by.bsuir.rssreaderapp.model.Item

class FavoriteViewModel(context: Context) : ViewModel() {

    private val databaseHelper = DatabaseHelper(context)

    private val _favoriteItems = MutableLiveData<List<Item>>()
    val favoriteItems: LiveData<List<Item>> get() = _favoriteItems

    init {
        // Загружаем избранные новости при создании ViewModel
        loadFavoriteNews()
    }

    // Загрузка избранных новостей из базы данных и обновление LiveData
    fun loadFavoriteNews() {

        // Получаем все новости из базы данных
        val favoriteFeeds = databaseHelper.getAllItems()

        // Обновляем LiveData
        _favoriteItems.value = favoriteFeeds
    }
}
