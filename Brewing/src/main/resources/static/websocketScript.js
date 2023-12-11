var stompClient = null;
function subscribeToStatus(nodeName, unit) {
    stompClient.subscribe('/sensor/data/' + nodeName, (message) => {
        document.getElementById(nodeName + "Label").innerText = message.body + " " + unit;
    })
}


function maintenanceCounter() {
    stompClient.subscribe('/sensor/data/maintenanceCounter', (message) => {
        let counterValue = parseInt(message.body);
        let maintenanceBar = document.getElementById("maintenanceBar");
        let maintenanceLabel = document.getElementById("maintenance");

        let fillLevel = ((counterValue / 30000) * 100)

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
        subscribeToStatus("temperature", "°C");
        subscribeToStatus("relativeHumidity", "%");
        subscribeToStatus("vibration", "DPS");
        subscribeToStatus("currentBatchId","");
        subscribeToStatus("currentBatchAmount", "bottles");
        subscribeToStatus("currentMachineSpeed", "bottles/s");
        subscribeToStatus("prodProduced", "bottles");
        subscribeToStatus("prodProcessedCount", "bottles");
        subscribeToStatus("prodDefectiveCount","bottles");
        subscribeToInventory("barley");
        subscribeToInventory("malt");
        subscribeToInventory("yeast");
        subscribeToInventory("hops");
        subscribeToInventory("wheat");
        subscribeToNotification("currentState");
        subscribeToNotification("temperature");
        checkFilling("fillingInventory");
        subscribeToNotification("barley");
        subscribeToNotification("wheat");
        subscribeToNotification("malt");
        subscribeToNotification("yeast");
        subscribeToNotification("hops");
        onPageLoad();
        subscribeToBatchStart();
        subscribeToConsoleMessages();
    });
}

function subscribeToBatchStart(){
    stompClient.subscribe('/sensor/data/batchStart', (body) => {
        cursorDefault();
    });
}

function startBatch() {
    clearConsole();
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
    if (selectedOption !== "Choose type" && brewAmount.length !== 0) {
        getRequired(selectedOption);
    } else {
        setRequired(0,0,0,0,0,0);
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
                   setRequired(data.barley,data.hops,data.malt,data.wheat,data.yeast,amount);
                   setMachSpeed(data.minMachSpeed, data.maxMachSpeed,((data.maxMachSpeed-data.minMachSpeed)/2));
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

function setMachSpeed(minMachSpeed, maxMachSpeed, recommendedMachSpeed){
    //let batchSpeedInput = document.getElementById("batchSpeed");
    //batchSpeedInput.setAttribute("min",minMachSpeed);
    //batchSpeedInput.setAttribute("max",maxMachSpeed);
    //batchSpeedInput.value = Math.round(recommendedMachSpeed);
    document.getElementById("minMachSpeed").innerText = minMachSpeed;
    document.getElementById("maxMachSpeed").innerText = maxMachSpeed;

    document.getElementById("recSpeed").innerText = Math.round(recommendedMachSpeed);
}

setRequired(0,0,0,0,0,0);
connectWebSocket();
function onPageLoad() {
    console.log("Load");
    stompClient.send("/app/sensor/data/onload", {}, {});
}

document.addEventListener("DOMContentLoaded", function() {
    var item = document.querySelector(".notification-drop .item");

    item.addEventListener("click", function() {
        var ul = this.querySelector("ul");
        if (ul) {
            ul.style.display = (ul.style.display === "none" || ul.style.display === "") ? "block" : "none";

            const badge = document.querySelector('.btn__badge');
            badge.textContent = "0";
        }
    });
});

function subscribeToNotification(nodeName) {
    stompClient.subscribe('/notification/' + nodeName, (message) => {
        let newState = message.body;
        const badge = document.querySelector('.btn__badge');
        const notificationList = document.getElementById('notificationList');
        const notificationId = 'notification-' + nodeName;
        const existingNotification = document.getElementById(notificationId);
        if (!existingNotification) {
            const newNotification = document.createElement("li");
            newNotification.id = notificationId;
            newNotification.textContent = `${newState}`
            notificationList.appendChild(newNotification);
            badge.textContent = parseInt(badge.textContent) + 1;
        } else {
            existingNotification.textContent = `${newState}`
            let stringArr = newState.split(":");
            console.log(stringArr[0]);
            console.log(stringArr[1]);
            let valueArr = stringArr[1].split(".");
            if(stringArr[1]=="10.0%"){
                badge.textContent = "" + notificationList.children.length;
            }
        }
    });
}
    function checkFilling(nodeName) {
        stompClient.subscribe('/sensor/data/' + nodeName, (message) => {
            boolean = message.body;
            if(boolean) {
                const list = document.getElementById("notificationList");
                const barley = document.getElementById("notification-barley");
                const hops = document.getElementById("notification-hops");
                const yeast = document.getElementById("notification-yeast");
                const malt = document.getElementById("notification-malt");
                const wheat = document.getElementById("notification-wheat");
                if(barley !== null) {
                    list.removeChild(barley);
                }
                if(hops !== null) {
                    list.removeChild(hops);
                }
                if(yeast !== null) {
                    list.removeChild(yeast);
                }
                if(malt !== null) {
                    list.removeChild(malt);
                }
                if(wheat !== null) {
                    list.removeChild(wheat);
                }
                const badge = document.querySelector('.btn__badge');
                badge.textContent = "" + list.children.length;
            }
        });

}







