function setScoreText(name, score, index){
    $("#player"+index+"").text(name+": "+score);
}

function setScorePlayer(score){
    $("#yourScore").text("Your score: "+score);
}

function testSetText(){
    window.SSInterface.getTopFive();
}

function redirMenu(){
    window.SSInterface.toMenu();
}

