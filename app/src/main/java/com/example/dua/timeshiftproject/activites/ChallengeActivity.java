package com.example.dua.timeshiftproject.activites;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.example.dua.timeshiftproject.R;
import com.example.dua.timeshiftproject.interfaces.ChallengeInterface;

/**
 * Created by dualap on 05.03.2015.
 */
public class ChallengeActivity extends Activity {
    private WebView challengeView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);
        challengeView = (WebView)findViewById(R.id.webview10);
        challengeView.loadUrl("file:///android_asset/www/challenges.html");
        ChallengeInterface challengeInterface = new ChallengeInterface(this, challengeView);
        challengeView.getSettings().setJavaScriptEnabled(true);
        challengeView.addJavascriptInterface(challengeInterface, "challengeInterface");
    }

}
