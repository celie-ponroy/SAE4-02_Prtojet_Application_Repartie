import fetch from "./fetch.js";

async function displayInfoStations() {
    let infoStation = await fetch.infoStation();
    if(infoStation !== undefined){
        let htmlStation = document.getElementById("stationInfo");
        let stationTemplate = document.getElementById("stationTemplate");
        let template = Handlebars.compile(stationTemplate.innerHTML);
        let stationList = [];
         infoStation.stationJSON.data.stations.forEach(station => {
            let name = station.name;
            let lat = station.lat;
            let lon = station.lon;
            let id = station.station_id;
            let status = infoStation.statusStationJSON.data.stations.find(station => station.station_id === id);
            let nbDock = status !== undefined ? status.num_docks_available : "none";
            let nbVelo = status !== undefined ? status.num_bikes_available : "none";
            stationList.push({name,lat,lon,id,nbDock,nbVelo})
        })
        console.log(stationList[0].nbVelo,stationList[0].nbDock,stationList[0].name)
        htmlStation.innerHTML = template({
            stations: stationList,
            name: stationList.name,
            nbVelo: stationList.nbVelo,
            nbDock: stationList.nbDock,
        });
    }
}

export default {
    displayInfoStation: displayInfoStations,
}