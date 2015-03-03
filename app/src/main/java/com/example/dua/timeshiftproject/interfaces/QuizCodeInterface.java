package com.example.dua.timeshiftproject.interfaces;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.example.dua.timeshiftproject.activites.LobbyActivity;
import com.example.dua.timeshiftproject.activites.QuizCodeActivity;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;

/**
 * Created by dualap on 30.01.2015.
 */
public class QuizCodeInterface {
    private QuizCodeActivity activity;
    private WebView webView;
    private boolean isLobby;

    public QuizCodeInterface(QuizCodeActivity act, WebView webView){
        this.activity = act;
        this.webView = webView;
    }

    @JavascriptInterface
    public void isLobby(final String lobbyId) {
        if (lobbyId.length() == 0) {
            webView.loadUrl("javascript:check()");
        }
        ParseQuery<ParseObject> query = ParseQuery.getQuery("LobbyList");
        query.whereEqualTo("lobbyId", lobbyId);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(final ParseObject parseObject, com.parse.ParseException e) {
                if (parseObject == null) {
                    webView.post(new Runnable() {
                        @Override
                        public void run() {
                            webView.loadUrl("javascript:check('no such code')");
                        }
                    });

                } else {
                    webView.post(new Runnable() {
                        @Override
                        public void run() {
                            ParsePush.subscribeInBackground(lobbyId);
                            parseObject.add("players", ParseUser.getCurrentUser().get("username"));
                            parseObject.put("counter",0);
                            try {
                                parseObject.save();
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                            ArrayList<String> names = (ArrayList<String>) parseObject.get("players");
                            if(names.size()==1){
                                parseObject.put("master", ParseUser.getCurrentUser().getUsername());

                                    parseObject.saveInBackground();
                            }
                            for(int a = 0; names.size() > a; a++){
                                if(names.get(a)!=ParseUser.getCurrentUser().getUsername()){
                                    Log.v("test",names.get(a));
                                }
                            }
                            Intent intent = new Intent(activity, LobbyActivity.class);
                            activity.startActivity(intent);
                            activity.finish();
                        }
                    });
                }
            }


        });
    }

    @JavascriptInterface
    public void toLobby(){
        Intent intent = new Intent(activity, LobbyActivity.class);
        activity.startActivity(intent);
    }
}
