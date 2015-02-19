package com.example.dua.timeshiftproject.activites;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.WebView;

import com.example.dua.timeshiftproject.R;
import com.example.dua.timeshiftproject.interfaces.JavaScriptInterface;
import com.example.dua.timeshiftproject.interfaces.LobbyInterface;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by dualap on 12.02.2015.
 */
public class LobbyActivity extends Activity {
    private WebView lobbyWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        lobbyWebView = (WebView)findViewById(R.id.webview5);
        lobbyWebView.getSettings().setJavaScriptEnabled(true);
        lobbyWebView.loadUrl("file:///android_asset/www/lobby.html");
        LobbyInterface lobbyInterface = new LobbyInterface(this, lobbyWebView);
        lobbyWebView.addJavascriptInterface(lobbyInterface, "LobbyInterface");

    }

    //if master
    //  LobbyInterface.addBotsToLobby();

    public void addBotsToLobby() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Scores");
        query.whereEqualTo("quizid", JavaScriptInterface.getCurrentChannel());
        query.whereEqualTo("bot", true);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                String[] names = new String[parseObjects.size()];
                for (int i = 0; i < parseObjects.size(); i++){
                    names[i] = parseObjects.get(i).getString("userid");
                }

                for(int j = 0; j < names.length; j++){
                    addBotsToLobbyWithTimer(names[j], (long)(2000+Math.random()*1500));
                }
            }
        });
    }

    public void addBotsToLobbyWithTimer(final String name, long time){
        final String channel = JavaScriptInterface.getCurrentChannel();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                JSONObject data = null;
                try {
                    data = new JSONObject();
                    data.put("type", "joinedLobby");
                    data.put("name", name);
                    data.put("channel", channel);
                }
                catch (JSONException e1) {
                    e1.printStackTrace();
                }
                //webView.loadUrl("javascript:printPlayers(\"" + name + "\")");
                Log.v("bot","added "+name);
            }
        }, time);
    }
}
