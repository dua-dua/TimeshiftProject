function startSomething(){
    window.document.getElementById("knapp").innerHTML = "yoyo";
    window.JSInterface.doSomething("hei");
}

function testFunc(){
    console.log("hei");
    window.QuizInterface.getQuestionArray("test");
    window.QuizInterface.getQuestionFromLocal(0);
}

function redirQuizcode(){
    window.LoginInterface.redir("file:///android_asset/www/quizcode.html")
}