var stompClient = null;
/*stompClient.onConnect =  (frame) => {

    console.log('Connected: ' + frame);
}
var socket = new SockJS('/wss');

stompClient = Stomp.over(socket);

*/
function connectWebSocket() {
    var socket = new SockJS('/wss');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function() {
        stompClient.subscribe('/sensor/data/stateCurrent', (message) => {
            document.getElementById("currentStateLabel").innerText = message.body;
        })
    });
}
connectWebSocket();


