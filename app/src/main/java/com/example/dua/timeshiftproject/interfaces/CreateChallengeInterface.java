package com.example.dua.timeshiftproject.interfaces;

import android.app.Activity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.example.dua.timeshiftproject.activites.CreateChallengeActivity;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Created by dualap on 12.03.2015.
 */
public class CreateChallengeInterface {
    private CreateChallengeActivity activity;
    private WebView webView;
    public CreateChallengeInterface(CreateChallengeActivity act, WebView webView){
        this.activity = act;
        this.webView = webView;

    }

    @JavascriptInterface
    public void getFriends(){
        Log.v("test", "in getFriends");
        final String name = null;
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("javascript:printFriend(\""+ name +"\")");
            }
        });

        Log.v("test", "after getting friends");
    }
}
