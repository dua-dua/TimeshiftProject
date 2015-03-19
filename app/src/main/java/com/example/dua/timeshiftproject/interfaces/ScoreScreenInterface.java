package com.example.dua.timeshiftproject.interfaces;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.example.dua.timeshiftproject.activites.MainActivity;
import com.example.dua.timeshiftproject.activites.QuizCodeActivity;
import com.example.dua.timeshiftproject.activites.ScoreActivity;
import com.example.dua.timeshiftproject.interfaces.JavaScriptInterface;
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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class ScoreScreenInterface {

    private ScoreActivity activity;
    private WebView webView;
    private static WebView webViewStatic;


    public ScoreScreenInterface(ScoreActivity act, WebView webView) {
        this.activity = act;
        this.webView = webView;
        webViewStatic = webView;
    }

    public void setHTMLText(String name, String score, int index){
        webView.loadUrl("javascript:setScoreText(\""+name+"\",\""+score+"\",\""+index+"\")");
    }

    public void setPlayerScore(String score){
        webView.loadUrl("javascript:setScorePlayer(\""+score+"\")");
    }

    @JavascriptInterface
    public void getTopFive(){
        String channel = JavaScriptInterface.getCurrentChannel();
        ParseQuery query = new ParseQuery("Scores");
        query.whereEqualTo("quizid", channel);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (parseObjects == null) {
                    Log.v("score", "null");
                } else {
                    String currentUser = ParseUser.getCurrentUser().getUsername().toString();
                    int questionCounter = 0;
                    int playerIndex = 0;
                    int playerScore = 0;
                    for(int i = 0; i<parseObjects.size(); i++){
                        if(parseObjects.get(i).getString("userid").toString().equals(currentUser)){
                            questionCounter = parseObjects.get(i).getList("scores").size();
                            playerIndex = i;
                        }
                    }

                    TreeMap<String, Integer> map = new TreeMap<String, Integer>();
                    int tempScore = 0;
                    for (int i = 0; i < parseObjects.size(); i++) {
                        for (int j = 0; j < questionCounter; j++) {
                            tempScore += Integer.parseInt(parseObjects.get(i).getList("scores").get(j).toString());
                        }
                        if(i == playerIndex){
                            playerScore = tempScore;
                        }
                        map.put(parseObjects.get(i).getString("userid"), tempScore);
                        tempScore = 0;
                    }
                    SortedSet<Map.Entry<String, Integer>> sortedMap = entriesSortedByValues(map);

                    int j = 1;
                    for (int i = sortedMap.size()-1; i >= sortedMap.size()-5; i--) { //kinda borked, fix it
                        String[] mainString = sortedMap.toArray()[i].toString().split("=");
                        String name = mainString[0];
                        String score = mainString[1];
                        setHTMLText(name, score, (j));
                        j++;
                    }
                    setPlayerScore(playerScore+"");
                }
            }
        });
    }

    static <K,V extends Comparable<? super V>> SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
        SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
                new Comparator<Map.Entry<K,V>>() {
                    @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
                        int res = e1.getValue().compareTo(e2.getValue());
                        return res != 0 ? res : 1; // Special fix to preserve items with equal values
                    }
                }
        );
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }

    //Chat functionality
    @JavascriptInterface
    public void sendChatJSON(String message){
        Log.v("chatqwer","in JSON with "+message);
        ParseUser pUser = ParseUser.getCurrentUser();
        String name = "";

        name = pUser.getUsername();
        String channel = JavaScriptInterface.getCurrentChannel();
        JSONObject data = null;

        try {
            data = new JSONObject();
            data.put("type","chat");
            data.put("name", name);
            data.put("message", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ParsePush push = new ParsePush();
        push.setChannel(channel);
        push.setData(data);
        push.sendInBackground();
        Log.v("chatqwer","done in JSON as "+name);

        saveEmoteInDatabase(message);
    }

    public void sendChatJSONforBot(String message, String name, String channel){
        Log.v("chatqwer","in JSON with "+message);
        JSONObject data = null;

        try {
            data = new JSONObject();
            data.put("type","chat");
            data.put("name", name);
            data.put("message", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ParsePush push = new ParsePush();
        push.setChannel(channel);
        push.setData(data);
        push.sendInBackground();
        Log.v("chatqwer","done in JSON as the bot "+name);
    }

    public void saveEmoteInDatabase(final String message){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Scores");
        Log.v("emotetag", "Entered saveEmote");
        String channel = JavaScriptInterface.getCurrentChannel();
        query.whereEqualTo("quizid", channel);
        query.whereEqualTo("userid", ParseUser.getCurrentUser().getUsername());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                //If null, make new emotearray
                Log.v("emotetag", "done with the call");
                if (parseObject.getList("emotes") == null) {
                    Log.v("emotetag", "No matching emotearray, creating a new one");
                    String[] emotes = {message};
                    parseObject.put("emotes", Arrays.asList(emotes));
                    parseObject.saveInBackground();
                    Log.v("emotetag","Done creating new emotearray");
                    //If exists, append score and update total score
                } else {
                    //List scoreArray = parseObject.getList("scores");
                    Log.v("emotetag", "Found list, appending "+message);
                    List emoteArray = parseObject.getList("emotes");
                    emoteArray.add(message);
                    parseObject.put("emotes",emoteArray);
                    parseObject.saveInBackground();
                    Log.v("emotetag", "Appended "+message);
                }
            }
        });
    }

    public void sendEmotesAsMaster(){
        final String channel = JavaScriptInterface.getCurrentChannel();
        ParseQuery query = new ParseQuery("Scores");
        query.whereEqualTo("quizid", channel);
        query.whereEqualTo("bot", true);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if(parseObjects == null){
                    Log.v("emotetag", "found nothing in sendEmoteAsMaster");
                }else {
                    for (int i = 0; i < parseObjects.size(); i++) {
                        final String name = parseObjects.get(i).getString("userid");
                        List emoteList = parseObjects.get(i).getList("emotes");
                        Log.v("emotetag","counter is "+activity.getCounter());
                        final String emote = emoteList.get(activity.getCounter()-2).toString();
                        double rollDice = Math.random();
                        Log.v("emotetag","dice rolled "+rollDice);
                        if(rollDice > 0){
                            Log.v("emotetag","sending json for bot "+name+" with emote "+emote);
                            long time = (long)(Math.random()*2000) + 500;
                            Log.v("emotetag","sending JSON for bot in "+time);
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    sendChatJSONforBot(emote, name, channel);
                                }
                            }, time);
                        }
                    }
                }
            }
        });
    }

    public static void sendChatHTML(final String name, final String message){
        Log.v("chatqwer","in html with n: "+name+" - m: "+message);
        webViewStatic.post(new Runnable() {
            @Override
            public void run() {
                //staticWebView.loadUrl("javascript:chatHTML("+name+","+message+")");
                webViewStatic.loadUrl("javascript:chatHTML(\""+ name +"\",\""+ message +"\")");
                Log.v("chatqwer","done in html");
            }
        });
    }
    //End chat functionality
}
