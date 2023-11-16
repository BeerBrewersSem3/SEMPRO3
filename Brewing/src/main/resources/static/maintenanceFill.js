function maintenanceFill() {

    const bar = document.getElementById("maintenanceBar");
    const label = document.getElementById("maintenance");

    for (let i = 0; i <= 100; i++) {
        setTimeout(() => {
            bar.style.width = i + "%";
            bar.style.background = colorCalc(i);
            label.innerText = i + "%";
        }, i*250);
    }
}

function colorCalc(fill) {
    // Ensure the input value is within the range [0, 100]
    fill = Math.min(100, Math.max(0, fill));

    const r = Math.floor(255 * (fill <= 50 ? fill / 50 : 1));
    const g = Math.floor(255 * (fill <= 50 ? 1 : 1 - (fill - 50) / 50));
    const b = 0;

    return `rgb(${r}, ${g}, ${b})`;
}