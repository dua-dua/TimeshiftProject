function findLobby(){

    a = window.JSInterface.getVal();
    b = document.getElementById("code").value;
    window.document.getElementById("test").innerHTML = b;
}
function isLobby(){
    lobbyId = document.getElementById("code").value;
    exists = window.QuizCodeInterface.isLobby(lobbyId);
    window.LoginInterface.redir("file:///android_asset/www/lobby.html")
    /*document.getElementById("test").innerHTML = exists;*/
}
function test(){
    document.getElementById("code").value = "test";

}
function check(names){
    document.getElementById("test").innerHTML = names;
}




