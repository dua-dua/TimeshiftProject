package com.example.dua.timeshiftproject.interfaces;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.example.dua.timeshiftproject.activites.ChallengeActivity;
import com.example.dua.timeshiftproject.activites.LobbyActivity;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by dualap on 05.03.2015.
 */
public class ChallengeInterface {
    private Activity activity;
    private WebView webView;

    public ChallengeInterface(ChallengeActivity act, WebView webView){
        this.activity = act;
        this.webView = webView;
    }
    @JavascriptInterface
    public void test(String id){
        Log.v("challengeTest", id);
    }
    @JavascriptInterface
    public void toChallengeLobby(String challengeId) throws ParseException {
        Log.v("challengeTest", "in toChallengeLobby");
        Log.v("challengeTest", "this is the Id" + challengeId);
        ParseQuery<ParseObject> query = new ParseQuery("Challenge");
        query.getInBackground(challengeId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                Intent intent = new Intent(activity, LobbyActivity.class);
                String sender = parseObject.getString("sender");
                String lobby = parseObject.getString("quizid");

                intent.putExtra("challenger", sender);
                intent.putExtra("lobbyId", lobby);

                intent.putExtra("fromChallenge", true);
                activity.startActivity(intent);
            }
        });

        Log.v("challengeTest", challengeId);




    }

    @JavascriptInterface
    public void toIndex(){
        activity.finish();
    }


}
