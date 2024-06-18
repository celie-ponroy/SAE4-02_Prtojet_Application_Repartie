import fetch from "./fetch.js";
import ui from "./ui.js"

//icone a définir
var myIcon = L.icon({
    iconUrl: './img/bike.svg',
    iconSize: [38, 95],
    popupAnchor: [-3, -76],
    shadowSize: [68, 95],
    shadowAnchor: [22, 94]
});

//icone a définir
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

    //récupération des infos des stations
     let infoStations = await fetch.infoStations();
     if (infoStations !== undefined) {
         infoStations.forEach(station => {
             //ajout des markers
             let marker = L.marker([station.lat, station.lon]);
             marker.on('click', function () {
                 //affichage des infos de la station apres un click
                 ui.displayInfoStation(station)
                 //changement des icones du dernier et du nouveau marker clické
                 if (lastClicked !== undefined) {
                     lastClicked.setIcon(myIcon2)
                 }
                 this.setIcon(myIcon)
                 lastClicked = this;
             }).addTo(map)
         });
     }

    let infoRestaurants = await fetch.infoRestaurants();
    if (infoRestaurants !== undefined) {
        infoRestaurants.forEach(restaurant => {
            let marker = L.marker([restaurant.lat, restaurant.lon]);
            marker.on('click', function () {
                //affichage des infos du restaurant apres un click
                ui.displayInfosResto(restaurant)
                //changement des icones du dernier et du nouveau marker clické
                if (lastClicked !== undefined) {
                    lastClicked.setIcon(myIcon2)
                }
                this.setIcon(myIcon)
                lastClicked = this;
            }).addTo(map)
        });
    }

    let infosTrafic = await fetch.infosTrafic();
    if (infosTrafic !== undefined) {
        infosTrafic.forEach(trafic => {
            let marker = L.marker([trafic.lat, trafic.long]);
            marker.on('click', function () {
                //affichage des infos du trafic apres un click
                ui.displayInfosTrafic(trafic)
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
    let meteoData = await fetch.fetchMeteo(); // Récupère les données météo
    if (meteoData) {
        ui.displayInfoMeteo(meteoData); // Affiche les données météo
    }

    // Afficher l'onglet "Accueil" par défaut lors du chargement de la page
    showTab('accueil');
}

document.addEventListener('DOMContentLoaded', init);
