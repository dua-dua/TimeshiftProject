package com.example.dua.timeshiftproject.interfaces;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.example.dua.timeshiftproject.activites.QuizActivity;
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
    private Activity activity;
    private static WebView webView;
    private static ArrayList<String> playerList = new ArrayList<String>();
    private static ArrayList<String> readyList = new ArrayList<String>();
    private static boolean isMaster = false;


    public LobbyInterface(Activity act, WebView webView) {
        this.activity = act;
        this.webView = webView;
        webViewStatic = webView;
        staticActivity = act;
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

        Log.v("tag", "User " + name + " is ready in channel " + channel);
    }
    @JavascriptInterface
    public void redir(){
        Intent intent = new Intent(activity, QuizActivity.class);
        intent.putExtra("isMaster", true);
        intent.putExtra("counter", 1);
        activity.startActivity(intent);


    }
    @JavascriptInterface
    public void getPlayers(){
        Log.v("wtf", "wtf");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("LobbyList");
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        List channelList = installation.getList("channels");
        final String channel = channelList.get(0).toString();
        Log.v("test", channel);
        Log.v("test", installation.getInstallationId());
        query.whereEqualTo("lobbyId", channel);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                JSONObject data = null;
                ParseUser user = ParseUser.getCurrentUser();
                String name = user.getUsername();

                List<String> players = parseObject.getList("players");
                if( players.size()==1){
                    isMaster=true;
                }
                playerList.addAll(players);
                for(int a=0; a<players.size(); a++) {
                    if (players.get(a) != user.getUsername()) {
                        webView.loadUrl("javascript:printPlayers(\"" + players.get(a) + "\")");
                    }
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
        if (!name.equals(ParseUser.getCurrentUser().getUsername())) {
            playerList.add(name);
            webView.loadUrl("javascript:printPlayers(\"" + name + "\")");
        }
        else{
            Log.v("test", "received own name" + name);
        }
    }

    public static void startQuiz() {

      Intent intent = new Intent(staticActivity, QuizActivity.class);
      intent.putExtra("test", "test");
      intent.putExtra("isMaster", true);
      intent.putExtra("counter", 1);
      staticActivity.startActivity(intent);
    }
    public static void isReady(String name) {
        webView.loadUrl("javascript:isReady(\""+ name +"\")");
        readyList.add(name);
        if(isMaster){
            allReady();
        }
        else{
            Log.v("test", "Im not the master");
        }
    }
    public static void allReady(){
        Log.v("test", "InallReady");
        if(readyList.containsAll(playerList)){
            ParseInstallation installation = ParseInstallation.getCurrentInstallation();
            List channelList = installation.getList("channels");
            final String channel = channelList.get(0).toString();
            Log.v("test", "allReady");
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
    }
}
