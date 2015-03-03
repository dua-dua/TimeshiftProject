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
            }

        });

}
function isReady(name){

    $("#"+name).addClass("glyphicon glyphicon-ok glyphiconGreen");

}

function redirQuiz(){
    window.LobbyInterface.redir()
}

$(function(){
    window.LobbyInterface.getPlayers();

});
