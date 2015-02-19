package com.example.dua.timeshiftproject.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.WebView;

import com.example.dua.timeshiftproject.R;
import com.example.dua.timeshiftproject.interfaces.JavaScriptInterface;
import com.example.dua.timeshiftproject.interfaces.QuizInterface;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dualap on 12.02.2015.
 */
public class QuizActivity extends Activity{
    private WebView quizWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setBotAnswers();
        setContentView(R.layout.activity_quiz);
        quizWebView = (WebView)findViewById(R.id.webview6);
        quizWebView.getSettings().setJavaScriptEnabled(true);
        quizWebView.loadUrl("File:///android_asset/www/quiz.html");
        Intent intent = getIntent();

        boolean isMaster = intent.getBooleanExtra("isMaster", false);
        int counter = intent.getIntExtra("counter",1);

        if(isMaster==true){
            Log.v("test", "I am the master");
            Log.v("test", String.valueOf(counter)+ "the value of counter in quiz");
        }
        else{
            Log.v("test", "I am not the master");
        }
        QuizInterface quizInterface = new QuizInterface(this, quizWebView,isMaster, counter);
        quizWebView.addJavascriptInterface(quizInterface, "QuizInterface");
    }

    public void test(){
        Log.v("test", "quizTest");
    }

    public void setBotAnswers(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Scores");
        query.whereEqualTo("quizid", JavaScriptInterface.getCurrentChannel());
        Log.v("bot", JavaScriptInterface.getCurrentChannel());
        query.whereEqualTo("bot", true);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                Log.v("botTest", "everRun");
                Log.v("botTest", String.valueOf(parseObjects.size()));

                for (int a = 0; a < parseObjects.size(); a++) {
                    Log.v("botTest", "everRunFor");
                    ParseObject bot = parseObjects.get(a);
                    String name = bot.getString("userid");
                    ArrayList<Object> scores = (ArrayList<Object>) bot.getList("scores");
                    ArrayList<Integer> intScores = new ArrayList<Integer>();
                    for (int b = 0; b < scores.size(); b++) {
                        intScores.add(Integer.parseInt((String) scores.get(b)));
                    }

                    String temp = (String) scores.get(a);

                    Log.v("botTest", temp);

                }
            }
        });
    }

    public void botAnswerTimer(final String name, long time){
        Log.v("bot","started bot timer for "+name+", with time "+time);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Actions to do after 10 seconds
                QuizInterface.sendJSONNotificationForBot(name);
                Log.v("bot","ended timer for "+name);
            }
        }, time);
    }

    public static void sendJSONNotificationForBot(String name) {
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
}
