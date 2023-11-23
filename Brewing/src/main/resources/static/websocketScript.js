var stompClient = null;
function subscribeToNode(nodeName) {
    stompClient.subscribe('/sensor/data/' + nodeName, (message) => {
        document.getElementById(nodeName + "Label").innerText = message.body;
    })
}

function maintenanceCounter() {
    stompClient.subscribe('/sensor/data/maintenanceCounter', (message) => {
        let counterValue = parseInt(message.body);
        let maintenanceBar = document.getElementById("maintenanceBar");
        let maintenanceLabel = document.getElementById("maintenance");

        let fillLevel = ((counterValue / 30000) * 100)
        console.log(fillLevel.toString());

        maintenanceBar.style.width =  fillLevel + "%";
        maintenanceBar.style.background = colorCalc(fillLevel);
        maintenanceLabel.innerText = Math.round(fillLevel) + "%";
    })
}

function colorCalc(fill) {
    // Ensure the input value is within the range [0, 100]
    fill = Math.min(100, Math.max(0, fill));

    const r = Math.floor(255 * (fill <= 50 ? fill / 50 : 1));
    const g = Math.floor(255 * (fill <= 50 ? 1 : 1 - (fill - 50) / 50));
    const b = 0;

    return `rgb(${r}, ${g}, ${b})`;
}

/*
function maintenanceTrigger() {
    stompClient.subscribe('/sensor/data/maintenanceTrigger', (message) => {
        console.log("Message: " + message);
    })
}
*/



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
        maintenanceTrigger();
        maintenanceCounter();
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


