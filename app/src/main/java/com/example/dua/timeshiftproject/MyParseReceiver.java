package com.example.dua.timeshiftproject;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.parse.ParseBroadcastReceiver;
import com.parse.ParsePushBroadcastReceiver;

public class MyParseReceiver extends ParsePushBroadcastReceiver {
    @Override
    protected void onPushReceive(Context context, Intent intent){
        Toast.makeText(context, "Custom ParsePush Detected.", Toast.LENGTH_LONG).show();
        
    }
}
