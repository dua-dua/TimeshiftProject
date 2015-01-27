package com.example.dua.timeshiftproject;

import android.app.Activity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.GetCallback;
import com.parse.ParseQuery;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;


/**
 * Created by dualap on 26.01.2015.
 */
public class JavaScriptInterface {
    private Activity activity;
    private boolean isLobby;

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
        Log.v("test", "test");
        if (lobbyId.length()==0){
            Log.v("test", "empty String");
            return false;
        }
        ParseQuery<ParseObject> query = ParseQuery.getQuery("LobbyList");
        int numb = Integer.parseInt(lobbyId);
        query.whereEqualTo("lobbyId",numb);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (parseObject == null) {
                    Log.v("test", "no such obj");
                    isLobby = false;
                } else {
                    Log.v("test", "here`s the object");
                    isLobby = true;
                }
            }
        });

        return isLobby;
    }

    @JavascriptInterface
    public void createUser(String name, String password){
        ParseUser user = new ParseUser();
        user.setUsername(name);
        user.setPassword(password);
        user.signUpInBackground();
        Log.v("tag","created user");
    }

    @JavascriptInterface
    public void logUser(String name, String password){

        Log.v("tag","logged in user: "+name);
    }
}
