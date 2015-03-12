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

$(function(){
    countdown();
});



