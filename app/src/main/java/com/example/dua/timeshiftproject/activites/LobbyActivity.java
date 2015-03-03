package com.example.dua.timeshiftproject.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.WebView;

import com.example.dua.timeshiftproject.R;
import com.example.dua.timeshiftproject.interfaces.JavaScriptInterface;
import com.example.dua.timeshiftproject.interfaces.LobbyInterface;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dualap on 12.02.2015.
 */
public class LobbyActivity extends Activity {
    private WebView lobbyWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.v("test", "in lobbyAct");
        setContentView(R.layout.activity_lobby);
        lobbyWebView = (WebView)findViewById(R.id.webview5);
        lobbyWebView.getSettings().setJavaScriptEnabled(true);
        lobbyWebView.loadUrl("file:///android_asset/www/lobby.html");
        LobbyInterface lobbyInterface = new LobbyInterface(this, lobbyWebView);
        lobbyWebView.addJavascriptInterface(lobbyInterface, "LobbyInterface");
        Log.v("lobby","Am I the master? "+lobbyInterface.getMaster());
        checkMaster();
    }

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
                    long time = (long)(4000 + Math.random() * 9500);
                    String name = names[j];
                    Log.v("lobby","Adding stuff #" + j);

                    addBotsToLobbyWithTimer(name, time);
                    setBotReadyTimer(name, time + 5000 + (long)Math.random()*12000);
                }
            }
        });
        Log.v("lobby","Finished addBotsToLobby");
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

                Log.v("bot","added "+name);
                ParsePush push = new ParsePush();
                push.setChannel(channel);
                push.setData(data);
                push.sendInBackground();
                Log.v("lobby","Someone powerful added me and my name is "+ name);
                addBotToLobbyList(name);
            }
        }, time);
    }

    public void setBotReadyTimer(final String name, long time){
        final String channel = JavaScriptInterface.getCurrentChannel();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                JSONObject data = null;
                try {

                    data = new JSONObject();
                    data.put("type", "userReady");
                    data.put("name", name);
                    data.put("channel", channel);
                }
                catch (JSONException e1) {
                    e1.printStackTrace();
                }
                Log.v("bot","isReady "+name);
                ParsePush push = new ParsePush();
                push.setChannel(channel);
                push.setData(data);
                push.sendInBackground();
                Log.v("lobby", name + " is ready!");
            }
        }, time);
    }


    public void checkMaster(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if(LobbyInterface.getMaster()){
                    addBotsToLobby();

                }
                Log.v("lobby","Am I the master now? "+LobbyInterface.getMaster());
            }
        }, 10000);
    }

    public void addBotToLobbyList(final String name){
        final String channel = JavaScriptInterface.getCurrentChannel();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("LobbyList");
        query.whereEqualTo("lobbyId", channel);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(final ParseObject parseObject, com.parse.ParseException e) {
            if (parseObject == null) {
                Log.v("test", "no such obj");
            } else {
                Log.v("test", "here`s the object");
                lobbyWebView.post(new Runnable() {
                    @Override
                    public void run() {
                        parseObject.add("players", name);
                        parseObject.saveInBackground();
                    }
                });
            }
            }
        });
    }
}
