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