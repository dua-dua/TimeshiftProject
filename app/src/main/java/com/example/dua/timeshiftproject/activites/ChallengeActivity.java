package com.example.dua.timeshiftproject.activites;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.dua.timeshiftproject.R;
import com.example.dua.timeshiftproject.interfaces.ChallengeInterface;
import com.example.dua.timeshiftproject.interfaces.LobbyInterface;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by dualap on 05.03.2015.
 */
public class ChallengeActivity extends Activity {
    private WebView challengeView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);
        challengeView = (WebView)findViewById(R.id.webview10);
        challengeView.loadUrl("file:///android_asset/www/challenges.html");
        ChallengeInterface challengeInterface = new ChallengeInterface(this, challengeView);
        challengeView.getSettings().setJavaScriptEnabled(true);
        challengeView.addJavascriptInterface(challengeInterface, "challengeInterface");


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Challenge");
        query.whereEqualTo("receiver", "a");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> challenges, ParseException e) {
                if (challenges.size() == 0) {
                    Log.v("challengeTest", "empty");
                    Log.v("challengeTest", ParseUser.getCurrentUser().getUsername());
                }
                final int challengesSize= challenges.size();
                for (int a = 0; a < challenges.size(); a++) {
                    final int aVal=a;
                    final String id = challenges.get(a).getObjectId();
                    final String sender = challenges.get(a).getString("sender");
                    Log.v("challengeTest", challenges.get(a).toString());
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            //challengeView.loadUrl("javascript:test()");

                            challengeView.loadUrl("javascript:printChallenges(\"" + sender + "\",\"" + id+ "\")");
                            if(aVal==challengesSize-1) {
                                challengeView.loadUrl("javascript:setOnclickForId()");
                            }
                        }
                    }, 500);
                }




                }

        });
    }


}
