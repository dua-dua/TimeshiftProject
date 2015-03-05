function toMenu(){
    window.friendInterface.toIndex();
}

function countRequests(){
    var size = $("#friendsList li").size()
    if(size == 0){
        $("#numOfRequests").text("No new friend requests");
    }
}

function countChallenges(){
    var size = $("#challengeList li").size()
    if(size == 0){
        $("#numOfChallenges").text("No new challenges");
    }
}

$(
    countRequests();
    countChallenges();
)