package com.example.dua.timeshiftproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;
import com.parse.ParsePushBroadcastReceiver;
import org.json.JSONException;
import org.json.JSONObject;

public class MyParseReceiver extends ParsePushBroadcastReceiver {

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        getObjectType(context, intent);
    }

    public void getObjectType(Context context, Intent intent){
        Log.v("tag", "received JSON in getObjectType");
        String type = "";
        JSONObject obj = null;
        try {
            obj = new JSONObject(intent.getExtras().getString("com.parse.Data"));
            type = obj.getString("type").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        switch (type){
            case "userAnswer": userAnswer(context, obj);
                break;
            case "userReady": userReady(context, obj);
                break;
            case "test": runTest(context);
                break;
            default: Toast.makeText(context, "Did not recognize the JSON D:", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void userAnswer(Context context, JSONObject obj) {
        String name = "";
        try {
            name = obj.getString("name").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JavaScriptInterface.sendHTMLNotification(name);

        Toast.makeText(context, name+" answered!", Toast.LENGTH_LONG).show();

    }

    private void userReady(Context context, JSONObject obj) {
        Toast.makeText(context, "Received userReady!", Toast.LENGTH_LONG).show();
    }

    private void runTest(Context context) {
        Toast.makeText(context, "Received test!", Toast.LENGTH_LONG).show();
    }
}
