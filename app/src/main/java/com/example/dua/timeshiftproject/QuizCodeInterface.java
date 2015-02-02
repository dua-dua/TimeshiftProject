package com.example.dua.timeshiftproject;

import android.app.Activity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONStringer;

import java.util.ArrayList;

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
        //query(hent ut alle spillere i lobby + skrive ut i HTML)
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(final ParseObject parseObject, com.parse.ParseException e) {
                if (parseObject == null) {
                    Log.v("test", "no such obj");
                    webView.post(new Runnable() {
                        @Override
                        public void run() {
                            webView.loadUrl("javascript:check('asd')");
                        }
                    });

                } else {
                    Log.v("test", "here`s the object");
                    webView.post(new Runnable() {
                        @Override
                        public void run() {
                            ParsePush.subscribeInBackground(lobbyId);
                            parseObject.add("players", ParseUser.getCurrentUser().get("username"));

                            try {
                                parseObject.save();
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                            ArrayList<String> names = (ArrayList<String>) parseObject.get("players");
                            for(int a = 0; names.size() > a; a++){
                                Log.v("test",names.get(a));

                            }
                            String derp = "lol";
                            //webView.loadUrl("javascript:check(\""+names+"\")");
                            //webView.loadUrl("javascript:printNames(\""+navn+"\")");
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
