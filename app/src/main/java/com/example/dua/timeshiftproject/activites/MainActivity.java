package com.example.dua.timeshiftproject.activites;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

import com.example.dua.timeshiftproject.PushFragment;
import com.example.dua.timeshiftproject.PushedFragment;
import com.example.dua.timeshiftproject.R;
import com.example.dua.timeshiftproject.interfaces.JavaScriptInterface;

public class MainActivity extends Activity {
    private WebView mWebView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mWebView = (WebView)findViewById(R.id.webview1);
        mWebView.loadUrl("file:///android_asset/www/index.html");
        JavaScriptInterface jsInterface = new JavaScriptInterface(this, mWebView);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        mWebView.getSettings().setJavaScriptEnabled(true);


        //ParseInstallation.getCurrentInstallation().saveInBackground();
    }

    // broadcast a custom intent.
    public void broadcastIntent(View view) {
        Intent intent = new Intent();
        intent.putExtra("name","jonas");
        intent.putExtra("age","24");
        intent.setAction("com.tutorialspoint.CUSTOM_INTENT");
        //intent.setAction("com.tutorialspoint.OTHER_CUSTOM_INTENT");

        Intent intent2 = new Intent();
        intent.putExtra("name","jonas");
        intent.putExtra("age","24");
        intent.setAction("com.parse.push.intent.CUSTOM_RECEIVE");

        sendBroadcast(intent2);
    }

    public void redir(){
        mWebView.loadUrl("file:///android_asset/www/index.html");
    }
    public void test(){

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void push(View view){
        PushFragment frag = new PushFragment();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.maincontainer, frag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void pushed(View view){
        PushedFragment frag = new PushedFragment();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.maincontainer, frag);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    public void exit(){
        System.exit(0);
    }



}