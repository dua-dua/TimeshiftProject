package com.example.dua.timeshiftproject;

import android.app.Activity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import com.parse.LogInCallback;
import android.webkit.WebView;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.GetCallback;
import com.parse.ParseQuery;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
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
    public String getTime(){
        Date date = new Date();
        return String.valueOf(date.getTime());
    }
    @JavascriptInterface
    public void doSomething() {
        JSONObject data = null;

        try {
            data = new JSONObject();
            data.put("type","test");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ParsePush push = new ParsePush();
        push.setChannel("test");
        push.setData(data);
        push.sendInBackground();
        Log.v("tag", "Sent JSON from doSomething");
    }

    @JavascriptInterface
    public String getVal() {
        return "string";
    }

    @JavascriptInterface
    public void createUser(String name, String password) {
        Log.v("tag", "Entered create user");
        ParseUser user = new ParseUser();
        user.setUsername(name);
        user.setPassword(password);
        user.signUpInBackground();
        Log.v("tag", "created user: " + name + ", " + password);
    }

    @JavascriptInterface
    public static String getCurrentChannel(){
        ParseInstallation inst = ParseInstallation.getCurrentInstallation();
        List channels = inst.getList("channels");
        return channels.get(0).toString();
    }
}
