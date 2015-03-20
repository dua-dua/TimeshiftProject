package com.example.dua.timeshiftproject.activites;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
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


        setContentView(R.layout.activity_quiz);
        quizWebView = (WebView)findViewById(R.id.webview6);
        quizWebView.getSettings().setJavaScriptEnabled(true);
        quizWebView.loadUrl("File:///android_asset/www/quiz.html");
        Intent intent = getIntent();
        boolean isMaster = intent.getBooleanExtra("isMaster", false);
        Log.v("master", "am I the master here in quizactivity? "+ isMaster);
        int counter = intent.getIntExtra("counter",1);
        Log.v("counter", "the value of count in QuizActivity " + String.valueOf(counter));


        if(isMaster==true){
            setBotAnswers(counter);
        }

        QuizInterface quizInterface = new QuizInterface(this, quizWebView,isMaster, counter);
        quizWebView.addJavascriptInterface(quizInterface, "QuizInterface");

    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        quizWebView.loadUrl("javascript:stopTimeOut()");
    }

    public void setBotAnswers(final int count){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Scores");
        query.whereEqualTo("quizid", JavaScriptInterface.getCurrentChannel());
        query.whereEqualTo("bot", true);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {

                for (int a = 0; a < parseObjects.size(); a++) {
                    ParseObject bot = parseObjects.get(a);
                    String name = bot.getString("userid");
                    ArrayList<Object> scores = (ArrayList<Object>) bot.getList("scores");
                    ArrayList<Integer> intScores = new ArrayList<Integer>();

                    for(int b=0; b<scores.size(); b++){
                        intScores.add(Integer.parseInt((String)scores.get(b)));
                    }
                    int value = calculateTimeOfScore(intScores.get(count-1));
                    botAnswerTimer(name, value);
                }
            }
        });
    }


    public void botAnswerTimer(final String name, long time){
        final String channel = JavaScriptInterface.getCurrentChannel();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                sendJSONNotificationForBot(name, channel);
            }
        }, time);
    }

    public static void sendJSONNotificationForBot( String name, String channel) {

        JSONObject data = null;

        try {
            data = new JSONObject();
            data.put("type", "userAnswer");
            data.put("name", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ParsePush push = new ParsePush();
        push.setChannel(channel);
        push.setData(data);
        push.sendInBackground();
    }

    public int calculateTimeOfScore(int score){
        int time = (int)((1000-score)*22.5);
        return time;
    }
}
