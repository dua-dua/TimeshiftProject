package com.example.dua.timeshiftproject.interfaces;

import android.webkit.WebView;

import com.example.dua.timeshiftproject.activites.FriendActivity;

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

}
