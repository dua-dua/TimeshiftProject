package com.example.dua.timeshiftproject;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.parse.ParsePush;

import org.json.JSONException;
import org.json.JSONObject;

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



        Log.v("derp", "derp");
        Intent intent = new Intent();
        intent.setAction("com.tutorialspoint.OTHER_CUSTOM_INTENT");
        activity.startActivity(intent);


    }
}
