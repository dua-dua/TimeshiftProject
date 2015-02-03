package com.example.dua.timeshiftproject;

import android.app.Activity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import java.util.List;

public class QuizInterface {

    private static WebView webViewStatic;
    private Activity activity;
    private static WebView webView;


    public QuizInterface(Activity act, WebView webView) {
        this.activity = act;
        this.webView = webView;
        webViewStatic = webView;
    }

    @JavascriptInterface
    public static void getQuestionArray(String quizcode){
        Log.v("tag","Started qetQuestionArray");
        //Find quiz with quizcode
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Quiz");
        query.whereEqualTo("code", quizcode);
        Log.v("tag","made query");
        query.getFirstInBackground(new GetCallback<ParseObject>() {

            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if(parseObject == null){
                    Log.v("tag","null");
                }else{
                    Log.v("tag","not null");
                    List<String> list = parseObject.getList("questions");
                    Log.v("tag","List:");
                    for(int i = 0; i < list.size(); i++){
                        getQuestion(list.get(i));
                    }
                }
            }
        });
    }

    @JavascriptInterface
    public static void getQuestion(String objectId){
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
                    String text = parseObject.getString("text");
                    List<String> answers = parseObject.getList("answers");
                    String correctAnswer = parseObject.getString("correctAnswer");

                    Log.v("tag","Text: "+text);
                    for( int i = 0; i<answers.size(); i++){
                        Log.v("tag","Answer"+i+" :"+answers.get(i));
                    }
                    Log.v("tag","Correct: "+correctAnswer);
                }
            }
        });
    }


}
