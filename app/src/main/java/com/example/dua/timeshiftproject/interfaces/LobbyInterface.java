package com.example.dua.timeshiftproject.interfaces;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.example.dua.timeshiftproject.activites.QuizActivity;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LobbyInterface {
    private static WebView webViewStatic;
    private static Activity staticActivity;
    private static WebView webView;
    private static ArrayList<String> playerList = new ArrayList<String>();
    private static ArrayList<String> readyList = new ArrayList<String>();
    private static boolean isMaster;
    private static boolean inLobby = false;


    public LobbyInterface(Activity act, WebView webView, Boolean isMaster) {
        this.webView = webView;
        webViewStatic = webView;
        staticActivity = act;
        this.isMaster = isMaster;
        inLobby = true;
    }

    @JavascriptInterface
    public void playerReady() {
        ParseUser pUser = ParseUser.getCurrentUser();
        String name = "";
        String channel = "";

        name = pUser.getUsername();
        JSONObject data = null;
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        List channelList = installation.getList("channels");
        channel = channelList.get(0).toString();

        try {
            data = new JSONObject();
            data.put("type","userReady");
            data.put("name",name);
            data.put("channel",channel);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        ParsePush push = new ParsePush();
        push.setChannel(channel);
        push.setData(data);
        push.sendInBackground();
    }

    @JavascriptInterface
    public void redir(){
        Intent intent = new Intent(staticActivity, QuizActivity.class);
        intent.putExtra("isMaster", true);
        intent.putExtra("counter", 1);

        staticActivity.startActivity(intent);
        staticActivity.finish();
    }

    @JavascriptInterface
    public void getPlayers(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("LobbyList");
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        List channelList = installation.getList("channels");
        final String channel = channelList.get(0).toString();
        query.whereEqualTo("lobbyId", channel);

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {

                JSONObject data = null;
                ParseUser user = ParseUser.getCurrentUser();
                String name = user.getUsername();

                List<String> players = parseObject.getList("players");
                List<String> readyPlayers = parseObject.getList("readyPlayers");
                Log.v("master", players.size()+ " <- size");
                if( players.size()==1){
                    isMaster=true;
                }
                playerList.addAll(players);

                for(int a=0; a<players.size(); a++) {
                    if (players.get(a) != user.getUsername()) {
                        webView.loadUrl("javascript:printPlayers(\"" + players.get(a) + "\")");
                    }
                }
                for(int a=0; a<readyPlayers.size(); a++){
                    String readyName = readyPlayers.get(a);
                    webView.loadUrl("javascript:isReady(\""+readyName+"\")");
                }
                try {
                    data = new JSONObject();
                    data.put("type", "joinedLobby");
                    data.put("name", name);
                    data.put("channel", channel);
                }
                catch (JSONException e1) {
                    e1.printStackTrace();
                }
                ParsePush push = new ParsePush();
                push.setChannel(channel);
                push.setData(data);
                push.sendInBackground();
            }
        });
    }

    public static void joinedLobby(String name){
        if(inLobby==true){
            if (!name.equals(ParseUser.getCurrentUser().getUsername())) {
                playerList.add(name);
                webView.loadUrl("javascript:printPlayers(\"" + name + "\")");
            }else{
                Log.v("master", "received own name " + name);
            }
        }else{
            Log.v("master", "not in lobby yet");
        }

    }

    public static void startQuiz() {
      Intent intent = new Intent(staticActivity, QuizActivity.class);
      intent.putExtra("test", "test");
      intent.putExtra("isMaster", true);
      intent.putExtra("counter", 1);
      staticActivity.startActivity(intent);
      staticActivity.finish();
      playerList.clear();
      readyList.clear();
    }

    public static void isReady(String name) {
        String channel = JavaScriptInterface.getCurrentChannel();
        webView.loadUrl("javascript:isReady(\""+ name +"\")");
        readyList.add(name);

        if(isMaster){
             allReady();
        }
        else{
            Log.v("master", "I'm not the master");
        }
    }

    public static void sendCountdown(){
        JSONObject data = null;
        String channel = JavaScriptInterface.getCurrentChannel();

        try {
            data = new JSONObject();
            data.put("type", "startCountdown");
            data.put("channel", channel);
        }
        catch (JSONException e1) {
            e1.printStackTrace();
        }
        ParsePush push = new ParsePush();
        push.setChannel(channel);
        push.setData(data);
        push.sendInBackground();
    }

    public void removeFromReady(){
        readyList.remove(ParseUser.getCurrentUser().getUsername());
    }

    public void emptyLists(){
        readyList.clear();
        playerList.clear();
    }

    public static void sendStartQuiz(){
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        List channelList = installation.getList("channels");
        final String channel = channelList.get(0).toString();

        JSONObject data = null;
        Date date = new Date();

        try {
            data = new JSONObject();
            data.put("type", "startQuiz");
            //data.put("startTime", date.getTime()+30000);
            data.put("channel", channel);
        }
        catch (JSONException e1) {
            e1.printStackTrace();
        }
        ParsePush push = new ParsePush();
        push.setChannel(channel);
        push.setData(data);
        push.sendInBackground();
    }

    @JavascriptInterface
    public static void startCountdown(){
        webView.loadUrl("javascript:countdown()");
    }

    public static void allReady(){
        if(readyList.containsAll(playerList)){
            ParseQuery<ParseObject> query = ParseQuery.getQuery("LobbyList");
            query.whereEqualTo("lobbyId", JavaScriptInterface.getCurrentChannel());
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    parseObject.put("locked", true);
                }
            });
            sendCountdown();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    sendStartQuiz();
                }
            }, 5000);

        }
    }

    public static boolean getMaster(){
        return isMaster;
    }
}
