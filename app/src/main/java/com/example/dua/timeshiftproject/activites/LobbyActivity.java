package com.example.dua.timeshiftproject.activites;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.example.dua.timeshiftproject.App;
import com.example.dua.timeshiftproject.MyParseReceiver;
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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by dualap on 12.02.2015.
 */
public class LobbyActivity extends Activity {
    private WebView lobbyWebView;
    private boolean fromChallenge;
    private Handler handler;
    private Runnable addBotsRun;
    private Runnable addBotsWithTimerRun;
    private Runnable setBotReadyRun;
    private ParseQuery<ParseObject> addBotQuery;
    private LobbyInterface lobbyInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        handler= new Handler();
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        fromChallenge = intent.getExtras().getBoolean("fromChallenge");
        if(fromChallenge) {
            Log.v("challenge", "from challenge");
            ParsePush.subscribeInBackground(intent.getExtras().getString("lobbyId"));
            ParseQuery<ParseObject> query = ParseQuery.getQuery("LobbyList");
            String lobby = intent.getExtras().getString("lobbyId");
            final String challenger = intent.getExtras().getString("challenger");
            query.whereEqualTo("lobbyId", lobby);
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    parseObject.add("players", ParseUser.getCurrentUser().getUsername());
                    parseObject.saveInBackground();
                }
            });

            addChallengerBot(challenger);

        }else{
            Log.v("challenge", "not from challenge");
        }

        setContentView(R.layout.activity_lobby);
        lobbyWebView = (WebView)findViewById(R.id.webview5);
        lobbyWebView.getSettings().setJavaScriptEnabled(true);
        lobbyWebView.loadUrl("file:///android_asset/www/lobby.html");

        lobbyInterface = new LobbyInterface(this, lobbyWebView, intent.getExtras().getBoolean("fromChallenge"));
        lobbyWebView.addJavascriptInterface(lobbyInterface, "LobbyInterface");
        checkMaster();
    }
    @Override
    public boolean onKeyDown(int keyKode, KeyEvent event){
        if(keyKode==KeyEvent.KEYCODE_BACK){
            if(LobbyInterface.getMaster()){
                Log.v("test", "in endQuiz as master");
                ParseQuery<ParseObject> query = ParseQuery.getQuery("LobbyList");
                query.whereEqualTo("lobbyId", JavaScriptInterface.getCurrentChannel());
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject parseObject, ParseException e) {
                        Log.v("endQuiz", "in endquiz, setting locked to true");
                        parseObject.put("locked", true);
                        parseObject.saveInBackground();
                        if(parseObject.getBoolean("locked")){
                            Log.v("endQuiz", "locked is true");
                        }
                    }
                });
                sendMasterHasLeft();
                playerCleanUp();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        endQuiz();
                    }
                }, 60000);
            }
            else{
                playerCleanUp();
                ParsePush.unsubscribeInBackground(JavaScriptInterface.getCurrentChannel());
            }
        }
        return super.onKeyDown(keyKode, event);
    }

    private void sendMasterHasLeft(){
        JSONObject data=null;
        try{
            data = new JSONObject();
            data.put("type", "masterHasLeft");
            data.put("channel", JavaScriptInterface.getCurrentChannel());


        }catch(JSONException e){
            e.printStackTrace();
        }
        ParsePush push = new ParsePush();
        push.setChannel(JavaScriptInterface.getCurrentChannel());
        push.setData(data);
        push.sendInBackground();


    }
    private void startingQuiz(){

    }
    private void playerCleanUp(){
        ParseQuery<ParseObject> query= ParseQuery.getQuery("LobbyList");
        query.whereEqualTo("lobbyId", JavaScriptInterface.getCurrentChannel());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                boolean quizLocked = parseObject.getBoolean("locked");
                if(quizLocked){
                    Log.v("test", "quiz is running, no actions necceccary");
                }
                else{
                    lobbyInterface.emptyLists();
                    Log.v("endQuiz", "removing player:"+ ParseUser.getCurrentUser().getUsername());
                    Log.v("endQuiz", "list before" +parseObject.getList("players").toString());
                    parseObject.getList("players").remove(ParseUser.getCurrentUser().getUsername());
                    Log.v("endQuiz", "list after"+ parseObject.getList("players").toString());
                    parseObject.getList("readyPlayers").remove(ParseUser.getCurrentUser().getUsername());
                    parseObject.saveInBackground();

                }
            }
        });
    }


    private void endQuiz() {
        Log.v("endQuiz", "starting cleanup");
        final String[] empty= {};
        ParseQuery<ParseObject> query = ParseQuery.getQuery("LobbyList");
        query.whereEqualTo("lobbyId", JavaScriptInterface.getCurrentChannel());

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                parseObject.put("players", Arrays.asList(empty));
                parseObject.put("readyPlayers", Arrays.asList(empty));
                parseObject.put("counter", 0);
                parseObject.put("locked", false);
                parseObject.saveInBackground();
                Log.v("endQuiz", parseObject.getList("players").toString());
                parseObject.getList("players").clear();
            }
        });
        ParsePush.unsubscribeInBackground(JavaScriptInterface.getCurrentChannel());
    }

    public void addChallengerBot(String challenger){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Scores");
        query.whereEqualTo("quizid", JavaScriptInterface.getCurrentChannel());
        query.whereEqualTo("userid", challenger);
        Log.v("test", "challengerName " + challenger);
        Log.v("test", JavaScriptInterface.getCurrentChannel());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if(parseObject==null){
                    Log.v("test", "no object");
                }
                String challengerName = parseObject.getString("userid");
                addBotsToLobbyWithTimer(challengerName, 1000);
                setBotReadyTimer(challengerName, 5000 + (long)Math.random()*12000 );
            }
        });

    }

    public void addBotsToLobby(final boolean fromChallenge) {
        Log.v("master", "in addBotsToLobby");
        addBotsRun = new Runnable() {
            @Override
            public void run() {
                Log.v("master", "inaddBotsRun");
                addBotQuery = ParseQuery.getQuery("Scores");
                addBotQuery.whereEqualTo("quizid", JavaScriptInterface.getCurrentChannel());
                addBotQuery.whereEqualTo("bot", true);
                if (fromChallenge) {
                    String challenger = getIntent().getExtras().getString("challenger");
                    addBotQuery.whereNotEqualTo("userid", challenger);


                }
                addBotQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, ParseException e) {
                        String[] names = new String[parseObjects.size()];
                        for (int i = 0; i < parseObjects.size(); i++) {
                            names[i] = parseObjects.get(i).getString("userid");
                        }

                        for (int j = 0; j < names.length; j++) {
                            long time = (long) (Math.random() * 9500);
                            String name = names[j];
                            Log.v("lobby", "Adding stuff #" + j);

                            addBotsToLobbyWithTimer(name, time);
                            setBotReadyTimer(name, time + 5000 + (long) Math.random() * 12000);
                        }
                    }
                });
            }

        };
        handler.post(addBotsRun);
    }



    public void addBotsToLobbyWithTimer(final String name, final long time) {
        Log.v("master", "in addBotsToLobbywithTimer");
        addBotsWithTimerRun = new Runnable() {
            @Override
            public void run() {
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
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        Log.v("test", "adding bots with timer");
                        ParsePush push = new ParsePush();
                        push.setChannel(channel);
                        push.setData(data);
                        push.sendInBackground();
                        addBotToLobbyList(name);
                    }
                }, time);
            }


        };
        handler.post(addBotsWithTimerRun);
    }

    public void setBotReadyTimer(final String name, final long time) {
        Log.v("master", "setBotReadyTimer");
        setBotReadyRun = new Runnable() {
            @Override
            public void run() {
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
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                        ParsePush push = new ParsePush();
                        push.setChannel(channel);
                        push.setData(data);
                        push.sendInBackground();
                    }
                }, time);
            }



        };
        handler.post(setBotReadyRun);
    }

    public void checkMaster(){
        Log.v("test", "in checkMaster");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Log.v("master", LobbyInterface.getMaster()+"");
                if(LobbyInterface.getMaster()){
                    addBotsToLobby(fromChallenge);
                    Log.v("test", "in checkMaster adding bots");
                }
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
            } else {
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
