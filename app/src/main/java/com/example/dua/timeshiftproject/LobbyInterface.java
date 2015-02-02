package com.example.dua.timeshiftproject;

import android.app.Activity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseUser;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;


public class LobbyInterface {
    private static WebView webViewStatic;
    private Activity activity;
    private WebView webView;


    public LobbyInterface(Activity act, WebView webView) {
        this.activity = act;
        this.webView = webView;
        webViewStatic = webView;
    }

    @JavascriptInterface
    public void playerReady() {
        ParseUser pUser = ParseUser.getCurrentUser();
        String name = "";
        String channel = "";

        name = pUser.getUsername();
        JSONObject data = null;
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        List channelList = installation.getList("channels");
        channel = channelList.get(0).toString();

        try {
            data = new JSONObject();
            data.put("type","userReady");
            data.put("name",name);
            data.put("channel",channel);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ParsePush push = new ParsePush();
        push.setChannel(channel);
        push.setData(data);
        push.sendInBackground();

        Log.v("tag", "User "+name+" is ready in channel "+channel);
    }
}
