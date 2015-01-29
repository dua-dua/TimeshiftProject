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