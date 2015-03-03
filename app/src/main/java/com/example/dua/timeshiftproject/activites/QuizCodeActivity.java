package com.example.dua.timeshiftproject.activites;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.example.dua.timeshiftproject.R;
import com.example.dua.timeshiftproject.interfaces.QuizCodeInterface;

/**
 * Created by dualap on 12.02.2015.
 */
public class QuizCodeActivity extends Activity {
    private WebView quizCodeWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizcode);
        quizCodeWebView = (WebView)findViewById(R.id.webview4);
        quizCodeWebView.loadUrl("file:///android_asset/www/quizcode.html");

        QuizCodeInterface quizCodeInterface = new QuizCodeInterface(this, quizCodeWebView);
        quizCodeWebView.getSettings().setJavaScriptEnabled(true);
        quizCodeWebView.addJavascriptInterface(quizCodeInterface, "QuizCodeInterface");
    }
}
