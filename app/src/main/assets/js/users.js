function createNewUser(){
    name = window.document.getElementById("newusername").value;
    password = window.document.getElementById("newpassword").value;
    window.JSInterface.createUser(name, password);
}

function logInUser(){
    user = window.document.getElementById("username").value;
    pass = window.document.getElementById("password").value;
    window.LoginInterface.logUser(user, pass);
}

function redir(){
    window.JSInterface.redir("file:///android_asset/www/index.html");
}
function wrongInput(){
    $("#errortext").fadeIn(1000);
    setTimeout(function(){
    $("#errortext").fadeOut(1000)},3000);
}
