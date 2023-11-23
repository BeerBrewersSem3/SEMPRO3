var stompClient = null;
function subscribeToStatus(nodeName) {
    stompClient.subscribe('/sensor/data/' + nodeName, (message) => {
        document.getElementById(nodeName + "Label").innerText = message.body;
    })
}
function subscribeToInventory(nodeName){
    stompClient.subscribe('/sensor/data/' + nodeName, (message)=>{
        console.log("Subscribed to: " + message.body);
        let totalStockInPercentage = Math.floor((parseInt(message.body) / 35000) * 100);

        console.log("The total percantage is: " + totalStockInPercentage)
        fillSilo(totalStockInPercentage, nodeName)
    })
}

function connectWebSocket() {
    var socket = new SockJS('/wss');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function() {
        console.log("WebSocket connection established");
        subscribeToStatus("currentState");
        subscribeToStatus("temperature");
        subscribeToStatus("relativeHumidity");
        subscribeToStatus("vibration");
        subscribeToStatus("currentBatchId");
        subscribeToStatus("currentBatchAmount");
        subscribeToStatus("currentMachineSpeed");
        subscribeToStatus("prodProduced");
        subscribeToStatus("prodProcessedCount");
        subscribeToStatus("prodDefectiveCount");
        subscribeToInventory("barley");
        subscribeToInventory("malt");
        subscribeToInventory("yeast");
        subscribeToInventory("hops");
        subscribeToInventory("wheat");
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

const fill = 12;

function fillSilo(fill_level, ingredient) {
    const currSilo = document.getElementById(ingredient);
    const col = colCalculator(fill_level);
    const gradient = fill_level;
    currSilo.style.background = `linear-gradient(to top, ${col} ${gradient}%, white 10%)`;
    const placeholder_prompt = "fill_"+ingredient
    document.getElementById(placeholder_prompt).textContent = fill_level + "%";
}


function colCalculator(fill) {
    if (fill >= 50) {
        let r = 255 - ((fill - 50) * 5.1);
        const g = 255;
        const b = 0;
        return `rgb(${r}, ${g}, ${b})`;
    } else if (fill < 50 && fill > 0) {
        const r = 255;
        let g = (fill * 5.1);
        const b = 0;
        return `rgb(${r}, ${g}, ${b})`;
    } else {
        return `rgb(0, 0, 0)`;
    }

}


/*
function testDrain() {
    for (let i = 0; i < 100; i++) {
        setTimeout(() => {       fillSilo(i + 1, 'Wheat');
        }, i * 250); // i * 500 milliseconds delay
    }
}

testDrain();
*/


connectWebSocket();


