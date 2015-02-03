function readyClicked(){
	document.getElementById("readyText").className += "glyphicon glyphicon-ok glyphiconGreen";
    window.LobbyInterface.playerReady();
}
function printPlayers(name){
    $("#tester").append("<li>"+name+"</li>");
}
$(function(){
    window.LobbyInterface.getPlayers();
    $("#tester").text("testy");

});
