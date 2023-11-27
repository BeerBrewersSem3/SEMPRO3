var stompClient = null;

function subscribeToNotification(nodeName){
    stompClient.subscribe('/sensor/data/' + nodeName, (callback) => {
        var sirenImage = document.getElementsByClassName("siren");
        sirenImage.src = "new-image.jpg";
    })
}

function connectWebSocket() {
    var socket = new SockJS('/wss');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function() {
        subscribeToNode("currentState");
        subscribeToNode("temperature");
    });
}
connectWebSocket();