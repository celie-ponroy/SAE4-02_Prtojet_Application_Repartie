/*Fichier contenant les fonctions d'affichage des données */

//Fonction pour afficher les informations des stations de vélo
export function displayInfoStations(station) {
    //récupération et affichage du template stationInfo
    let htmlStation = document.getElementById("stationInfo");
    let stationTemplate = document.getElementById("stationTemplate");
    let template = Handlebars.compile(stationTemplate.innerHTML);
    htmlStation.innerHTML = template({
        stations: station,
        name: station.name,
        nbVelo: station.nbVelo,
        nbDock: station.nbDock,
        address: station.address,
    });
}

//Fonction pour afficher les informations météo
export function displayInfoMeteo(temperatures) {
    const meteoInfoDiv = document.getElementById('meteoInfo');
    const meteoTemplate = document.getElementById('meteoTemplate');
    const template = Handlebars.compile(meteoTemplate.innerHTML);
    meteoInfoDiv.innerHTML += template({
        temperatures: temperatures
    });
}