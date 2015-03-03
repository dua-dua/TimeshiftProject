package com.example.dua.timeshiftproject.activites;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.example.dua.timeshiftproject.R;
import com.example.dua.timeshiftproject.interfaces.FriendInterface;

/**
 * Created by dualap on 03.03.2015.
 */
public class FriendActivity extends Activity {
    private WebView friendView;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        friendView = (WebView)findViewById(R.id.webview9);
        friendView.loadUrl("file:///android_asset/www/friends.html");
        FriendInterface friendInterface = new FriendInterface(this, friendView);
        friendView.getSettings().setJavaScriptEnabled(true);
        friendView.addJavascriptInterface(friendInterface, "friendInterface");
    }
}
