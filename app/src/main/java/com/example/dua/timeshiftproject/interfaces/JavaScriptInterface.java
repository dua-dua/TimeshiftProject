package com.example.dua.timeshiftproject.interfaces;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.example.dua.timeshiftproject.activites.LoginActivity;
import com.example.dua.timeshiftproject.activites.QuizCodeActivity;

import android.webkit.WebView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class JavaScriptInterface {
    private static android.webkit.WebView webViewStatic;
    private Activity activity;
    private WebView webView;


    public JavaScriptInterface(Activity act, WebView webView) {
        this.activity = act;
        this.webView = webView;
        webViewStatic = webView;
    }

    @JavascriptInterface
    public String getTime() {
        Date date = new Date();
        return String.valueOf(date.getTime());
    }

    @JavascriptInterface
    public void doSomething() {
        JSONObject data = null;

        try {
            data = new JSONObject();
            data.put("type", "test");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ParsePush push = new ParsePush();
        push.setChannel("test");
        push.setData(data);
        push.sendInBackground();
    }

    @JavascriptInterface
    public String getVal() {
        return "string";
    }

    @JavascriptInterface
    public void createUser(String name, String password) {
        ParseUser user = new ParseUser();
        user.setUsername(name);
        user.setPassword(password);
        user.signUpInBackground();
    }

    @JavascriptInterface
    public static String getCurrentChannel() {
        ParseInstallation inst = ParseInstallation.getCurrentInstallation();
        List channels = inst.getList("channels");
        return channels.get(0).toString();
    }

    @JavascriptInterface
    public void toQuizCode() {
        Intent intent = new Intent(activity, QuizCodeActivity.class);
        activity.startActivity(intent);
    }

    @JavascriptInterface
    public void exit() {
        activity.finish();
        System.exit(0);
    }
    @JavascriptInterface
    public void unsubscribe(){
        ParsePush.unsubscribeInBackground("q");
        ParsePush.unsubscribeInBackground("test");
    }
    @JavascriptInterface
    public void toLogin(){
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.finish();
        ParseUser.logOut();
    }
}
