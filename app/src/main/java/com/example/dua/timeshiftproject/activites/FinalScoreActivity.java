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
        setContentView(R.layout.activity_finalscore);
        scoreWebView = (WebView)findViewById(R.id.webview8);
        scoreWebView.loadUrl("file:///android_asset/www/finalscorescreen.html");
        FinalScoreScreenInterface finalScoreScreenInterface = new FinalScoreScreenInterface(this, scoreWebView);
        scoreWebView.getSettings().setJavaScriptEnabled(true);
        scoreWebView.addJavascriptInterface(finalScoreScreenInterface, "FSSInterface");


    }


}

