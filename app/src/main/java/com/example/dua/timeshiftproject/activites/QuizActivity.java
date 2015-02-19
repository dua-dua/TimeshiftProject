package com.example.dua.timeshiftproject.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.example.dua.timeshiftproject.R;
import com.example.dua.timeshiftproject.interfaces.JavaScriptInterface;
import com.example.dua.timeshiftproject.interfaces.QuizInterface;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

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
        int counter = intent.getIntExtra("counter",1);
        setBotAnswers(counter);

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

    public void setBotAnswers(final int count){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Scores");
        query.whereEqualTo("quizid", JavaScriptInterface.getCurrentChannel());
        Log.v("bot", JavaScriptInterface.getCurrentChannel());
        query.whereEqualTo("bot", true);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                Log.v("botTest", "everRun");
                Log.v("botTest", String.valueOf(parseObjects.size()));

                for(int a=0; a<parseObjects.size(); a++){
                    Log.v("botTest", "everRunFor");
                    ParseObject bot = parseObjects.get(a);
                    String name = bot.getString("userid");
                    ArrayList<Object> scores = (ArrayList<Object>) bot.getList("scores");
                    ArrayList<Integer> intScores = new ArrayList<Integer>();
                    for(int b=0; b<scores.size(); b++){
                        intScores.add(Integer.parseInt((String)scores.get(b)));
                        Log.v("botInt", intScores.get(b).toString());
                    }
                    Log.v("botTime", "count " + Integer.toString(count-1));
                    Log.v("botTime", Integer.toString(intScores.get(count-1)));
                    int value = calculateTimeOfScore(intScores.get(count-1));
                    


                }
            }
        });
    }
    public int calculateTimeOfScore(int score){
        int time = (int)((1000-score)*22.5);

        Log.v("botTime", Integer.toString(time));
        return time;

    }
}
