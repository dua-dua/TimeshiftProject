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

import java.util.List;

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
        List friendArray = ParseUser.getCurrentUser().getList("friends");
        if(friendArray==null){
            Log.v("test", "you have no friends");
        }
        else{
            for(int a =0; a<friendArray.size(); a++){
                printFriend(friendArray.get(a).toString());
            }
        }


        Log.v("test", "after getting friends");
    }
    @JavascriptInterface
    public void sendChallenges(String friend){
        String channel = activity.getIntent().getExtras().getString("channel");
        ParseObject challenge = new ParseObject("Challenge");
        Log.v("test", "before putting");
        Log.v("test", friend);
        challenge.put("sender", ParseUser.getCurrentUser().getUsername());
        challenge.put("receiver", friend);
        challenge.put("quizid", channel);
        Log.v("test", "after putting");
        challenge.saveInBackground();
    }

    private void printFriend(final String friend) {

        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("javascript:printFriend(\"" + friend + "\")");
            }
        });
    }
}
