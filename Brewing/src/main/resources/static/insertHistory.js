const urlParams = new URLSearchParams(window.location.search);
const batchId = urlParams.get('batchId');

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
            document.getElementById("started").innerText = formatString(data.startTime);
            if (data.endTime == null) {
                document.getElementById("stopped").innerText = "endtime not found";
            }
            document.getElementById("stopped").innerText = formatString(data.endTime);
            document.getElementById("workerID").innerText = data.operation.worker.workerId;
        });
}

function formatString(string) {
    const replaced = string.replace('T', ' | ');
    return replaced.split('.')[0].trim();
}


function fillTable() {
    fetch("http://localhost:8080/api/v1/sensorreading")
        .then(response => response.json())
        .then(data => {
            processJson(data);
        });
}

function processJson(entries) {
    const table = document.getElementById("openHistoryTable")

    entries.forEach((entry, index) => {
        if (index % 3 === 0) {
            const row = document.createElement('tr');

            for (const key in entry) {
                const cell = document.createElement('td');
                cell.textContent = entry[key];
                row.appendChild(cell);
            }

            // Append the row to the table body
            table.appendChild(row);
        }
    });
}

insertInfo();
