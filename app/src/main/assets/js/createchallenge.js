function getFriends(){
    window.createChallengeInterface.getFriends();
}
function challengeFriends(){

    $("input[type=checkbox]").each(function(){
        if(this.checked){
            window.createChallengeInterface.sendChallenges(this.id);
         }
    });
}
function printFriend(friend){
    $("#test").text("printFriend");
    $("#friendList").append("<li >"+friend+"<input id="+friend+" type='checkbox'></li>");


}