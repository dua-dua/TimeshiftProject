package com.example.dua.timeshiftproject.interfaces;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import com.example.dua.timeshiftproject.activites.FinalScoreActivity;
import com.example.dua.timeshiftproject.activites.MainActivity;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class FinalScoreScreenInterface {

    private Activity activity;
    private WebView webView;
    private static WebView webViewStatic;
    private String quizChannel;


    public FinalScoreScreenInterface(FinalScoreActivity act, WebView webView) {
        this.activity = act;
        this.webView = webView;
        webViewStatic = webView;
    }

    public void setHTMLText(String name, String score, int index) {
        webView.loadUrl("javascript:setScoreText(\"" + name + "\",\"" + score + "\",\"" + index + "\")");
    }

    public void setPlayerScore(String score) {
        webView.loadUrl("javascript:setScorePlayer(\"" + score + "\")");
    }

    @JavascriptInterface
    public void getTopFive() {
        String channel = JavaScriptInterface.getCurrentChannel();
        ParseQuery query = new ParseQuery("Scores");
        query.whereEqualTo("quizid", channel);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (parseObjects == null) {
                } else {
                    String currentUser = ParseUser.getCurrentUser().getUsername().toString();
                    int questionCounter = 0;
                    int playerIndex = 0;
                    int playerScore = 0;
                    for (int i = 0; i < parseObjects.size(); i++) {
                        if (parseObjects.get(i).getString("userid").toString().equals(currentUser)) {
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
                        if (i == playerIndex) {
                            playerScore = tempScore;
                        }
                        map.put(parseObjects.get(i).getString("userid"), tempScore);
                        tempScore = 0;
                    }
                    SortedSet<Map.Entry<String, Integer>> sortedMap = entriesSortedByValues(map);

                    int j = 1;
                    for (int i = sortedMap.size() - 1; i >= sortedMap.size() - 5; i--) { //kinda borked, fix it
                        String[] mainString = sortedMap.toArray()[i].toString().split("=");
                        String name = mainString[0];
                        String score = mainString[1];
                        setHTMLText(name, score, (j));
                        j++;
                    }
                    setPlayerScore(playerScore + "");
                }
                clearDatabase();
            }
        });
    }
    static <K, V extends Comparable<? super V>> SortedSet<Map.Entry<K, V>> entriesSortedByValues(Map<K, V> map) {
        SortedSet<Map.Entry<K, V>> sortedEntries = new TreeSet<Map.Entry<K, V>>(
                new Comparator<Map.Entry<K, V>>() {
                    @Override
                    public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
                        int res = e1.getValue().compareTo(e2.getValue());
                        return res != 0 ? res : 1; // Special fix to preserve items with equal values
                    }
                }
        );
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }

    @JavascriptInterface
    public void toMenu() {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }
    public void clearLobbyArray() {
        String channel = JavaScriptInterface.getCurrentChannel();

        ParseQuery query = new ParseQuery("LobbyList");
        final String[] empty = {};
        query.whereEqualTo("lobbyId", channel);
        query.getFirstInBackground(new GetCallback(){
            @Override

            public void done(ParseObject parseObject, ParseException e) {
                if(e != null){
                }else{
                    parseObject.put("players", Arrays.asList(empty));
                    parseObject.put("readyPlayers", Arrays.asList(empty));
                    parseObject.put("counter", 0);
                    parseObject.saveInBackground();
                }
            }
        });
    }

    public void makeBotsFromPlayers() {
        String channel = JavaScriptInterface.getCurrentChannel();
        quizChannel = channel;
        ParseQuery query = new ParseQuery("Scores");
        query.whereEqualTo("quizid", channel);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if(parseObjects == null){
                }else {
                    for (int i = 0; i < parseObjects.size(); i++) {
                        parseObjects.get(i).put("bot", true);
                        parseObjects.get(i).saveInBackground();
                    }
                }
            }
        });
    }

    @JavascriptInterface
    public void createChallenge(String challengedPlayer){
        Log.v("test", "in createChallenge");
        Log.v("test", challengedPlayer);
        Log.v("test", ParseUser.getCurrentUser().getUsername());
        Log.v("test", quizChannel);
        ParseObject challenge = new ParseObject("Challenge");
        Log.v("test", "before putting");
        challenge.put("sender", ParseUser.getCurrentUser().getUsername());
        challenge.put("receiver", challengedPlayer);
        challenge.put("quizid", quizChannel);
        Log.v("test", "after putting");
        challenge.saveInBackground();
    }

    @JavascriptInterface
    public void clearDatabase(){
        makeBotsFromPlayers();
        clearLobbyArray();
        ParsePush.unsubscribeInBackground(JavaScriptInterface.getCurrentChannel());
    }
    @JavascriptInterface
    public void endQuiz(){
        activity.finish();
    }

}
