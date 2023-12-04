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
        let totalStockInPercentage = Math.floor((parseInt(message.body) / 35000) * 100);
        fillSilo(totalStockInPercentage, nodeName);
        fillStock(nodeName,message.body);
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
        subscribeToBatchStart();
    });
}

function subscribeToBatchStart(){
    stompClient.subscribe('/sensor/data/batchStart', (body) => {
        cursorDefault();
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
    cursorLoadingAnimation();
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

function fillStock(type,stock) {
    document.getElementById("stock_" + type).innerText = Math.floor(stock);
}

const brewType = document.getElementById("brewType");
const brewAmount = document.getElementById("batchAmount");
function fillRequired() {
    var selectedOption = brewType.options[brewType.selectedIndex].text;
    if (selectedOption !== brewType.options[0] && brewAmount.length !== 0) {
        getRequired(selectedOption);
    } else {
        setRequired(0,0,0,0,0,0)
    }
}
brewType.addEventListener("change",fillRequired);
brewAmount.addEventListener("input",fillRequired);

function getRequired(type) {
    const amount = document.getElementById("batchAmount").value;
    fetch("http://localhost:8080/recipe/brewTypes")
        .then(response => response.json())
        .then(jsonArray => {
            jsonArray.forEach(data => {
               if (data.name == type) {
                   setRequired(data.barley,data.hops,data.malt,data.wheat,data.yeast,amount)
               }
            });
        })
        .catch(error => console.error("Error fetching data:", error));
}

function setRequired(barley,hops,malt,wheat,yeast,amount) {
    document.getElementById("required_barley").innerText = barley * amount;
    document.getElementById("required_hops").innerText = hops * amount;
    document.getElementById("required_malt").innerText = malt * amount;
    document.getElementById("required_wheat").innerText = wheat * amount;
    document.getElementById("required_yeast").innerText = yeast * amount;
}

setRequired(0,0,0,0,0,0);
connectWebSocket();
function onPageLoad() {
    console.log("Load");
    stompClient.send("/app/sensor/data/onload", {}, {});
}


