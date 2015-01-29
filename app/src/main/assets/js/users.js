function createUser(){
    name = window.document.getElementById("newusername").value;
    password = window.document.getElementById("newpassword").value;
    window.JSInterface.createUser(name, password);
}

function logUser(){
    namae = window.document.getElementById("username").value;
    passworda = window.document.getElementById("password").value;
    window.JSInterface.logUser(namea, passworda);
    }

function redir(){
    window.JSInterface.redir();
}
function yoyo(){
    document.getElementById("derp").innerHTML = "yoyo";
}
