package com.example.dua.timeshiftproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONObject;


public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(context, "Intent Detected.", Toast.LENGTH_LONG).show();
        String name = "";
        String val = "";

        name = intent.getStringExtra("name");
        val = intent.getStringExtra("age");

        Toast.makeText(context, name+val, Toast.LENGTH_LONG).show();
        Log.v("tag",name);
        Log.v("tag",val);

        //String val = inData.getString("value");
        //Log.v("tag","Received: "+"val");
        /*Bundle inData = intent.getExtras();
            if(inData.getString("com.parse.Data") == null){
                Log.v("tag","null");
            }else{
                Log.v("tag","not null");
            }
            String dataString = inData.getString("com.parse.Data");
            Toast.makeText(context, dataString, Toast.LENGTH_LONG).show();
            */
    }

}