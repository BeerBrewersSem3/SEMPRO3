var stompClient = null;
function subscribeToStatus(nodeName) {
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


function subscribeToInventory(nodeName){
    stompClient.subscribe('/sensor/data/' + nodeName, (message)=>{
        //console.log("Subscribed to: " + message.body);
        let totalStockInPercentage = Math.floor((parseInt(message.body) / 35000) * 100);

        //console.log("The total percantage is: " + totalStockInPercentage)
        fillSilo(totalStockInPercentage, nodeName)
    })
}

function connectWebSocket() {
    var socket = new SockJS('/wss');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function() {
        console.log("WebSocket connection established");
        maintenanceCounter();
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
        onPageLoad();
    });
}

function startBatch() {
    const batchID   = document.getElementById("batchID").value;
    const brewType    = convertBrewType()
    const batchAmount  = document.getElementById("batchAmount").value;
    const batchSpeed   = document.getElementById("batchSpeed").value;

    const batchData = {
        batchID,
        brewType,
        batchAmount,
        batchSpeed
    }

    if (stompClient) {
        stompClient.send("/app/batch/newBatch", {}, JSON.stringify(batchData));
        console.log(batchData);
    } else {
        console.error("WebSocket connection not yet established");
    }
    toggleNewBatch();
}
function emergencyStopMachine(){
    pauseMachine();
}
function pauseMachine(){
    stompClient.send("/app/machine/pause", {}, {});
    document.getElementById("pauseBtn").innerText = "Continue";
}

function continueBatch() {
    stompClient.send("/app/machine/continue", {}, {});
    document.getElementById("pauseBtn").innerText = "Pause";
}

let isPaused = false;

function toggleSwitchPauseStart() {
    if (isPaused) {
        continueBatch();
    } else {
        pauseMachine();
    }
    isPaused = !isPaused;
}

function toggleNewBatch() {
    document.getElementById("newBatch").classList.toggle("active");
}
function convertBrewType() {
    const batchType    = document.getElementById("brewType").value;
    switch(batchType) {
        case "Pilsner": return 0;
        case "Wheat": return 1;
        case "IPA": return 2;
        case "Stout": return 3;
        case "Ale": return 4;
        case "Alcohol Free": return 5
    }
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


connectWebSocket();
function onPageLoad() {
    console.log("Load");
    stompClient.send("/app/sensor/data/onload", {}, {});
}


