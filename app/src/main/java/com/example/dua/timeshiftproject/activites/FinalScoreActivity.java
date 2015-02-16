package com.example.dua.timeshiftproject.activites;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import com.example.dua.timeshiftproject.R;
import com.example.dua.timeshiftproject.interfaces.FinalScoreScreenInterface;


public class FinalScoreActivity extends Activity {
    private WebView scoreWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        scoreWebView = (WebView)findViewById(R.id.webview7);
        scoreWebView.loadUrl("file:///android_asset/www/finalscorescreen.html");
        scoreWebView.getSettings().setJavaScriptEnabled(true);
        FinalScoreScreenInterface finalScoreScreenInterface = new FinalScoreScreenInterface(this, scoreWebView);
        scoreWebView.addJavascriptInterface(finalScoreScreenInterface, "FSSInterface");
    }
}

