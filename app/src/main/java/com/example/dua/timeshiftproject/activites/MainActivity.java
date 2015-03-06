package com.example.dua.timeshiftproject.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.example.dua.timeshiftproject.R;
import com.example.dua.timeshiftproject.interfaces.JavaScriptInterface;

public class MainActivity extends Activity {
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWebView = (WebView)findViewById(R.id.webview1);
        //mWebView.loadUrl("file:///android_asset/www/index.html");
        JavaScriptInterface jsInterface = new JavaScriptInterface(this, mWebView);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        mWebView.getSettings().setJavaScriptEnabled(true);

        //ParseInstallation.getCurrentInstallation().saveInBackground();
    }

    // broadcast a custom intent.
    public void broadcastIntent(View view) {
        Intent intent = new Intent();
        intent.putExtra("name","jonas");
        intent.putExtra("age","24");
        intent.setAction("com.tutorialspoint.CUSTOM_INTENT");
        //intent.setAction("com.tutorialspoint.OTHER_CUSTOM_INTENT");
        Intent intent2 = new Intent();
        intent.putExtra("name","jonas");
        intent.putExtra("age","24");
        intent.setAction("com.parse.push.intent.CUSTOM_RECEIVE");
        sendBroadcast(intent2);
    }

    public void redir(){
        mWebView.loadUrl("file:///android_asset/www/index.html");
    }

    public void test(){

    }

    public void exit(){
        System.exit(0);
    }
}