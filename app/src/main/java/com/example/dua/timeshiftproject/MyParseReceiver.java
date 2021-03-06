package com.example.dua.timeshiftproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.dua.timeshiftproject.activites.LobbyActivity;
import com.example.dua.timeshiftproject.activites.QuizActivity;
import com.example.dua.timeshiftproject.interfaces.ChallengeInterface;
import com.example.dua.timeshiftproject.interfaces.JavaScriptInterface;
import com.example.dua.timeshiftproject.interfaces.LobbyInterface;
import com.example.dua.timeshiftproject.interfaces.QuizInterface;
import com.example.dua.timeshiftproject.interfaces.ScoreScreenInterface;
import com.parse.GetCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;
import com.parse.ParsePushBroadcastReceiver;
import com.parse.ParseQuery;

import org.json.JSONException;
import org.json.JSONObject;

public class MyParseReceiver extends ParsePushBroadcastReceiver {


    @Override
    protected void onPushReceive(Context context, Intent intent) {
        getObjectType(context, intent);
    }

    public void getObjectType(Context context, Intent intent){
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
            case "startCountdown": startCountdown();
                break;
            case "endQuiz": endQuiz();
                break;
            case "chat": chat(context, obj);
                break;
            case "masterHasLeft": masterHasLeft(context);
                break;
            default: Toast.makeText(context, "Did not recognize the JSON D:", Toast.LENGTH_LONG).show();
                break;
        }
    }
    private void masterHasLeft(Context context){
        Toast.makeText(context, "the master has left the quiz, and so should you", Toast.LENGTH_LONG).show();
    }
    private void endQuiz(){
        Log.v("test", "in endQuiz");
    }

    private void startQuiz(){
        LobbyInterface.startQuiz();
    }

    private void userJoinedLobby(Context context, JSONObject obj)  {
        Log.v("test", "in user joined Lobby");
        String name = "";
        try {
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
        QuizInterface.sendHTMLNotification(name);
    }

    private void userReady(Context context, JSONObject obj) {
        String name = "";
        try {
            name = obj.getString("name").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String readyName = name;
        String channel = JavaScriptInterface.getCurrentChannel();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("LobbyList");
        query.whereEqualTo("lobbyId", channel);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done( ParseObject parseObject, com.parse.ParseException e) {
                parseObject.addUnique("readyPlayers", readyName);
                parseObject.saveInBackground();
            }
        });
        LobbyInterface.isReady(name);
        //Toast.makeText(context, name+" is ready!", Toast.LENGTH_LONG).show();
    }

    private void runTest(Context context) {
        Toast.makeText(context, "Received test!", Toast.LENGTH_LONG).show();
        Toast.makeText(context, "Did something cool without crashing!", Toast.LENGTH_LONG).show();
    }

    private void startCountdown(){
        LobbyInterface.startCountdown();
    }

    private void chat(Context context, JSONObject obj){
        Log.v("chatqwer", "in chat receiver");
        String name = "";
        String message = "";
        try {
            name = obj.getString("name").toString();
            message = obj.get("message").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.v("chatqwer","n: "+name+" - m: "+message);
        ScoreScreenInterface.sendChatHTML(name, message);
    }
}
