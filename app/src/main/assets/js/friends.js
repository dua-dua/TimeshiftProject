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
    $("#friendsList").append("<li><span id="+name+">"+name+"</span><button>+</button></li>");
    /*test mer her
    list.append("<button>ButtonTest</button>");

    list.on("click", "button", function(){
        getFeed('http://open.live.bbc.co.uk/weather/feeds/en/PA2/3dayforecast.rss, showFeedResults');
    });
    */
}

function getRequests(){
    window.friendInterface.getRequests();
}

function acceptRequest(){
    $("adf").text("click");
    window.friendInterface.acceptRequest("qwer");
}

$(function(){
    getRequests();
});