package com.example.dua.timeshiftproject.interfaces;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.example.dua.timeshiftproject.activites.QuizCodeActivity;

import android.webkit.WebView;

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

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class JavaScriptInterface {
    private static android.webkit.WebView webViewStatic;
    private Activity activity;
    private WebView webView;


    public JavaScriptInterface(Activity act, WebView webView) {
        this.activity = act;
        this.webView = webView;
        webViewStatic = webView;
    }
    @JavascriptInterface
    public String getTime(){
        Date date = new Date();
        return String.valueOf(date.getTime());
    }
    @JavascriptInterface
    public void doSomething() {
        JSONObject data = null;

        try {
            data = new JSONObject();
            data.put("type","test");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ParsePush push = new ParsePush();
        push.setChannel("test");
        push.setData(data);
        push.sendInBackground();
        Log.v("tag", "Sent JSON from doSomething");
    }

    @JavascriptInterface
    public String getVal() {
        return "string";
    }

    @JavascriptInterface
    public void createUser(String name, String password) {
        Log.v("tag", "Entered create user");
        ParseUser user = new ParseUser();
        user.setUsername(name);
        user.setPassword(password);
        user.signUpInBackground();
        Log.v("tag", "created user: " + name + ", " + password);
    }

    @JavascriptInterface
    public static String getCurrentChannel(){
        ParseInstallation inst = ParseInstallation.getCurrentInstallation();
        List channels = inst.getList("channels");
        return channels.get(0).toString();
    }
    @JavascriptInterface
    public void toQuizCode(){
        Log.v("test", "toQuizCode");
        Intent intent = new Intent(activity, QuizCodeActivity.class);
        activity.startActivity(intent);
    }

    @JavascriptInterface
    public void exit() {
        activity.finish();
        System.exit(0);
    }

    public void clearLobbyArray() {
        Log.v("daab", "inClearLobbyArray");
        String channel = JavaScriptInterface.getCurrentChannel();
        ParseQuery query = new ParseQuery("LobbyList");
        final String[] scores = {};
        query.whereEqualTo("lobbyId", "test");
        query.getFirstInBackground(new GetCallback(){
            @Override

            public void done(ParseObject parseObject, ParseException e) {
                if(e != null){
                    Log.v("daab","exception");
                }else{
                    Log.v("daab","no exception");
                    parseObject.put("players", Arrays.asList(scores));
                    parseObject.put("counter", 0);
                    parseObject.saveInBackground();
                }

            }
        });
    }

    public void makeBotsFromPlayers() {
        String channel = JavaScriptInterface.getCurrentChannel();
        ParseQuery query = new ParseQuery("Scores");
        query.whereEqualTo("quizid", "test");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if(parseObjects == null){
                    Log.v("daab","list is null");
                }else {
                    Log.v("daab","list is not null");
                    for (int i = 0; i < parseObjects.size(); i++) {
                        parseObjects.get(i).put("bot", true);
                        parseObjects.get(i).saveInBackground();
                    }
                }
            }
        });
    }



    @JavascriptInterface
    public void clearDatabase(){
        Log.v("daab","clear db start");
        makeBotsFromPlayers();
        clearLobbyArray();
        Log.v("daab","clear db end");
    }

}
