package com.example.leejuwon.social_service;

        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.webkit.WebView;
        import android.webkit.WebViewClient;

public class WebviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        WebView webView = (WebView) this.findViewById(R.id.web);
        webView.setWebViewClient(new WebViewClient());

        webView.loadUrl("http://localhost:192.168.0.12/spring_ptj/portfolio?kid=skdigur@naver.com");
    }
}
