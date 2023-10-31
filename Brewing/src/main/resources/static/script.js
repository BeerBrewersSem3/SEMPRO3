var stompClient = null;

function connectWebSocket() {
    var socket = new SockJS('/wss');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function() {
        stompClient.subscribe('/sensor/data/currentState', function(message) {
            document.querySelector("p").innerText = message.body;
        });
    });
}

connectWebSocket();
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