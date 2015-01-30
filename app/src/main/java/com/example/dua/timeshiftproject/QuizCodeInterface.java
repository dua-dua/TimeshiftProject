package com.example.dua.timeshiftproject;

import android.app.Activity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.parse.GetCallback;
import com.parse.ParseObject;
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
    public boolean isLobby(String lobbyId) {
        Log.v("test", "test");
        if (lobbyId.length() == 0) {
            Log.v("test", "empty String");
            return false;
        }
        ParseQuery<ParseObject> query = ParseQuery.getQuery("LobbyList");
        int numb = Integer.parseInt(lobbyId);
        query.whereEqualTo("lobbyId", numb);

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

                    isLobby = false;
                } else {
                    Log.v("test", "here`s the object");
                    isLobby = true;
                }
            }


        });

        return isLobby;
    }
}
