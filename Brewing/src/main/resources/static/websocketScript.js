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
        console.log("WebSocket connection established");
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
    const batchID   = document.getElementById("batchID").value;
    const batchType    = document.getElementById("batchType").value;
    const batchAmount  = document.getElementById("batchAmount").value;
    const batchSpeed   = document.getElementById("batchSpeed").value;

    const batchData = {
        batchID,
        batchType,
        batchAmount,
        batchSpeed
    }

    if (stompClient) {
        stompClient.send("/app/batch/newBatch", {}, JSON.stringify(batchData));
        console.log(batchData);
    } else {
        console.error("WebSocket connection not yet established");
    }
     //stompClient.send("batch/newBatch", {}, JSON.stringify(batchData));
     //console.log(batchData)
}
connectWebSocket();


