$(function(){
    getRequests();
});

function getRequests(){
    window.friendInterface.getRequests();
    window.friendInterface.getFriendsList();
}

function countRequests(){
    var size = $("#friendsList li").size()
    if(size == 0){
        $("#numOfRequests").text("No new friend requests");
    }
}

function addFriend(){
    var receiver = "";
    receiver = $("#friendName").val();
    window.friendInterface.sendRequest(receiver);
    $("#addNotify").text("Friend added");
    $("#addNotify").fadeOut(2000);
}

function addToLi(name){
    /* DO NOT TOUCH THE LINE BELOW. YOU WILL BE SHOT! */
    $('#friendsList').append('<li id="li'+name+'"><span class="bigLiText">'+name+' </span><button class="btn btn-default adButton" id="'+name+'" onClick="acceptRequest(this.id)"><span class="glyphicon glyphicon-ok glyphiconGreen"></span></button><button class="btn btn-default adButton" id="decline'+name+'" onClick="declineRequest(this.id)"><span class="glyphicon glyphicon-remove glyphiconRed"></span></button></li>');
}

function acceptRequest(name){
    window.friendInterface.acceptRequest(name);
    var id = "#li"+name
    $(id).remove();
}

function declineRequest(input){
    var name = input.replace("decline","");
    window.friendInterface.removeRequest(name);
    var id = "#li"+name
    $(id).remove();
}

function addToFriendList(name){
    $('#friendsListReal').append('<li><span class="bigLiText">'+name+' </span></li>');
}