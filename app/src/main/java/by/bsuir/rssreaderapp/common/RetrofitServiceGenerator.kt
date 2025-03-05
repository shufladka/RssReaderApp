package by.bsuir.rssreaderapp.common

import by.bsuir.rssreaderapp.BuildConfig
import by.bsuir.rssreaderapp.interfaces.FeedService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitServiceGenerator {
    companion object {
        fun createService(): FeedService {
            val apiUrl = "https://api.rss2json.com/v1/"
            val retrofit = Retrofit.Builder()
                .baseUrl(apiUrl)
                .client(builderHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(FeedService::class.java)
        }

        private fun builderHttpClient(): OkHttpClient {
            val client = OkHttpClient.Builder()
            if (BuildConfig.DEBUG) {
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BODY
                client.addInterceptor(logging)
            }
            return client.build()
        }
    }
}