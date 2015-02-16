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
        boolean isMaster = intent.getBooleanExtra("isMaster", false);
        if(isMaster==true){
            Log.v("test", "master in scorescreen");
        }

        timer();
    }

    public void timer(){
        Log.v("timer","started timer");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Actions to do after 10 seconds
                Log.v("timer","ended timer");
                toNextQuestion();
            }
        }, 10000);
    }

    public void toNextQuestion(){
        Intent intent = new Intent(this, QuizActivity.class);
        this.startActivity(intent);

    }
}
