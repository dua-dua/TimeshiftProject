function getFriends(){
    window.createChallengeInterface.getFriends();
}
function printFriend(friend){
    $("#test").text("printFriend");
    $("#friendList").append("<li >"+friend+"<input id="+friend+" type='checkbox'></li>");

}