
function createHistoryElement(data) {
    const row = document.createElement("tr");
    row.id = data.batchId;

    const idData = document.createElement("td");
    idData.id = "historyID";
    idData.textContent = data.batchId;

    const typeData = document.createElement("td");
    typeData.id = "historyType";
    typeData.textContent = data.brewName;

    const amountData = document.createElement("td");
    amountData.id = "historyAmount";
    amountData.textContent = data.amount;

    const startData = document.createElement("td");
    startData.id = "historyStart";
    startData.textContent = data.startTime;

    const stopData = document.createElement("td");
    stopData.id = "historyStop";
    if (data.endTime == null) {
        data.endTime = "endtime not found"
    }
    stopData.textContent = data.endTime; // assuming endTime is a property in your data

    row.appendChild(idData);
    row.appendChild(typeData);
    row.appendChild(amountData);
    row.appendChild(startData);
    row.appendChild(stopData);

    document.getElementById("historyTable").appendChild(row);

    row.addEventListener("click", function (row) {
        window.location.href = `historyPageOpen.html?batchId=${data.batchId}`
    });
}

function fillTable() {
    fetch("http://localhost:8080/api/v1/batch")
        .then(response => response.json())
        .then(jsonArray => {
            jsonArray.forEach(data => createHistoryElement(data));
        })
        .catch(error => console.error("Error fetching data:", error));
}
fillTable();