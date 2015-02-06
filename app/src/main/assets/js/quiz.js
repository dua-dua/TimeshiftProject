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


function notification(name){
    $("#notificationText").hide();
    $("#notificationText").text(name + " has answered!");
    $("#notificationText").fadeIn(500);
    setTimeout(function(){
    $("#notificationText").fadeOut(500)},1500);
}