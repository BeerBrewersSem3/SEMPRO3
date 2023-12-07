
function fetchHistory(pageNumber, pageSize) {
    return fetch(`http://localhost:8080/api/v1/batch?pageNo=${pageNumber}&pageSize=${pageSize}`)
        .then(response => response.json());
}

async function loadPageArray(pageNumber,pageSize) {
    const fetched = await fetchHistory(pageNumber,pageSize);
    pageArray = fetched;
    console.log(pageArray);
    console.log(pageArray.data);
    if(pageArray && pageArray.data) {
        pageArray = pageArray.data;
        currentPage = fetched.pageNumber;
        totalPages = fetched.totalPages;
    }
}

async function displayTable() {
    $(".historyTable tbody tr").remove();

    for (const entry of pageArray) {
        var row = document.createElement('tr');
        row.innerHTML =
            '<td>' + entry.batchId + '</td>'+
            '<td>' + entry.brewName + '</td>'+
            '<td>' + entry.amount + '</td>'+
            '<td>' + await formatTime(entry.startTime) + '</td>'+
            '<td>' + await formatTime(entry.endTime) + '</td>';

        row.addEventListener("click", function () {
            window.location.href = `historyPageOpen.html?batchId=${entry.batchId}`;
        });

        $(".historyTable tbody").append(row);
    }
}


let currentPage = 0;
async function fetchAndDisplay(pageNumber,pageSize) {
    cursorLoadingAnimation();
    await loadPageArray(pageNumber,pageSize);
    await displayTable();
    currentPage = pageNumber;
    console.log(pageSize);
    cursorDefault();
}

async function nextPage() {
    console.log(currentPage);
    if (currentPage < totalPages - 1) {
        currentPage++;
        await fetchAndDisplay(currentPage, 10);
    }
}

async function previousPage() {
    console.log(currentPage);
    if (currentPage > 0) {
        currentPage--;
        await fetchAndDisplay(currentPage, 10);
    }
}

function setPageSize() {
    pageSize = pageArray.size;
}
fetchAndDisplay(0,10);