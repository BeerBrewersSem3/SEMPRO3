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
        subscribeToNode("currentState");
        subscribeToNode("temperature");
        subscribeToNode("relativeHumidity");
        subscribeToNode("vibration");
        subscribeToNode("currentBatchId");
        subscribeToNode("currentBatchAmount");
        subscribeToNode("currentMachineSpeed");
        subscribeToNode("prodProduced");
        subscribeToNode("prodProcessedCount");
        subscribeToNode("prodDefectiveCount");
    });
}
function startBatch() {
    const batchID   = document.getElementById("batchID").innerText;
    const typeID    = document.getElementById("typeID").innerText;
    const amountID  = document.getElementById("amountID").innerText;
    const speedID   = document.getElementById("speedID").innerText;

    const batchData = {
        batchID,
        typeID,
        amountID,
        speedID
    }

     stompClient.send("batch/newBatch", {}, batchData);
}


connectWebSocket();


