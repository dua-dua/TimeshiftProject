package com.example.dua.timeshiftproject.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.example.dua.timeshiftproject.R;
import com.example.dua.timeshiftproject.interfaces.JavaScriptInterface;
import com.example.dua.timeshiftproject.interfaces.LoginInterface;

public class MenuActivity extends Activity{
    private WebView menuWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        menuWebView = (WebView)findViewById(R.id.webview3);
        menuWebView.loadUrl("file:///android_asset/www/index.html");
        JavaScriptInterface jsInterface = new JavaScriptInterface(this, menuWebView);
        Intent intent = getIntent();
        String text = intent.getStringExtra("test");
        menuWebView.getSettings().setJavaScriptEnabled(true);
        menuWebView.addJavascriptInterface(jsInterface, "JSInterface");



    }
}
