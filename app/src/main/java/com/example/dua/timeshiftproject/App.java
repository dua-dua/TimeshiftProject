package com.example.dua.timeshiftproject;

import android.app.Application;

import com.example.dua.timeshiftproject.activites.MainActivity;
import com.parse.Parse;
import com.parse.PushService;

/**
 * Created by dualap on 10.02.2015.
 */
public class App extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        Parse.enableLocalDatastore(getApplicationContext());
        Parse.initialize(this, "0qwu1NjJN6Omb7C6JhpAML7ltY2y1dYG2dp6O92L", "RYc9OPFFWIMiorIGFa2Sh2xvLCqwleS7QZNzTZFI");
        PushService.setDefaultPushCallback(this, MainActivity.class);
    }
}
