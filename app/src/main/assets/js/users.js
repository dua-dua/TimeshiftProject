function createNewUser(){
    name = window.document.getElementById("newusername").value;
    password = window.document.getElementById("newpassword").value;
    window.NewUserInterface.createUser(name, password);
    window.NewUserInterface.returnToLogin();
}

function logInUser(){
    user = window.document.getElementById("username").value;
    pass = window.document.getElementById("password").value;
    window.LoginInterface.logUser(user, pass);
}

function toNewUser(){
    window.LoginInterface.toNewUser();
}

function wrongInput(){
    $("#errorText").fadeIn(1000);
    setTimeout(function(){
    $("#errorText").fadeOut(1000)},3000);
}

