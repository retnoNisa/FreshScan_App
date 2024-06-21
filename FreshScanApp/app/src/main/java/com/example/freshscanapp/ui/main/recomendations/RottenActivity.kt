package com.example.freshscanapp.ui.main.recomendations

import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.freshscanapp.R

class RottenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_rotten)

        val webView1: WebView = findViewById(R.id.webview1)
        val webView2: WebView = findViewById(R.id.webview2)
        val webView3: WebView = findViewById(R.id.webview3)
        val webView4: WebView = findViewById(R.id.webview4)
        val webView5: WebView = findViewById(R.id.webview5)

        setupWebView(webView1, "https://www.youtube.com/embed/mDIVpJgjoXQ")
        setupWebView(webView2, "https://www.youtube.com/embed/LJgHVP2rr-8")
        setupWebView(webView3, "https://www.youtube.com/embed/6izQfXMO9nY")
        setupWebView(webView4, "https://www.youtube.com/embed/1hyQscoaGhw")
        setupWebView(webView5, "https://www.youtube.com/embed/ZyvcmpyD7FM")
    }

    private fun setupWebView(webView: WebView, url: String) {
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        val videoUrl = "<html><body style='margin:0;padding:0;'><iframe width=\"100%\" height=\"100%\" src=\"$url\" frameborder=\"0\" allowfullscreen></iframe></body></html>"
        webView.loadData(videoUrl, "text/html", "utf-8")
    }
}
