function toMenu(){
    window.challengeInterface.toIndex();
}

function countChallenges(){
    var size = $("#challengeList li").size()
    if(size == 0){
        $("#numOfChallenges").text("No new challenges");
    }
}

$(
    countChallenges();
)