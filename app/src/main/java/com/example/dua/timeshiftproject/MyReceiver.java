package com.example.dua.timeshiftproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.parse.ParsePushBroadcastReceiver;


public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction()=="com.tutorialspoint.CUSTOM_INTENT"){
            Toast.makeText(context, "Intent Detected.", Toast.LENGTH_LONG).show();
            Log.v("tag","Intent received");
        }else{
            Toast.makeText(context, "Other Intent Detected.", Toast.LENGTH_LONG).show();
            Log.v("tag","Other Intent received");
        }

    }

}