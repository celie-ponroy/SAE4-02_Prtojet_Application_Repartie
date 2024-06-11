function displayInfoStations(station) {
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

export default {
    displayInfoStation: displayInfoStations,
}