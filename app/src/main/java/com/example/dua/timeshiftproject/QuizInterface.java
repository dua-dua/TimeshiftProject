package com.example.dua.timeshiftproject;

import android.app.Activity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
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
        final String QUIZ = "quiz";
        Log.v("tag","Started qetQuestionArray");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Quiz");
        query.whereEqualTo("code", quizcode);
        Log.v("tag","made query");
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(final List<ParseObject> list, ParseException e) {
                if (list == null) {
                    Log.v("tag", "null");
                } else {
                    Log.v("tag", "not null");
                    //final List<String> list = parseObject.getList("questions");
                    Log.v("tag", "List:");
                    for (int i = 0; i < list.size(); i++) {
                        getQuestion(list.get(i).getObjectId());
                    }
                    ParseObject.unpinAllInBackground(QUIZ, list, new DeleteCallback() {
                        public void done(ParseException e) {
                            if (e != null) {
                                Log.v("tag", "error in local");
                                return;
                            }

                            // Add the latest results for this query to the cache.
                            ParseObject.pinAllInBackground(QUIZ, list);
                        }
                    });

                }
            }
        });
    }

    @JavascriptInterface
    public static void getQuestion(String objectId){
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

    @JavascriptInterface
    public static void getQuestionFromLocal(final int i){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Question");
        query.fromLocalDatastore();
        //query.whereEqualTo("objectId", objectId);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(final List<ParseObject> list, ParseException e) {
                if (e == null) {
                    // Results were successfully found from the local datastore.
                    Log.v("tag", "no result in local");
                    List l = list.get(i).getList("questions");

                    Log.v("tag","got questions from local: "+l);
                } else {
                    Log.v("tag","inner error");
                }
            }
        });
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

    @JavascriptInterface
    public static void playerAnswered(final int numScore, final boolean isBot, final String answer){
        Log.v("tag","Entered playerAnswered with answer "+answer);

        final String channel = JavaScriptInterface.getCurrentChannel();
        final String user = ParseUser.getCurrentUser().getUsername();
        sendJSONNotification();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Scores");
        query.whereEqualTo("quizid", channel);
        query.whereEqualTo("userid", user);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(final ParseObject parseObject, com.parse.ParseException e) {
                //Check if answer is correct or not, set numScore = 0 or use it as is

                //If null, make new score
                if (parseObject == null) {
                    Log.v("tag", "No matching score, creating a new one");
                    String[] scores = {Integer.toString(numScore)};
                    ParseObject score = new ParseObject("Scores");
                    score.put("bot", isBot);
                    score.put("quizid", channel);
                    score.put("scores", Arrays.asList(scores));
                    score.put("totalScore", numScore);
                    score.put("userid", user);
                    score.saveInBackground();
                    Log.v("tag","Done creating new score");
                //If exists, append score and update total score
                } else {
                    Log.v("tag","Found score, appending and updating");
                    List scoreArray = parseObject.getList("scores");
                    scoreArray.add(Integer.toString(numScore));
                    int score = parseObject.getInt("totalScore")+numScore;

                    parseObject.put("scores",scoreArray);
                    parseObject.put("totalScore", score);
                    parseObject.saveInBackground();
                    Log.v("tag", "Updated score");
                }
            }
        });
    }

    @JavascriptInterface
    public static void sendHTMLNotification(final String name) {
        final String user = name;

        Log.v("tag", "Entered sendHTMLNotification");
        webViewStatic.post(new Runnable() {
            @Override
            public void run() {
                webViewStatic.loadUrl("javascript:notification(\""+user+"\")");
                Log.v("tag", "Completed sendHTMLNotification");
            }
        });
    }

    @JavascriptInterface
    public static void sendJSONNotification() {
        ParseUser pUser = ParseUser.getCurrentUser();
        String name = "";

        name = pUser.getUsername();
        JSONObject data = null;

        try {
            data = new JSONObject();
            data.put("type","userAnswer");
            data.put("name",name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ParsePush push = new ParsePush();
        push.setChannel("channel");
        push.setData(data);
        push.sendInBackground();
        Log.v("tag", "Sent JSON from sendJSONNotification");
    }
}
