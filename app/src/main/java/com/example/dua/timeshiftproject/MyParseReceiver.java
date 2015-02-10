package com.example.dua.timeshiftproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
            case "joinedLobby": userJoinedLobby(context, obj);
                break;
            case "test": runTest(context);
                break;
            case "startQuiz": startQuiz();
                break;
            default: Toast.makeText(context, "Did not recognize the JSON D:", Toast.LENGTH_LONG).show();
                break;
        }
    }
    private void startQuiz(){
        LobbyInterface.startQuiz();
    }
    private void userJoinedLobby(Context context, JSONObject obj)  {

        String name = "";
        try {
            Log.v("test", "inReceiver"+obj.getString("name").toString());
            name = obj.getString("name").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LobbyInterface.joinedLobby(name);

    }

    private void userAnswer(Context context, JSONObject obj) {
        String name = "";
        try {
            name = obj.getString("name").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.v("tag", "received answer in parsereceiver");

        QuizInterface.sendHTMLNotification(name);
    }

    private void userReady(Context context, JSONObject obj) {
        String name = "";
        try {
            name = obj.getString("name").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LobbyInterface.isReady(name);
        //Toast.makeText(context, name+" is ready!", Toast.LENGTH_LONG).show();
    }

    private void runTest(Context context) {
        Toast.makeText(context, "Received test!", Toast.LENGTH_LONG).show();

        Toast.makeText(context, "Did something cool without crashing!", Toast.LENGTH_LONG).show();
    }
}
