var stompClient = null;
function subscribeToNode(nodeName) {
    stompClient.subscribe('/sensor/data/' + nodeName, (message) => {
        document.getElementById(nodeName + "Label").innerText = message.body;
    })
}
function connectWebSocket() {
    var socket = new SockJS('/wss');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function() {
        subscribeToNode("temperature");
        subscribeToNode("relativeHumidity");
        subscribeToNode("vibration");
        subscribeToNode("currentBatchId");
        subscribeToNode("currentBatchAmount");
        subscribeToNode("currentMachineSpeed");
        subscribeToNode("prodProduced");
        subscribeToNode("prodProcessedCount");
        subscribeToNode("prodDefectiveCount");
        subscribeToNotification("currentState");
    });
}

function subscribeToNotification(nodeName){
    stompClient.subscribe('/sensor/notification/' + nodeName, (message) => {
        console.log(message);
        const sirenImage = document.getElementsByClassName("siren");
        sirenImage.src = "thermometer.png";
    })
}


connectWebSocket();


