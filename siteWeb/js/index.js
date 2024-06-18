import { fetchMeteo, infoStations } from "./fetch.js";
import { displayInfoMeteo, displayInfoStations } from "./ui.js";

// Icônes à définir
var myIcon = L.icon({
    iconUrl: './img/bike.svg',
    iconSize: [38, 95],
    popupAnchor: [-3, -76],
    shadowSize: [68, 95],
    shadowAnchor: [22, 94]
});

var myIcon2 = L.icon({
    iconUrl: './img/parking.svg',
    iconSize: [38, 95],
    popupAnchor: [-3, -76],
    shadowSize: [68, 95],
    shadowAnchor: [22, 94]
});

// Variable contenant le dernier marker cliqué
let lastClicked = undefined;

// Fonction d'initialisation
async function init() {
    // Initialisation de la carte
    let map = L.map('map').setView([48.688135, 6.171586], 13);
    L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
        // maxZoom: 18,
        // minZoom: 12,
        attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    }).addTo(map);

    window.map = map; // Stocke la carte dans window pour accès global

    // Récupération des données des stations
    let stationsData = await infoStations();
    if (stationsData) {
        stationsData.forEach(station => {
            let marker = L.marker([station.lat, station.lon]);
            marker.on('click', function () {
                displayInfoStations(station);
                if (lastClicked) {
                    lastClicked.setIcon(myIcon2);
                }
                this.setIcon(myIcon);
                lastClicked = this;
            }).addTo(map);
        });
    }

    let infoRestaurants = await fetch.infoRestaurants();
    if (infoRestaurants !== undefined) {
        infoRestaurants.forEach(restaurant => {
            let marker = L.marker([restaurant.lat, restaurant.lon]);
            marker.on('click', function () {
                //changement des icones du dernier et du nouveau marker clické
                if (lastClicked !== undefined) {
                    lastClicked.setIcon(myIcon2)
                }
                this.setIcon(myIcon)
                lastClicked = this;
            }).addTo(map)
        });
    }

    // Récupération des données météo
    let meteoData = await fetchMeteo(); // Récupère les données météo
    if (meteoData) {
        displayInfoMeteo(meteoData); // Affiche les données météo
    }

    // Afficher l'onglet "Accueil" par défaut lors du chargement de la page
    showTab('accueil');
}

document.addEventListener('DOMContentLoaded', init);
