package com.example.dua.timeshiftproject.interfaces;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.example.dua.timeshiftproject.activites.LoginActivity;
import com.example.dua.timeshiftproject.activites.MenuActivity;
import com.parse.LogInCallback;
import com.parse.ParseUser;

/**
 * Created by dualap on 30.01.2015.
 */
public class LoginInterface {
    private LoginActivity activity;
    private WebView webView;

    public LoginInterface(LoginActivity act, WebView webView){
        this.activity = act;
        this.webView  = webView;
    }

    @JavascriptInterface
    public void logUser(String name, String password){

        ParseUser.logInInBackground(name, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, com.parse.ParseException e) {
                if (user != null) {
                    Log.v("tag", "logged in user");
                    //redirect to index.html
                    redir("file:///android_asset/www/index.html");

                } else {
                    Log.v("tag", "Did not log in user");
                    webView.post(new Runnable() {
                        @Override
                        public void run() {
                            webView.loadUrl("javascript:wrongInput()");
                        }
                    });
                }
            }
        });
    }

    @JavascriptInterface
    public void redir(final String url){

        Log.v("redir", "redir");
        webView.post(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(activity, MenuActivity.class);
                activity.startActivity(intent);
                //webView.loadUrl(url);
                //webView.loadUrl("javascript:yoyo()");
            }
        });

    }
}
