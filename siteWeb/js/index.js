import ui from "./ui.js"

// setView([latitude,longitude], zoom)
var map = L.map('map').setView([48.688135, 6.171586 ], 13);
L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 18,
    minZoom:12,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
}).addTo(map);

ui.displayInfoStation();
