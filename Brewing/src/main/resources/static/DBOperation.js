
function login() {
    createOperation();
    //openMonitor();
}

function openMonitor() {
    window.location.href = 'monitorPage.html';
}

function createOperation() {
// Step 1: Fetch the Worker from the database
    const workerId = 1;
    fetch(`http://localhost:8080/api/v1/worker/${workerId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json();
        })
        .then(worker => {
            // Step 2: Use the fetched Worker to create the Operation
            const operation = {
                worker
            };
            // Step 3: Make a POST request to the saveOperation endpoint
            fetch('http://localhost:8080/api/v1/operation', {
                method: 'POST',
                body: JSON.stringify(operation),
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error(`HTTP error! Status: ${response.status}`);
                    }
                    // If the response is okay, you can handle it here
                    console.log('Operation saved successfully');
                    openMonitor();
                })
                .catch(error => {
                    console.error('Error saving operation:', error);
                });
        })
        .catch(error => {
            console.error('Error fetching worker:', error);
        });
}