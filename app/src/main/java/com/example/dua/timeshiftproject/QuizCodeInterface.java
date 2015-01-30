package com.example.dua.timeshiftproject;

import android.app.Activity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;

/**
 * Created by dualap on 30.01.2015.
 */
public class QuizCodeInterface {
    private Activity activity;
    private WebView webView;
    private boolean isLobby;

    public QuizCodeInterface(Activity act, WebView webView){
        this.activity = act;
        this.webView = webView;
    }

    @JavascriptInterface
    public void isLobby(final String lobbyId) {
        Log.v("test", "test");
        Log.v("test", lobbyId);
        if (lobbyId.length() == 0) {
            Log.v("test", "empty String");
            webView.loadUrl("javascript:check()");
        }
        ParseQuery<ParseObject> query = ParseQuery.getQuery("LobbyList");

        query.whereEqualTo("lobbyId", lobbyId);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, com.parse.ParseException e) {
                if (parseObject == null) {
                    Log.v("test", "no such obj");
                    webView.post(new Runnable() {
                        @Override
                        public void run() {
                            webView.loadUrl("javascript:check()");
                        }
                    });

                } else {
                    Log.v("test", "here`s the object");
                    webView.post(new Runnable() {
                        @Override
                        public void run() {
                            ParsePush.subscribeInBackground(lobbyId);
                            webView.loadUrl("file:///android_asset/www/lobby.html");
                        }
                    });

                }
            }


        });

    }
    @JavascriptInterface
    public void toLobby(){

    }
}
