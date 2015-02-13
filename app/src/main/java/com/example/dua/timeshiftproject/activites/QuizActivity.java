package com.example.dua.timeshiftproject.activites;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.example.dua.timeshiftproject.R;
import com.example.dua.timeshiftproject.interfaces.QuizInterface;

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
        QuizInterface quizInterface = new QuizInterface(this, quizWebView);
        quizWebView.addJavascriptInterface(quizInterface, "QuizInterface");


    }
    public void test(){
        Log.v("test", "quizTest");
    }

}
