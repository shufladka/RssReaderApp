package by.bsuir.rssreaderapp.ui.pages

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import by.bsuir.rssreaderapp.R

class OneNewsActivity : AppCompatActivity() {

    private var title = ""
    private var date = ""
    private var link = ""
    private var htmlContent = ""

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_one_news)

        // Извлекаем данные из Intent
        title = intent.getStringExtra("TITLE") ?: "No Title"
        date = intent.getStringExtra("DATE") ?: "No Date"
        link = intent.getStringExtra("LINK") ?: ""
        htmlContent = intent.getStringExtra("HTML_CONTENT") ?: ""

        // Настройка Toolbar с кнопкой "назад"
        val toolbar: Toolbar = findViewById(R.id.toolbar_one)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Действие кнопки "назад"
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()   // Возвращаемся назад к HomeFragment
        }

        // Настроим WebView
        val webView: WebView = findViewById(R.id.htmlWebView)
//        webView.settings.javaScriptEnabled = false // Отключаем JS для безопасности
        webView.webViewClient = WebViewClient() // Открываем ссылки внутри WebView

        // Скрываем текстовые элементы, пока не начнется загрузка
        hideTextViews()

        // Загружаем HTML-контент
        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)

        // Отображение заголовка и даты новости
        val titleTextView: TextView = findViewById(R.id.oneTextTitle)
        val dateTextView: TextView = findViewById(R.id.oneTextPubdate)

        titleTextView.text = title
        dateTextView.text = date

        // Когда WebView закончит загрузку, скрываем старые элементы и показываем новостную страницу
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                hideTextViews()  // Скрываем текстовые элементы при открытии новостной страницы
            }
        }
    }

    private fun hideTextViews() {
        // Скрываем текстовые элементы
        findViewById<TextView>(R.id.oneTextTitle).visibility = android.view.View.GONE
        findViewById<TextView>(R.id.oneTextPubdate).visibility = android.view.View.GONE
    }

    private fun showTextViews() {
        // Показываем текстовые элементы, если нужно
        findViewById<TextView>(R.id.oneTextTitle).visibility = android.view.View.VISIBLE
        findViewById<TextView>(R.id.oneTextPubdate).visibility = android.view.View.VISIBLE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed() // Используем новый способ для обработки кнопки "назад"
        return true
    }
}
