import fetch from "./fetch.js";
import ui from "./ui.js"
import form from "./form.js"


var iconVeloCourant = L.icon({
    iconUrl: './img/bike.svg',
    iconSize: [38, 95],
    popupAnchor: [-3, -76],
    shadowSize: [68, 95],
    shadowAnchor: [22, 94]
});

var iconVelib = L.icon({
    iconUrl: './img/icon_velib.png',
    iconSize: [23, 40],
    popupAnchor: [-3, -76],
    shadowSize: [68, 85],
    shadowAnchor: [22, 84]
});


var iconRestoCourant = L.icon({
    iconUrl: './img/resto.png',
    iconSize: [40, 40],
    popupAnchor: [-3, -76],
    shadowSize: [68, 85],
    shadowAnchor: [22, 84]
});

export var iconRestos = L.icon({
    iconUrl: './img/icon_resto.png',
    iconSize: [23, 40],
    popupAnchor: [-3, -76],
    shadowSize: [68, 85],
    shadowAnchor: [22, 84]
});

var iconTraficCourant = L.icon({
    iconUrl: './img/trafic_courant.png',
    iconSize: [40, 40],
    popupAnchor: [-3, -76],
    shadowSize: [68, 85],
    shadowAnchor: [22, 84]
});

var iconTrafic = L.icon({
    iconUrl: './img/icon_trafic.png',
    iconSize: [23, 40],
    popupAnchor: [-3, -76],
    shadowSize: [68, 85],
    shadowAnchor: [22, 84]
});

export let lastRestaurant;


// Fonction d'initialisation
async function init() {
    // Initialisation de la carte
    let map = L.map('map').setView([48.688135, 6.171586], 13);
    L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 18,
        minZoom: 12,
        attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    }).addTo(map);


    window.map = map; // Stocke la carte dans window pour accès global


// Variables pour stocker les derniers marqueurs cliqués

    let lastClickedStation;
    let lastClickedRestaurant;
    let lastClikedTrafic;


// Récupération des infos des stations
    let infoStations = await fetch.infoStations();
    if (infoStations !== undefined) {
        infoStations.forEach(station => {
            // Ajout des markers
            let marker = L.marker([station.lat, station.lon], {icon: iconVelib});
            marker.on('click', function () {
                // Affichage des infos de la station après un clic
                ui.displayInfoStation(station);

                // Réinitialisation de l'icône du dernier marqueur cliqué pour les stations
                if (lastClickedStation !== undefined) {
                    lastClickedStation.setIcon(iconVelib);
                }

                // Définition de l'icône pour le marqueur actuellement cliqué
                this.setIcon(iconVeloCourant);
                lastClickedStation = this; // Mettre à jour le dernier marqueur cliqué pour les stations
            }).addTo(map);
        });
    }


    //addEventListeneur pour contextMenu
    map.on('contextmenu', function (event) {
        form.formRestorant(event);
    });

    document.getElementById("formResto").addEventListener("submit", (event) => {
        form.callbackFormResto(event)
    });

    //addEventListeneur pour reservation de réstaurant
    document.getElementById("stationInfo").addEventListener('click', function (event) {
        if(event.target.id == "reservation"){
            form.formReserver();
        }
    });

    document.getElementById("stationInfo").addEventListener("submit", (event) => {
        form.callbackFormReservationResto(event);
    });


// Récupération des infos des restaurants
    let infoRestaurants = await fetch.infoRestaurants();
    if (infoRestaurants !== undefined) {
        infoRestaurants.forEach(restaurant => {
            let marker = L.marker([restaurant.lat, restaurant.lon], {icon: iconRestos});
            marker.on('click', function () {
                // Affichage des infos du restaurant après un clic
                ui.displayInfosResto(restaurant);

                // Réinitialisation de l'icône du dernier marqueur cliqué pour les restaurants
                if (lastClickedRestaurant !== undefined) {
                    lastClickedRestaurant.setIcon(iconRestos);
                }

                lastRestaurant = restaurant;

                // Définition de l'icône pour le marqueur actuellement cliqué
                this.setIcon(iconRestoCourant);
                lastClickedRestaurant = this; // Mettre à jour le dernier marqueur cliqué pour les restaurants
            }).addTo(map);
        });
    }


    let infosTrafic = await fetch.infosTrafic();
    if (infosTrafic !== undefined) {
        infosTrafic.forEach(trafic => {
            let marker = L.marker([trafic.lat, trafic.long], {icon: iconTrafic});
            marker.on('click', function () {
                //affichage des infos du trafic apres un click
                ui.displayInfosTrafic(trafic)
                //changement des icones du dernier et du nouveau marker clické
                if (lastClikedTrafic !== undefined) {
                    lastClikedTrafic.setIcon(iconTrafic)
                }
                this.setIcon(iconTraficCourant)
                lastClikedTrafic = this;
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
