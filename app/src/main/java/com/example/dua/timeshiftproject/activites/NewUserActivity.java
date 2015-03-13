package com.example.dua.timeshiftproject.activites;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.example.dua.timeshiftproject.R;
import com.example.dua.timeshiftproject.interfaces.NewUserInterface;

public class NewUserActivity extends Activity {
    private WebView newUserWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newuser);
        newUserWebView = (WebView)findViewById(R.id.webview12);
        newUserWebView.loadUrl("file:///android_asset/www/newuser.html");
        newUserWebView.getSettings().setJavaScriptEnabled(true);
        NewUserInterface newuserInterface = new NewUserInterface(this, newUserWebView);
        newUserWebView.addJavascriptInterface(newuserInterface, "NewUserInterface");

    }
}
