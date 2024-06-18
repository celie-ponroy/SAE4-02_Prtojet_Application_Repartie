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

function displayInfosResto(resto) {
    let htmlResto = document.getElementById("stationInfo");
    let restoTemplate = document.getElementById("restoTemplate");
    let template = Handlebars.compile(restoTemplate.innerHTML);
    htmlResto.innerHTML = template({
        nomResto : resto.name,
        address : resto.address,
        xGPS : resto.lat,
        yGPS : resto.lon,
        nbPlaces : resto.nbPlaces,
    });
}

function displayInfosTrafic(trafic) {
    let htmlTrafic = document.getElementById("stationInfo");
    let traficTemplate = document.getElementById("traficTemplate");
    let template = Handlebars.compile(traficTemplate.innerHTML);
htmlTrafic.innerHTML = template({
        shortDescription : trafic.shortDescription,
        description : trafic.description,
        dateDebut : trafic.dateDebut,
        dateFin : trafic.dateFin,
        adresse : trafic.adresse,
        type : trafic.type,
    });
}

export default {
    displayInfoStation: displayInfoStations, displayInfosTrafic : displayInfosTrafic, displayInfosResto : displayInfosResto
}