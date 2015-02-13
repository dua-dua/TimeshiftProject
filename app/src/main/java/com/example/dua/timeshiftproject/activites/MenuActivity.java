package com.example.dua.timeshiftproject.activites;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.example.dua.timeshiftproject.R;
import com.example.dua.timeshiftproject.interfaces.JavaScriptInterface;
import com.example.dua.timeshiftproject.interfaces.LoginInterface;

/**
 * Created by dualap on 10.02.2015.
 */
public class MenuActivity extends Activity{
    private WebView menuWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        menuWebView = (WebView)findViewById(R.id.webview3);
        menuWebView.loadUrl("file:///android_asset/www/index.html");
        JavaScriptInterface jsInterface = new JavaScriptInterface(this, menuWebView);


        menuWebView.getSettings().setJavaScriptEnabled(true);
        menuWebView.addJavascriptInterface(jsInterface, "JSInterface");


    }
}
