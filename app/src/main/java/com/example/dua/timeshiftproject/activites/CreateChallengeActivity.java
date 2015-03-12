package com.example.dua.timeshiftproject.activites;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.example.dua.timeshiftproject.R;
import com.example.dua.timeshiftproject.interfaces.CreateChallengeInterface;

/**
 * Created by dualap on 12.03.2015.
 */
public class CreateChallengeActivity extends Activity{
    private WebView createChallengeView;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createchallenge);
        createChallengeView = (WebView)findViewById(R.id.webview11);
        createChallengeView.loadUrl("file:///android_asset/www/createchallenge.html");
        CreateChallengeInterface createChallengeInterface = new CreateChallengeInterface(this, createChallengeView);
        createChallengeView.getSettings().setJavaScriptEnabled(true);
        createChallengeView.addJavascriptInterface(createChallengeInterface, "createChallengeInterface");

    }
}
