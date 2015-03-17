/* chat */
$(function(){
    $('#chatbox').scrollbox({
        linear: true,
        autoPlay: false
    });
    $('#title').text("running");
});

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

function test(){
    $("#title").text("test");
}

function printChallenges(name, id){
    $("#challengeList").append('<li id="'+id+'"><span>Challenger: '+name+' </span><button id="accept'+id+'" class="btn btn-default adButton" onClick="acceptChallenge(this.id)"><span class="glyphicon glyphicon-ok glyphiconGreen"></span></button><button id="decline'+id+'" class="btn btn-default adButton" onClick="declineChallenge(this.id)"><span class="glyphicon glyphicon-remove glyphiconRed"></span></button></li>');
}

function noChallengesText(){
    $("#challengeList").append('<li>No challenges :(</li>');
}

function getId(){
    var id = this;
    window.challengeInterface.test(id);
}

function acceptChallenge(input){
    var quizId = input.replace("accept","");
    window.challengeInterface.toChallengeLobby(quizId);
    $("#"+quizId).remove();
}

function declineChallenge(input){
    var quizId = input.replace("decline","");
    window.challengeInterface.removeChallenge(quizId);
    $("#"+quizId).remove();
}