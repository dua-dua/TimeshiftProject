package com.example.dua.timeshiftproject;

import android.app.Activity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
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
    private static List<String> list;



    public QuizInterface(Activity act, WebView webView) {
        this.activity = act;
        this.webView = webView;
        webViewStatic = webView;
    }

    @JavascriptInterface
    public static void getQuestionArray(String quizcode, final int count){
        Log.v("tag","Started qetQuestionArray");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Quiz");
        query.whereEqualTo("code", quizcode);
        Log.v("tag","made query");
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (parseObject == null) {
                    Log.v("tag", "null");
                } else {
                    Log.v("tag", "not null");
                    list = parseObject.getList("questions");
                    getQuestion(list.get(count).toString());
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
                    setText(text, answers.get(0), answers.get(1), answers.get(2), answers.get(3));

                    Log.v("tag", "Text: " + text);
                    for (int i = 0; i < answers.size(); i++) {
                        Log.v("tag", "Answer " + i + ": " + answers.get(i));
                    }
                    Log.v("tag", "Correct: " + correctAnswer);
                }
            }
        });
    }



    @JavascriptInterface
    public static void getQuizAndSaveLocal(String quizcode){

        //Hent quiz basert på quizcode
        //Hente alle questions fra quiz
        //Lage parseObject for hvert question i local storage

        final String QUIZ_LABEL = "quiz";
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Quiz");
        query.whereEqualTo("code", quizcode);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(final List<ParseObject> list, ParseException e) {
                if (e != null) {
                    // There was an error or the network wasn't available.
                    return;

                }

                // Release any objects previously pinned for this query.
                ParseObject.unpinAllInBackground(QUIZ_LABEL, list, new DeleteCallback() {
                    public void done(ParseException e) {
                        if (e != null) {
                            // There was some error.
                            return;
                        }

                        // Lagre quizObject i en liste
                        //ParseObject.pinAllInBackground(QUIZ_LABEL, list);
                        List<String> questionList = list.get(0).getList("questions");
                        Log.v("tag","saved in local");
                        for(int i = 0; i<questionList.size(); i++){
                            Log.v("tag",questionList.get(i).toString());
                        }
                    }
                });
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
        String channel = JavaScriptInterface.getCurrentChannel();
        JSONObject data = null;

        try {
            data = new JSONObject();
            data.put("type","userAnswer");
            data.put("name",name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ParsePush push = new ParsePush();
        push.setChannel(channel);
        push.setData(data);
        push.sendInBackground();
        Log.v("tag", "Sent JSON from sendJSONNotification");
    }

    @JavascriptInterface
    public static void setText(final String question, final String a1, final String a2, final String a3, final String a4){

        webViewStatic.post(new Runnable() {
            @Override
            public void run() {
                webViewStatic.loadUrl("javascript:setQuestion(\"" + question + "\")");
                webViewStatic.loadUrl("javascript:setA1(\"" + a1 + "\")");
                webViewStatic.loadUrl("javascript:setA2(\"" + a2 + "\")");
                webViewStatic.loadUrl("javascript:setA3(\"" + a3 + "\")");
                webViewStatic.loadUrl("javascript:setA4(\"" + a4 + "\")");
                Log.v("tag", "text set");
            }
        });
    }

    @JavascriptInterface
    public void getNextQuestion(){
        Log.v("tag","start getnextq");
        final String channel = JavaScriptInterface.getCurrentChannel();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("LobbyList");
        query.whereEqualTo("lobbyId", channel);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, com.parse.ParseException e) {
                if (parseObject == null) {
                    Log.v("tag", "No matching lobby. This should NEVER happen");

                } else {
                    Log.v("tag","Found lobby, getting count");
                    getQuestionArray(channel, parseObject.getInt("counter"));
                    if(parseObject.getString("master") == ParseUser.getCurrentUser().toString()){
                        parseObject.increment("counter");
                        parseObject.saveInBackground();
                    }
                }
            }
        });
    }
}
