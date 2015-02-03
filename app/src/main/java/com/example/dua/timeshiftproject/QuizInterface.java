package com.example.dua.timeshiftproject;

import android.app.Activity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import java.util.List;

public class QuizInterface {

    private Activity activity;
    private WebView webView;


    public QuizInterface(Activity act, WebView webView) {
        this.activity = act;
        this.webView = webView;
    }

    @JavascriptInterface
    public static String[] getQuestionArray(String quizcode){
        Log.v("tag","Started qetQuestionArray");
        final String[] quizIds = {};
        //Find quiz with quizcode
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Quiz");
        query.whereEqualTo("code", quizcode);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(final ParseObject parseObject, com.parse.ParseException e) {
                if (parseObject == null) {
                    Log.v("tag", "No matching quiz with that code");
                } else {
                    Log.v("tag", "Found quiz");
                    List<String> list = parseObject.getList("questions");

                    for(int i = 0; i < list.size(); i++){
                        quizIds[i] = list.get(i);
                    }
                }
            }
        });
        Log.v("tag","getQuestionArray: "+quizIds[1]+quizIds[2]+quizIds[3]);
        return quizIds;
    }

    @JavascriptInterface
    public ParseObject getQuestion(String objectId){
        final ParseObject[] obj = {};
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Question");
        query.whereEqualTo("objectId", objectId);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(final ParseObject parseObject, com.parse.ParseException e) {
                if (parseObject == null) {
                    Log.v("tag", "No matching question with that code");
                } else {
                    Log.v("tag", "Found question");
                    obj[0] = parseObject;

                }
            }
        });
        Log.v("tag","Question text: "+obj[0].getString("text"));
        return obj[0];
    }




}
