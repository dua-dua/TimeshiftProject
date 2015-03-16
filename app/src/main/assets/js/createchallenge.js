function getFriends(){
    window.createChallengeInterface.getFriends();
}

function challengeFriends(){
    $("input[type=checkbox]").each(function(){
        if(this.checked){
            window.createChallengeInterface.sendChallenges(this.id);
        }
    });
    challengeNotify();
}

function printFriend(friend){
    $("#friendList").append("<li >"+friend+"<input id="+friend+" type='checkbox'></li>");
}

function challengeNotify(){
    $("#challengeSent").text("Challenges sent!");
    $("#challengeSent").fadeOut(2000);
}