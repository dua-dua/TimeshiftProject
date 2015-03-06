package com.example.dua.timeshiftproject.interfaces;

import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.example.dua.timeshiftproject.activites.FriendActivity;
import com.example.dua.timeshiftproject.activites.MenuActivity;
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
import java.util.List;

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
    public void sendRequest(final String receiver){
        //Send request
        final String current = ParseUser.getCurrentUser().getUsername().toString();
        ParseObject friendRequest = new ParseObject("FriendRequest");
        friendRequest.put("sender", current);
        friendRequest.put("receiver", receiver);
        friendRequest.saveInBackground();
        addToMyList(receiver);
    }

    public void addToMyList(String name){
        Log.v("friendreq", "entered addtomylist with "+name);
        //Add friend to my list
        List friendArray = ParseUser.getCurrentUser().getList("friends");
        if(friendArray == null){
            String[] friends = {name};
            ParseUser.getCurrentUser().put("friends", Arrays.asList(friends));
            ParseUser.getCurrentUser().saveInBackground();
        }else{
            friendArray.add(name);
            ParseUser.getCurrentUser().put("friends", friendArray);
            ParseUser.getCurrentUser().saveInBackground();
        }
        Log.v("friendreq", "finished addtomylist");
    }

    @JavascriptInterface
    public void removeRequest(String name){
        Log.v("friendreq", "entered removerequest with "+name);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("FriendRequest");
        String current = ParseUser.getCurrentUser().getUsername();
        query.whereEqualTo("sender", name);
        query.whereEqualTo("receiver", current);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                parseObject.deleteInBackground();
            }
        });
        Log.v("friendreq", "finished removerequest");
    }

    @JavascriptInterface
    public void getRequests(){
        String current = ParseUser.getCurrentUser().getUsername().toString();
        Log.v("friendreq", "inside getRequest");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("FriendRequest");
        query.whereEqualTo("receiver", current);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if(parseObjects == null){
                    Log.v("friendreq", "no friend requests");
                }else {
                    for (int i = 0; i < parseObjects.size(); i++) {
                        addToHTMLList(parseObjects.get(i).getString("sender"));
                    }
                }
            }
        });
    }

    @JavascriptInterface
    public void acceptRequest(String name){
        Log.v("friendreq", "entered accept with "+name);
        addToMyList(name);
        removeRequest(name);
    }

    @JavascriptInterface
    public void toIndex(){
        Intent intent = new Intent(activity, MenuActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    @JavascriptInterface
    public void addToHTMLList(String name){
        Log.v("friendreq", "added "+name);
        webView.loadUrl("javascript:addToLi(\""+name+"\")");
    }

}
