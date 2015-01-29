package com.example.dua.timeshiftproject;

import android.app.Activity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.parse.LogInCallback;
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
        query.getFirstInBackground();
        return true;

    }

    @JavascriptInterface
    public void createUser(String name, String password){
        Log.v("tag","Entered create user");
        ParseUser user = new ParseUser();
        user.setUsername(name);
        user.setPassword(password);
        user.signUpInBackground();
        Log.v("tag","created user: "+name+", "+password);
    }

    @JavascriptInterface
    public void logUser(String name, String password){

        ParseUser.logInInBackground(name, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, com.parse.ParseException e) {
                if (user != null) {
                    Log.v("tag","logged in user");
                    //redirect to index.html

                } else {
                    Log.v("tag","Did not log in user");
                }
            }
        });
    }
}
