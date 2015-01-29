function findLobby(){

    a = window.JSInterface.getVal();
    b = document.getElementById("code").value;
    window.document.getElementById("test").innerHTML = b;
}
function isLobby(){
    lobbyId = document.getElementById("code").value;
    exists = window.JSInterface.isLobby(lobbyId);
    document.getElementById("test").innerHTML = exists;
}
function test(){
    document.getElementById("code").value = "test";

}

