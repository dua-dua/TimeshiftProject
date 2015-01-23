package com.example.dua.timeshiftproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.parse.ParseBroadcastReceiver;
import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

public class MyParseReceiver extends ParsePushBroadcastReceiver {
    @Override
    protected void onPushReceive(Context context, Intent intent){

        String name = "";

        try {
            JSONObject obj = new JSONObject(intent.getExtras().getString("com.parse.Data"));
            name = obj.getString("name").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Toast.makeText(context, "Received: "+name, Toast.LENGTH_LONG).show();
    }
}
