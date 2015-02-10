function sendNotification(){
    window.JSInterface.sendJSONNotification()
}

function userAnswer(id){
    var score = 1000-$("#progressbar").val();
    var bot = false;
    var id = "#button"+id;
    var ans = $(id).text();
    window.QuizInterface.playerAnswered(score, bot, ans);
}
function setQuestion(question){
    $("#question").text(question);
}
function setA1(a){
    $("#button0").text(a);
}
function setA2(a){
    $("#button1").text(a);
}
function setA3(a){
    $("#button2").text(a);
}
function setA4(a){
    $("#button3").text(a);
}


function notification(name){
    $("#notificationText").hide();
    $("#notificationText").text(name + " has answered!");
    $("#notificationText").fadeIn(500);
    setTimeout(function(){
    $("#notificationText").fadeOut(500)},1500);
}

$(function(){
    window.QuizInterface.getNextQuestion();
});
