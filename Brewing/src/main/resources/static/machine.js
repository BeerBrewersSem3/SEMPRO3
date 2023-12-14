function emergencyStopMachine(){
    pauseMachine();
}
function pauseMachine(){
    stompClient.send("/app/machine/pause", {}, {});
    document.getElementById("pauseBtn").innerText = "Continue";
}

function continueBatch() {
    stompClient.send("/app/machine/continue", {}, {});
    document.getElementById("pauseBtn").innerText = "Pause";
}

let isPaused = false;

function toggleSwitchPauseStart() {
    cursorLoadingAnimation();
    if (isPaused) {
        continueBatch();
    } else {
        pauseMachine();
    }
    isPaused = !isPaused;
}