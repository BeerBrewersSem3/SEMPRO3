const urlParams = new URLSearchParams(window.location.search);
const batchId = urlParams.get('batchId');
console.log("Batch Id: ",batchId)

function insertInfo() {
    fetch("http://localhost:8080/api/v1/batch/" + batchId)
        .then(response => response.json())
        .then(data => {
            document.getElementById("id").innerText = data.batchId;
            document.getElementById("type").innerText = data.brewName;
            document.getElementById("amount").innerText = data.amount;
            document.getElementById("produced").innerText = data.completedCount + data.defectiveCount;
            document.getElementById("successful").innerText = data.completedCount;
            document.getElementById("rejected").innerText = data.defectiveCount;
            document.getElementById("speed").innerText = data.speed;
            document.getElementById("started").innerText = formatTime(data.startTime);
            document.getElementById("workerID").innerText = data.operation.worker.name;
            if (data.endTime == null) {
                document.getElementById("stopped").innerText = "endtime not found";
            }
            document.getElementById("stopped").innerText = formatTime(data.endTime);

        });
}



async function createTableEntries(id) {
    let data = await bundleEntries(id)
    const table = document.getElementById("tableBody")
    console.log("Sensor Array på batchId: ",data);
    console.log("Længde af sensor array på batchId: ",data.length);
    console.log("Mængden af elementer der skal være i table: ",data.length/3)

    for (let i = 0; i < data.length; i++) {
        if (i % 3 === 0) {
            const row = document.createElement('tr');
            const timestamp = document.createElement('td');

            timestamp.textContent = formatTime(data[i].timestamp);
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
