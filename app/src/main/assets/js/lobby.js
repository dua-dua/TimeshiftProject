function readyClicked(){
	document.getElementById("readyText").className += "glyphicon glyphicon-ok glyphiconGreen";
    window.LobbyInterface.playerReady();
}
function printPlayers(name){
    addToList(name);
}
function addToList(name){
        var addit = true;
        $("#playerList").each(function(){
            if(name == $(this).text()){
                addit= false;
            }
            if(addit){
                $("#playerList").append("<li>"+name+"<span id="+name+"></span></li>");
                enableReadyButton();
            }
        });
}

function isReady(name){
    $("#"+name).addClass("glyphicon glyphicon-ok glyphiconGreen");
}

function redirQuiz(){
    window.LobbyInterface.redir()
}

function countdown(){
    count(2000, "Starting in 3");
    count(3000, "Starting in 2");
    count(4000, "Starting in 1");
}

function count(wait, string){
    setTimeout(function(){
        $("#countdown").text(string);
    },wait);
}

function enableReadyButton(){
    if($("#playerList li").size() < 3){
        $("#readyButton").hide();
    }else{
        $("#readyButton").show();
    }
}

$(function(){
    window.LobbyInterface.getPlayers();
});
