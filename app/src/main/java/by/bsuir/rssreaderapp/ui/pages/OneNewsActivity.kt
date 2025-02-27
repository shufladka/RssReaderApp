package by.bsuir.rssreaderapp.ui.pages

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import by.bsuir.rssreaderapp.R

class OneNewsActivity : AppCompatActivity() {

    var title = ""
    var date = ""
    var link = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_one_news)

        // Настроим WebView
        val webView: WebView = findViewById(R.id.htmlWebView)
        webView.settings.javaScriptEnabled = false // Отключаем JS для безопасности
        webView.webViewClient = WebViewClient() // Открываем ссылки внутри WebView

        // Загружаем HTML-контент
        val htmlContent = """
            <html>
            <head>
                <meta name="viewport" content="width=device-width, initial-scale=1">
                <style>
                    body { font-family: sans-serif; padding: 10px; }
                    h2 { color: #007bff; }
                </style>
            </head>
            <body>
                <h2>Example News Title</h2>
                <p><i>Published on 2017-05-13 12:30:00</i></p>
                <p>This is an example <b>bold</b> text with <u>underline</u>.</p>
                <p><a href="https://example.com">Click here</a> to read more.</p>
            </body>
            </html>
        """.trimIndent()

        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)

        // Обработка системных отступов
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
