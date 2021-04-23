package com.shentu.gamebox.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;

import com.shentu.gamebox.R;
import com.shentu.gamebox.base.BaseActivity;
import com.shentu.gamebox.utils.LogUtils;

//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;


public class WebViewActivity extends BaseActivity {

    private WebView webView;
    private String html;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setTitle() {

    }

    @Override
    protected void initView() {
        /*显示福利详情*/
        webView = findViewById(R.id.web_view);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_webview;
    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
//            String url = bundle.getString("uri");
//            LogUtils.e(url);
//            webView.loadUrl(url);
            html = bundle.getString("html");

        }
//        webView.getSettings().setJavaScriptEnabled(true);

        webView.loadDataWithBaseURL(null,html,"text/html","utf-8",null);


    }
}
