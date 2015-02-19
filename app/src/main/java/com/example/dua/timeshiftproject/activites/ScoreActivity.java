package com.example.dua.timeshiftproject.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;

import android.os.Handler;
import android.util.Log;
import android.webkit.JavascriptInterface;

import android.webkit.WebView;

import com.example.dua.timeshiftproject.R;
import com.example.dua.timeshiftproject.interfaces.ScoreScreenInterface;

import java.util.concurrent.TimeUnit;

/**
 * Created by dualap on 13.02.2015.
 */
public class ScoreActivity extends Activity {
    private WebView scoreWebView;
    private int counter;
    private boolean isMaster;
    private Handler handler;
    private Runnable runnable = new Runnable() {
        public void run() {
            Log.v("timer", "ended timer");
            toNextQuestion();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        scoreWebView = (WebView)findViewById(R.id.webview7);
        scoreWebView.loadUrl("file:///android_asset/www/scorescreen.html");
        scoreWebView.getSettings().setJavaScriptEnabled(true);
        ScoreScreenInterface scoreScreenInterface = new ScoreScreenInterface(this, scoreWebView);
        scoreWebView.addJavascriptInterface(scoreScreenInterface, "SSInterface");

        Intent intent = getIntent();
        isMaster = intent.getBooleanExtra("isMaster", false);
        counter = intent.getIntExtra("counter", 1);
        if(isMaster==true){
            Log.v("test", "master in scorescreen");
            Log.v("test", "the value of counter " +String.valueOf(counter));
            counter ++;

        }
        timer();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    public void timer(){
        Log.v("timer","started timer");
        handler = new Handler();
        handler.postDelayed(runnable,10000);
    }

    public void toNextQuestion(){
        Intent intent = new Intent(this, QuizActivity.class);
        if(isMaster){
            intent.putExtra("counter", counter);
            intent.putExtra("isMaster", true);
        }

        this.startActivity(intent);
        this.finish();

    }
}
