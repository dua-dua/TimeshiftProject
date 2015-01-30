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
function sendNotification(){
    window.JSInterface.sendJSONNotification()
}

function redir(){
    window.JSInterface.redir("file:///android_asset/www/index.html");
}

function wrongInput(){
    $("#errorText").fadeIn(1000);
    setTimeout(function(){
    $("#errorText").fadeOut(1000)},3000);
}

function notification(name){
    $("#notificationText").hide();
    $("#notificationText").text(name + " has answered!");
    $("#notificationText").fadeIn(500);
    setTimeout(function(){
    $("#notificationText").fadeOut(500)},1500);
}