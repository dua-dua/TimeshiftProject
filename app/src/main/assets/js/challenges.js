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