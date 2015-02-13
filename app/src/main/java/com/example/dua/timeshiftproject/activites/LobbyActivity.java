package com.example.dua.timeshiftproject.activites;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.example.dua.timeshiftproject.R;
import com.example.dua.timeshiftproject.interfaces.LobbyInterface;

/**
 * Created by dualap on 12.02.2015.
 */
public class LobbyActivity extends Activity {
    private WebView lobbyWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.v("test", "in lobbyAct");
        setContentView(R.layout.activity_lobby);
        lobbyWebView = (WebView)findViewById(R.id.webview5);
        Log.v("test", "thisFar0");
        lobbyWebView.getSettings().setJavaScriptEnabled(true);
        Log.v("test", "thisFar1");

        Log.v("test", "thisFar2");
        lobbyWebView.loadUrl("file:///android_asset/www/lobby.html");

        LobbyInterface lobbyInterface = new LobbyInterface(this, lobbyWebView);
        lobbyWebView.addJavascriptInterface(lobbyInterface, "LobbyInterface");

    }
}
