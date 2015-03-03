package com.example.dua.timeshiftproject.activites;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.example.dua.timeshiftproject.R;
import com.example.dua.timeshiftproject.interfaces.LoginInterface;

/**
 * Created by dualap on 10.02.2015.
 */
public class LoginActivity extends Activity {
    private WebView loginWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginWebView = (WebView)findViewById(R.id.webview2);
        loginWebView.loadUrl("file:///android_asset/www/login.html");
        loginWebView.getSettings().setJavaScriptEnabled(true);
        LoginInterface loginInterface = new LoginInterface(this, loginWebView);
        loginWebView.addJavascriptInterface(loginInterface, "LoginInterface");

    }

    public void toMenu(){

    }

}
