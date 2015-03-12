function setScoreText(name, score, index){
    $("#player"+index+"").text(name+": "+score);
}

function setScorePlayer(score){
    $("#yourScore").text("Your score: "+score);
}
function challengeFriend(){
    var friendName = $("#challengedName").val();
    $("#yourScore").text("friend added");
    window.FSSInterface.createChallenge(friendName);
}

function getScores(){
    window.FSSInterface.getTopFive();
}

function redirMenu(){
    window.FSSInterface.toMenu();
}
function endQuiz(){
    window.FSSInterface.endQuiz();

}
function toCreateChallenge(){
    window.FSSInterface.toCreateChallenge();
}



