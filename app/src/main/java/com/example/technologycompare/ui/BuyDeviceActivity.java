package com.example.technologycompare.ui;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.technologycompare.R;

public class BuyDeviceActivity extends AppCompatActivity {

    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_device);

        webView = findViewById(R.id.webViewBuyDevice);

        Intent intent = getIntent();
        String link = intent.getStringExtra("link");
        Toast.makeText(this, link, Toast.LENGTH_SHORT).show();

        webView.loadUrl(link);
        webView.setWebViewClient(new WebViewClient());

    }
}