function setScoreText(name, score, index){
    $("#player"+index+"").text(name+": "+score);
}

function setScorePlayer(score){
    $("#yourScore").text("Your score: "+score);
}

function testSetText(){
    window.FSSInterface.getTopFive();
}

function redirMenu(){
    window.FSSInterface.toMenu();
}

function clearDB(){
    window.FSSInterface.clearLobbyArray();
    $("#testButton").text("hello");
}

