function setScoreText(name, score, index){
    $("#player"+index+"").text(name+": "+score);
}

function setScorePlayer(score){
    $("#yourScore").text("Your score: "+score);
}

function getScores(){
    window.SSInterface.getTopFive();
}

function redirMenu(){
    window.SSInterface.toMenu();
}

function countdown(){
    count(5000, "Next question in 5");
    count(6000, "Next question in 4");
    count(7000, "Next question in 3");
    count(8000, "Next question in 2");
    count(9000, "Next question in 1");
}

function count(wait, string){
    setTimeout(function(){
        $("#timer").text(string);
    },wait);
}

/* chat */

var count = 0;

function nextLine(){
    count++;
    $("#chat").append("<li>"+count+"</li>");
    var len = $("ul#chat li").length
    if(len == 5){
        console.log("removing");
        setTimeout(function() {
            $("#chat li:nth-last-child(2)").remove();},1);
            len = $("ul#chat li").length
    }

    $('#nextButton').click(function () {
        if(len > 3){
            setTimeout(function() {
                $('#chatbox').trigger('forward');},250);
        }
        console.log("len is: " + len);

    });
    return false;
}

function sendMessage(string){
    window.challengeInterface.sendChatJSON(string);
    $('#title').text(string);
}

function chatHTML(name, message){
    $("#chat").append("<li><p>"+name+": "+message+"</p></li>");
    var len = $("ul#chat li").length
    if(len == 5){
       console.log("removing");
       setTimeout(function() {
           $("#chat li:nth-last-child(2)").remove();},50);
           len = $("ul#chat li").length
    }

    if(len > 3){
       setTimeout(function() {
           $('#chatbox').trigger('forward');},250);
    }
    return false;
}

/* end chat */

$(function(){
    countdown();
    $('#chatbox').scrollbox({
            linear: true,
            autoPlay: false
    });
});



