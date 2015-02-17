package com.example.dua.timeshiftproject.interfaces;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.example.dua.timeshiftproject.activites.FinalScoreActivity;
import com.example.dua.timeshiftproject.activites.QuizActivity;
import com.example.dua.timeshiftproject.activites.ScoreActivity;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class QuizInterface {

    private static WebView webViewStatic;
    private QuizActivity activity;
    private static WebView webView;
    private static List<String> list;
    private static boolean hasAnswered = false;
    private boolean isMaster=false;
    private static int counter;



    public QuizInterface(QuizActivity act, WebView webView, boolean master, int counter) {
        this.activity = act;
        this.webView = webView;
        webViewStatic = webView;
        isMaster = master;
        this.counter = counter;
        Log.v("test", "the value of counter in QInterface " + String.valueOf(counter));
    }

    @JavascriptInterface
    public static void getQuestionArray(String quizcode, final int count){
        Log.v("tag","Started qetQuestionArray");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Quiz");
        query.whereEqualTo("code", quizcode);
        Log.v("tag","made query ");
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (parseObject == null) {

                } else {

                    list = parseObject.getList("questions");
                    getQuestion(list.get(count-1).toString());

                }
            }
        });
    }

    @JavascriptInterface
    public static void getQuestion(String objectId){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Question");
        query.whereEqualTo("objectId", objectId);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(final ParseObject parseObject, com.parse.ParseException e) {
                if (parseObject == null) {
                    Log.v("tag", "No matching question with that code");
                } else {
                    Log.v("tag", "Found question");

                    String text = parseObject.getString("text");

                    List<String> answers = parseObject.getList("answers");
                    String correctAnswer = parseObject.getString("correctAnswer");
                    setText(text, answers.get(0), answers.get(1), answers.get(2), answers.get(3), correctAnswer);

                    Log.v("tag", "Text: " + text);
                    for (int i = 0; i < answers.size(); i++) {
                        Log.v("tag", "Answer " + i + ": " + answers.get(i));
                    }
                    Log.v("tag", "Correct: " + correctAnswer);
                }
            }
        });
    }

    @JavascriptInterface
    public static void playerAnswered(final int numScore, final boolean isBot, final String answer){
        Log.v("tag","Entered playerAnswered with answer "+answer);
        hasAnswered=true;
        final String channel = JavaScriptInterface.getCurrentChannel();
        final String user = ParseUser.getCurrentUser().getUsername();
        sendJSONNotification();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Scores");
        query.whereEqualTo("quizid", channel);
        query.whereEqualTo("userid", user);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(final ParseObject parseObject, com.parse.ParseException e) {
                //Check if answer is correct or not, set numScore = 0 or use it as is

                //If null, make new score
                if (parseObject == null) {
                    Log.v("tag", "No matching score, creating a new one");
                    String[] scores = {Integer.toString(numScore)};
                    ParseObject score = new ParseObject("Scores");
                    score.put("bot", isBot);
                    score.put("quizid", channel);
                    score.put("scores", Arrays.asList(scores));
                    score.put("totalScore", numScore);
                    score.put("userid", user);
                    score.saveInBackground();
                    Log.v("tag","Done creating new score");

                //If exists, append score and update total score
                } else {
                    Log.v("tag","Found score, appending and updating");
                    List scoreArray = parseObject.getList("scores");
                    scoreArray.add(Integer.toString(numScore));
                    int score = parseObject.getInt("totalScore")+numScore;

                    parseObject.put("scores",scoreArray);
                    parseObject.put("totalScore", score);
                    parseObject.saveInBackground();
                    Log.v("tag", "Updated score");
                }
            }
        });
    }

    @JavascriptInterface
    public static void sendHTMLNotification(final String name) {

        Log.v("tag", "Entered sendHTMLNotification");
        webViewStatic.post(new Runnable() {
            @Override
            public void run() {
                webViewStatic.loadUrl("javascript:notification(\""+name+"\")");
                Log.v("tag", "Completed sendHTMLNotification");
            }
        });
    }

    @JavascriptInterface
    public static void sendJSONNotification() {
        ParseUser pUser = ParseUser.getCurrentUser();
        String name = "";

        name = pUser.getUsername();
        String channel = JavaScriptInterface.getCurrentChannel();
        JSONObject data = null;

        try {
            data = new JSONObject();
            data.put("type","userAnswer");
            data.put("name",name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ParsePush push = new ParsePush();
        push.setChannel(channel);
        push.setData(data);
        push.sendInBackground();
        Log.v("tag", "Sent JSON from sendJSONNotification");
    }

    @JavascriptInterface
    public static void setText(final String question, final String a1, final String a2, final String a3, final String a4, final String correct){

        webViewStatic.post(new Runnable() {
            @Override
            public void run() {
                webViewStatic.loadUrl("javascript:setQuestion(\"" + question + "\")");
                webViewStatic.loadUrl("javascript:setA1(\"" + a1 + "\")");
                webViewStatic.loadUrl("javascript:setA2(\"" + a2 + "\")");
                webViewStatic.loadUrl("javascript:setA3(\"" + a3 + "\")");
                webViewStatic.loadUrl("javascript:setA4(\"" + a4 + "\")");
                Log.v("tag","correct = "+correct);
                webViewStatic.loadUrl("javascript:setAnswerText(\""+correct+"\")");
                Log.v("tag", "text set");
            }
        });
    }

    @JavascriptInterface
    public static void getNextQuestion(){
        Log.v("tag","start getnextq");

        final String channel = JavaScriptInterface.getCurrentChannel();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("LobbyList");
        query.whereEqualTo("lobbyId", channel);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, com.parse.ParseException e) {
                if (parseObject == null) {
                    Log.v("tag", "No matching lobby. This should NEVER happen");

                } else {
                    Log.v("tag", "Found lobby, getting count");
                    getQuestionArray(channel, counter);
                    Log.v("counter","hentet quiz");
                    Log.v("counter","master: "+parseObject.getString("master"));
                    Log.v("counter","current user: "+ParseUser.getCurrentUser().getUsername());
                    Log.v("counter","current user string: "+ParseUser.getCurrentUser().getUsername().toString());
                    if (parseObject.getString("master").equals(ParseUser.getCurrentUser().getUsername())) {
                        parseObject.increment("counter");
                        Log.v("counter","++sd");
                        parseObject.saveInBackground();
                    }
                }
            }
        });
    }

    @JavascriptInterface
    public void toScore(){
        /*activity.test();
        Log.v("test", "toScore");
        if(hasAnswered==false){
            playerAnswered(0, false, null);
        }
        Intent intent = new Intent(activity, ScoreActivity.class);
        if(isMaster==true){
            intent.putExtra("isMaster", true);
        }

        activity.startActivity(intent);*/

        moreQuestions(counter);
    }

    @JavascriptInterface
    public void toFinalScore(){
        activity.test();
        Log.v("test", "toFinalScore");
        if(hasAnswered==false){
            playerAnswered(0, false, null);
        }
        Intent intent = new Intent(activity, FinalScoreActivity.class);
        activity.startActivity(intent);
    }

    @JavascriptInterface
    public void moreQuestions(final int count){ //finne en måte å hente ut count på
        final String channel = JavaScriptInterface.getCurrentChannel();
        String counter = String.valueOf(count);
        Log.v("test", "the count " + counter);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Quiz");
        query.whereEqualTo("code", channel);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                Log.v("test", "the number of questions "+String.valueOf(parseObject.getList("questions").size()));
                if(count == parseObject.getList("questions").size()){

                   toFinalScore();
                }else{
                    if(hasAnswered==false){
                        playerAnswered(0, false, null);
                    }
                    Intent intent = new Intent(activity, ScoreActivity.class);

                    if(isMaster==true){
                        intent.putExtra("isMaster", true);
                        intent.putExtra("counter", count);
                    }

                    activity.startActivity(intent);
                }
            }
       });
    }
}
