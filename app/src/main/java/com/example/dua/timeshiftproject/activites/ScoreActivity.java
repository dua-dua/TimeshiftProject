package com.example.dua.timeshiftproject.activites;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.example.dua.timeshiftproject.R;

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

    }
}
