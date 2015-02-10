function readyClicked(){
	document.getElementById("readyText").className += "glyphicon glyphicon-ok glyphiconGreen";
    window.LobbyInterface.playerReady();
}
function printPlayers(name){
    $("#playerList").append("<li>"+name+"<span id="+name+"></span></li>");
}
function isReady(name){
    $("#"+name).addClass("glyphicon glyphicon-ok glyphiconGreen");

}

function redirQuiz(){
    window.LoginInterface.redir("file:///android_asset/www/quiz.html")
}

$(function(){
    window.LobbyInterface.getPlayers();
    $("#tester").text("testy");

});
