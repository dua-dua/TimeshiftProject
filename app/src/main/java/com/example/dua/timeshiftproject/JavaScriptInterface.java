package com.example.dua.timeshiftproject;

import android.app.Activity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import com.parse.LogInCallback;
import android.webkit.WebView;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.GetCallback;
import com.parse.ParseQuery;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;


/**
 * Created by dualap on 26.01.2015.
 */
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
    public void doSomething(String userName) {
        JSONObject data = null;

        try {
            data = new JSONObject();
            data.put("type","test");
            data.put("name","jonas");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ParsePush push = new ParsePush();
        push.setChannel("channel");
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
    public void sendJSONNotification() {
        ParseUser pUser = ParseUser.getCurrentUser();
        String name = "";

        name = pUser.getUsername();
        JSONObject data = null;

        try {
            data = new JSONObject();
            data.put("type","userAnswer");
            data.put("name",name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ParsePush push = new ParsePush();
        push.setChannel("channel");
        push.setData(data);
        push.sendInBackground();
        Log.v("tag", "Sent JSON from sendJSONNotification");
    }

    @JavascriptInterface
    public static void sendHTMLNotification(final String name) {
        final String user = name;

        Log.v("tag", "Entered sendHTMLNotification");
        webViewStatic.post(new Runnable() {
            @Override
            public void run() {
                webViewStatic.loadUrl("javascript:notification(\""+user+"\")");
                Log.v("tag", "Completed sendHTMLNotification");
            }
        });
    }
}
