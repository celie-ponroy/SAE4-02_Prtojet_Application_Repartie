import fetch from "./fetch.js";
import ui from "./ui.js"
import form from "./form.js"
//icone a définir
const myIcon = L.icon({
    iconUrl: './img/bike.svg',
    iconSize: [38, 95],
    popupAnchor: [-3, -76],
    shadowSize: [68, 95],
    shadowAnchor: [22, 94]
});

//icone a définir
const myIcon2 = L.icon({
    iconUrl: './img/parking.svg',
    iconSize: [38, 95],
    popupAnchor: [-3, -76],
    shadowSize: [68, 95],
    shadowAnchor: [22, 94]
});


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
        console.log("click")
    });

    document.getElementById("formResto").addEventListener("submit", (event) => {
        form.callbackFormResto(event)
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
