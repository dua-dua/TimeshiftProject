package com.example.dua.timeshiftproject.interfaces;

import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.example.dua.timeshiftproject.activites.LoginActivity;
import com.example.dua.timeshiftproject.activites.MenuActivity;
import com.example.dua.timeshiftproject.activites.NewUserActivity;
import com.parse.LogInCallback;
import com.parse.ParseUser;

/**
 * Created by dualap on 30.01.2015.
 */
public class NewUserInterface {
    private NewUserActivity activity;
    private WebView webView;

    public NewUserInterface(NewUserActivity act, WebView webView){
        this.activity = act;
        this.webView  = webView;
    }

    @JavascriptInterface
    public void returnToLogin(){
        activity.finish();
    }

    @JavascriptInterface
    public void createUser(String name, String password) {
        Log.v("newuser","new user: "+name+", "+password);
        ParseUser user = new ParseUser();
        user.setUsername(name);
        user.setPassword(password);
        user.signUpInBackground();
    }

}
