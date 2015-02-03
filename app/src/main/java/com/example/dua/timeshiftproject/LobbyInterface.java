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
import java.util.List;


public class LobbyInterface {
    private static WebView webViewStatic;
    private Activity activity;
    private static WebView webView;


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
                List<String> players = parseObject.getList("players");
                for(int a=0; a<players.size(); a++) {
                    if (players.get(a) != user.getUsername()) {
                        Log.v("test", "hva skjer");
                        Log.v("test", players.get(a));
                        Log.v("test", user.getUsername());
                        webView.loadUrl("javascript:printPlayers(\"" + players.get(a) + "\")");
                    }
                }
            }
        });
    }

    public static void joinedLobby(String name){
        webView.loadUrl("javascript:printPlayers(\""+ name +"\")");

    }

    public static void isReady(String name) {
        Log.v("test", "isReady");
        Log.v("test", name);
        webView.loadUrl("javascript:isReady(\""+ name +"\")");
    }
}
