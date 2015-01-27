function findLobby(){

    a = window.JSInterface.getVal();
    b = document.getElementById("code").value;
    window.document.getElementById("test").innerHTML = b;
}
function isLobby(){
    lobbyId = document.getElementById("code").value;
    window.JSInterface.isLobby(lobbyId);

}
