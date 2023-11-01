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
});

/*stompClient.connect({}, function() {
    stompClient.subscribe('/sensor/data/currentState', function(message) {
        document.getElementById("CURRENT_STATE").innerText = message.body;
    });
    stompClient.subscribe('/sensor/data/CntrlCmd', function(message) {
        document.getElementById("CNTRL_CMD").innerText = message.body;
    });
});

 */

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