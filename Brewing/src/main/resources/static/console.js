
function subscribeToConsoleMessages(){
    const console = document.getElementById("console");
    stompClient.subscribe("/console/message", message => {
        let listEntry = document.createElement("li");
        let timeStamp = new Date(Date.now());
        let formattedTimeStamp = timeStamp.getDate() + "/" + timeStamp.getMonth() + " - " + timeStamp.getHours() + ":" + timeStamp.getMinutes() + ":" + timeStamp.getSeconds() + " | ";
        listEntry.textContent = formattedTimeStamp + message.body;
        console.appendChild(listEntry);
    })
}

function clearConsole(){
    const console = document.getElementById("console");
    console.innerHTML = "";
}