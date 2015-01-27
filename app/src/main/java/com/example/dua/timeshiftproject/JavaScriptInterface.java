package com.example.dua.timeshiftproject;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import bolts.Task;

/**
 * Created by dualap on 26.01.2015.
 */
public class JavaScriptInterface {
    private Activity activity;

    public JavaScriptInterface(Activity act){
        this.activity= act;
    }
    @JavascriptInterface
    public void doSomething(){
        JSONObject data = null;
        try {
            data = new JSONObject("{\"name\": \"jonas\"}");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ParsePush push = new ParsePush();
        push.setChannel("channel");
        push.setData(data);
        push.sendInBackground();
        Log.v("tag","Sent JSON");
    }
    @JavascriptInterface
    public String getVal(){
        return "string";
    }

    @JavascriptInterface
    public boolean isLobby(String lobbyId){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("LobbyList");

        int numb = Integer.parseInt(lobbyId);

        query.whereEqualTo("lobbyId",numb);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if(parseObject==null){
                    Log.v("test", "no such obj");
                }
                else{
                    Log.v("test", "here`s the object");
                }

            }
        });
        return true;

    }
}
