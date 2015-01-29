function createNewUser(){
    name = window.document.getElementById("newusername").value;
    password = window.document.getElementById("newpassword").value;
    window.JSInterface.createUser(name, password);
}

function logInUser(){
    user = window.document.getElementById("username").value;
    pass = window.document.getElementById("password").value;
    window.JSInterface.logUser(user, pass);
}

function redir(){
    window.JSInterface.redir();
}
function yoyo(){
    document.getElementById("derp").innerHTML = "yoyo";
}
