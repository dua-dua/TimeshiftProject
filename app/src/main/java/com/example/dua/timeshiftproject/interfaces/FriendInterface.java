package com.example.dua.timeshiftproject.interfaces;

import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.example.dua.timeshiftproject.activites.FriendActivity;
import com.example.dua.timeshiftproject.activites.MenuActivity;

/**
 * Created by dualap on 03.03.2015.
 */
public class FriendInterface {

    private WebView webView;
    private FriendActivity activity;

    public FriendInterface(FriendActivity act, WebView webView){
        this.activity = act;
        this.webView = webView;
    }

    @JavascriptInterface
    public void toIndex(){
        Intent intent = new Intent(activity, MenuActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

}
