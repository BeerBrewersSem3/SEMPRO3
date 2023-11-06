var stompClient = null;

function connectWebSocket() {
    var socket = new SockJS('/wss');
    stompClient = Stomp.over(socket);

}
function subscribeToNode(nodeName) {
    stompClient.subscribe('/sensor/data/' + nodeName, function(message) {
        document.getElementById(nodeName.toUpperCase()).innerText = message.body;
    });
}
connectWebSocket();
stompClient.connect({}, function() {
    subscribeToNode("currentState");
    subscribeToNode("cntrlCmd");
    subscribeToNode("machSpeedRead");
    subscribeToNode("prodDefectiveCount");
    subscribeToNode("prodProcessedCount");
    subscribeToNode("currentBatchId");
    subscribeToNode("batchQty");
    subscribeToNode("relHumidity");
    subscribeToNode("temperature");
    subscribeToNode("vibration");
    subscribeToNode("machSpeed");
    stompClient.subscribe('/command/response', function(message) {
        console.log('Response from server: ' + message.body);
    });
});

function sendCommand() {
    var number = document.getElementById('inputNumber').value;
    stompClient.send("/app/command/sendCommand", {}, JSON.stringify({ number: number }));
}

function executeCommand() {
    stompClient.send("/app/command/executeCommand", {}, {});
}
/*
function sendCommand() {
    var number = document.getElementById('inputNumber').value;
    fetch('/sendCommand', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ number: number })
    })
        .then(response => response.text())
        .then(data => {
            console.log('Response from sendData:', data);
        })
        .catch(error => {
            console.error('Error sending data:', error);
        });
}

function executeCommand() {
    fetch('/executeCommand', {
        method: 'POST'
    })
        .then(response => response.text())
        .then(data => {
            console.log('Response from setBooleanTrue:', data);
        })
        .catch(error => {
            console.error('Error setting boolean:', error);
        });
}

 */