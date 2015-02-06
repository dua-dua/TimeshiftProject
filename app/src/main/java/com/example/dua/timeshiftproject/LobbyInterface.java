package com.example.dua.timeshiftproject;

import android.app.Activity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

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
    private Activity activity;
    private static WebView webView;
    private static ArrayList<String> playerList = new ArrayList<String>();
    private static ArrayList<String> readyList = new ArrayList<String>();
    private static boolean isMaster = false;


    public LobbyInterface(Activity act, WebView webView) {
        this.activity = act;
        this.webView = webView;
        webViewStatic = webView;
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

    public static void startQuiz(long startTime) {
        Log.v("test", "startQuiz");
        Date date = new Date();
        while(date.getTime()<startTime){
            Log.v("test", "stillWaiting");
        }
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("file:///android_asset/www/quiz.html");
                //webView.loadUrl("javascript:yoyo()");
            }
        });
    }
    public static void isReady(String name) {
        webView.loadUrl("javascript:isReady(\""+ name +"\")");
        readyList.add(name);
        if(isMaster){
            allReady();
        }
    }
    public static void allReady(){
        Log.v("test", "InallReady");
        String rl = String.valueOf(readyList.size());
        String pl = String.valueOf(playerList.size());
        Log.v("test", rl);
        Log.v("test", pl);
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
                data.put("startTime", date.getTime()+10000);
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
