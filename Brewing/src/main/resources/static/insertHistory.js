const urlParams = new URLSearchParams(window.location.search);
const batchId = urlParams.get('batchId');
console.log("Batch Id: ",batchId)

function insertInfo() {
    fetch("http://localhost:8080/api/v1/batch/" + batchId)
        .then(response => response.json())
        .then(async data => {
            document.getElementById("id").innerText = data.batchId;
            document.getElementById("type").innerText = data.brewName;
            document.getElementById("amount").innerText = data.amount;
            document.getElementById("produced").innerText = data.completedCount + data.defectiveCount;
            document.getElementById("successful").innerText = data.completedCount;
            document.getElementById("rejected").innerText = data.defectiveCount;
            document.getElementById("speed").innerText = data.speed;
            document.getElementById("started").innerText = await formatTime(data.startTime);
            document.getElementById("workerID").innerText = data.operation.worker.name;
            if (data.endTime == null) {
                document.getElementById("stopped").innerText = "endtime not found";
            }
            document.getElementById("stopped").innerText = await formatTime(data.endTime);

        });
}

async function createTableEntries(id) {
    let data = await bundleEntries(id);
    createGraph(data);
    const table = document.getElementById("tableBody");

    for (let i = 0; i < data.length; i++) {
        if (i % 3 === 0) {
            const row = document.createElement('tr');
            const timestamp = document.createElement('td');

            timestamp.textContent = await formatTime(data[i].timestamp);
            row.appendChild(timestamp);

            for (let j = 0; j < 3; j++) {
                const cell = document.createElement('td');
                cell.textContent = data[i+j].sensorValue;
                row.appendChild(cell);
            }
            table.appendChild(row);
        }
    }
}

function createGraph(data) {
    const timeStamps = [];
    const temperatures = [];
    const humidities = [];
    const vibrations = [];

    for (let i = 0; i < data.length; i++) {
        if (i % 3 === 0) {
            timeStamps.push(formatTime(data[i].timestamp));

            for (let j = 0; j < 3; j++) {
                switch(j) {
                    case 0:
                        temperatures.push(data[i+j].sensorValue);
                        break;
                    case 1:
                        humidities.push(data[i+j].sensorValue);
                        break;
                    case 2:
                        vibrations.push(data[i+j].sensorValue);
                        break;
                    default:
                        console.error("Unable to push sensorreading");
                }
            }
        }
    }
    new Chart("chart", {
        type: "line",
        data: {
            labels: timeStamps,
            datasets: [{
                label:'Temperature (°C)',
                data: temperatures,
                borderColor: "red",
                fill: false
            }, {
                label:'Humidity (%)',
                data: humidities,
                borderColor: "blue",
                fill: false
            }, {
                label:'Vibration (DPS)',
                data: vibrations,
                borderColor: "green",
                fill: false
            }]
        },
        options: {
            legend: {display: true}
        }
    });
}

async function bundleEntries(id) {
    let entries = [];
    let batchId = id;
    return fetch("http://localhost:8080/api/v1/sensorreading")
        .then(response => response.json())
        .then(jsonArray => {
            jsonArray.forEach((e) => {
                if (e.batch.batchId == batchId) {
                    entries.push(e);
                }
            })
            return entries;
        })
        .catch(error => {
            console.error("Noget gik galt... hvad spørger du? \n Bare det her", error);
        });
}

insertInfo();
createTableEntries(batchId);

function toggleGraph() {
    document.getElementById("graph").classList.toggle("active");
}
