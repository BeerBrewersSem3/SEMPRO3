var array = [];
var array_length = 0;
var tableSize = 25;
var startIndex = 1;
var endIndex = 0;
var currentIndex = 1;
var maxIndex = 0;

async function preLoad() {
    array = await fetchHistory(0,tableSize);
    array_length = array.length;
    maxIndex = Math.ceil(array_length / tableSize); // Use Math.ceil to round up

    if ((array_length % tableSize) > 0) {
        maxIndex++;
    }
}

async function displayIndexButton() {
    await preLoad();
    $(".indexBtns button").remove();
    $(".indexBtns").append('<button onclick="previous()">Previous</button>');

    // Display only 3 buttons centered around the current index
    var start = Math.max(1, currentIndex - 1);
    var end = Math.min(maxIndex, start + 2);

    for (let i = start; i <= end; i++) {
        $(".indexBtns").append('<button onclick="indexPagination('+ i +')" index="'+ i +'">'+ i +'</button>');
    }
    $(".indexBtns").append('<button onclick="next();">Next</button>');

    highlightIndexButton();
}

displayIndexButton();

function highlightIndexButton() {
    startIndex = ((currentIndex - 1) * tableSize) + 1;
    endIndex = Math.min(startIndex + tableSize - 1, array_length);

    $(".footer span").text('Showing '+ startIndex +' to '+ endIndex +' of '+ array_length +' entries');
    $(".indexBtns button").removeClass('active');
    $(".indexBtns button[index='" + currentIndex + "']").addClass('active');

    displayTable();
}

async function displayTable() {
    $(".historyTable tbody tr").remove();
    var tabStart = startIndex - 1;
    var tabEnd = endIndex;

    for (let i = tabStart; i < tabEnd; i++) {
        var entry = array[i];

        var row = document.createElement('tr'); // Create a new tr element
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


function next() {
    if (currentIndex < maxIndex) {
        startIndex = endIndex + 1; // Update the start index for the next page
        currentIndex++;
        displayIndexButton();
        highlightIndexButton();
    }
}

function previous() {
    if (currentIndex > 1) {
        currentIndex--;
        startIndex = Math.max(0, startIndex - tableSize); // Update the start index for the previous page
        displayIndexButton();
        highlightIndexButton();
    }
}


async function fetchHistory(startIndex,limit) {
    let dataArray = [];
    return fetch(`http://localhost:8080/api/v1/batch?start=${startIndex}&limit=${limit}`)
        .then(response => response.json())
        .then(jsonArray => {
            jsonArray.forEach(data => {
                dataArray.push(data);
            });
            return dataArray;
        })
        .catch(error => console.error("Error fetching data:", error));
}
