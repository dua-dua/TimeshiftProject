
function test(){
    $("#title").text("test");
}
function printChallenges(name, id){
    $("#challengeList").append("<span id="+id+" ><li >"+name+ "  quiz:  "+id+"</li></span>")



}
function getId(){
    var id = this;
    window.challengeInterface.test(id);
}
function setOnclickForId(){
    $("span").on('click', function () {
       var id = $(this).attr('id');
       window.challengeInterface.toChallengeLobby(id);
       window.challengeInterface.test(id);
    });

}
