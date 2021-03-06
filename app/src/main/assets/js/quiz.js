var hasAnswered = false;

function sendNotification(){
    window.JSInterface.sendJSONNotification()
}

function userAnswer(id){
    var score = 0;
    var bot = false;
    var id = "#button"+id;
    var ans = $(id).text();
    var correct = $("#correct").text();
    if(!hasAnswered){
        if(ans == correct){
            score = 1000-$("#progressbar").val();
        }else{
            score = 0;
        }
        window.QuizInterface.playerAnswered(score, bot, ans);
        hasAnswered = true;
    }
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

function setAnswerText(ans){
    $("#correct").text(ans);
}

function notification(name){
    $("#notificationText").text(name + " has answered!");
    var sound = document.getElementById("notificationSound");
    sound.play();
    /*$("#notificationText").fadeTo(500, 1);
    setTimeout(function(){
    $("#notificationText").fadeTo(500, 0 )},1500);*/
}

function progressbar(){
	$("#progressbar").show();
	answer = false;
	clearInterval(animate);
	var progressbar = $('#progressbar'),
	max = progressbar.attr('max'),
	time = (1000/max)*20,
	value = 0;

	var loading = function() {
		value += 1;
		var val = progressbar.val(value);

		$('.progress-value').html(value + '%');

		if (value == max) {
			clearInterval(animate);
	        $('.progress-value').html(max + '%');
	    }
	};
	var animate = setInterval(function() {
		loading();
	}, time);
}

function toScore(){
    QuizInterface.toScore();
}

var timeOut;

function stopTimeOut(){
    window.clearTimeout(timeOut);
}

function zeroScore(){
    if(!hasAnswered){
        var score = 0;
        var bot = false;
        var ans = "zero";
        window.QuizInterface.playerAnswered(score, bot, ans);
        hasAnswered = true;
    }
}

$(function(){
    window.QuizInterface.getNextQuestion();
    progressbar();
    setTimeout(function(){zeroScore();}, 22000);
    timeOut = setTimeout(toScore, 22500);
});
