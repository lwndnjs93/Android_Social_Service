package com.example.leejuwon.social_service;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebviewActivity3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview3);

        WebView webView = (WebView) this.findViewById(R.id.web3);
        webView.setWebViewClient(new WebViewClient());

        webView.loadUrl("http://m.daum.net");
    }
}
