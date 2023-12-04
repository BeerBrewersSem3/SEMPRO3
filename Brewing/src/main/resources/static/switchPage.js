function openHistory() {
    window.location.href = 'history';
}

function openMonitor() {
    window.location.href = 'monitor';
}

function toggleNewBatch() {
    document.getElementById("newBatch").classList.toggle("active");
    fetch("http://localhost:8080/recipe/nextBatchId")
        .then(response => response.json())
        .then(nextId => {
            document.getElementById("batchID").innerText = nextId;
        });
}

function openHistoryOpen() {
    window.location.href = `historyPageOpen.html`;
}