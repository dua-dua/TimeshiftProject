package com.example.dua.timeshiftproject.interfaces;

import android.app.Activity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.example.dua.timeshiftproject.activites.ChallengeActivity;

/**
 * Created by dualap on 05.03.2015.
 */
public class ChallengeInterface {
    private Activity activity;
    private WebView webView;

    public ChallengeInterface(ChallengeActivity act, WebView webView){
        this.activity = act;
        this.webView = webView;
    }
    
    @JavascriptInterface
    public void toIndex(){
        activity.finish();
    }


}
