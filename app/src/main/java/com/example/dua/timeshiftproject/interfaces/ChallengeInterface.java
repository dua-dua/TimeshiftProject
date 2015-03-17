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
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dualap on 05.03.2015.
 */
public class ChallengeInterface {
    private Activity activity;
    private WebView webView;
    private static WebView staticWebView;

    public ChallengeInterface(ChallengeActivity act, WebView webView){
        this.activity = act;
        this.webView = webView;
        this.staticWebView = webView;
    }
    @JavascriptInterface
    public void test(String id){
        Log.v("challengeTest", id);
    }

    @JavascriptInterface
    public void toChallengeLobby(String challengeId) throws ParseException {
        Log.v("challengeTest", "in toChallengeLobby");
        Log.v("challengeTest", "this is the Id " + challengeId);
        ParseQuery<ParseObject> query = new ParseQuery("Challenge");
        query.getInBackground(challengeId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                Intent intent = new Intent(activity, LobbyActivity.class);
                String sender = parseObject.getString("sender");
                String lobby = parseObject.getString("quizid");
                Log.v("challengeTest","im here");
                intent.putExtra("challenger", sender);
                intent.putExtra("lobbyId", lobby);

                intent.putExtra("fromChallenge", true);
                activity.startActivity(intent);
                try {
                    parseObject.delete();
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }

            }
        });
        Log.v("challengeTest", challengeId);
    }

    @JavascriptInterface
    public void removeChallenge(String challengeId) throws ParseException {
        Log.v("challengeTest", "in remove");
        Log.v("challengeTest", "this is the Id" + challengeId);
        ParseQuery<ParseObject> query = new ParseQuery("Challenge");
        query.getInBackground(challengeId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                parseObject.deleteInBackground();
            }
        });
    }

    @JavascriptInterface
    public void toIndex(){
        activity.finish();
    }


}
