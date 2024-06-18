async function infoStations() {
    try {
        //fetch pour récupère les infos des stations
        let fetchStation = await fetch("https://transport.data.gouv.fr/gbfs/nancy/station_information.json");
        let fetchStatusStation = await fetch("https://transport.data.gouv.fr/gbfs/nancy/station_status.json");
        //passage en json
        let stationJSON = await fetchStation.json();
        let statusStationJSON = await fetchStatusStation.json();
        //extraction des données json
        let stationList = [];
        stationJSON.data.stations.forEach(station => {
            //regEx pour formatage nom
            let name = station.name.match(new RegExp(`[a-z].+`, "gi"));
            let lat = station.lat;
            let lon = station.lon;
            let id = station.station_id;
            let address = station.address;
            let status = statusStationJSON.data.stations.find(station => station.station_id === id);
            let nbDock = status !== undefined ? status.num_docks_available : "none";
            let nbVelo = status !== undefined ? status.num_bikes_available : "none";
            stationList.push({name, lat, lon, id, nbDock, nbVelo, address})
        })
        return stationList
    } catch (error) {
        alert('fetch error :' + error.message);
    }
}

async function infoRestaurants() {
    try {
        let fetchBD = await fetch("http://localhost:8001/resto");
        let restaurantJSON = await fetchBD.json();
        
        if (restaurantJSON === undefined || restaurantJSON.error !== "") {
            alert("error : " + restaurantJSON.error);
            return;
        }

        if (restaurantJSON.success == "false") {
            alert("No restaurant found");
        }

        let restaurantList = [];
        restaurantJSON.restaurants.forEach(restaurant => {
            let id = restaurant.numResto;
            let name = restaurant.nomResto;
            let address = restaurant.adresse;
            let nbPlaces = restaurant.nbPlaces;
            let lat = restaurant.xGPS;
            let lon = restaurant.yGPS;
            restaurantList.push({ id, name, address, nbPlaces, lat, lon })
        })
        return restaurantList;
    } catch (error) {
        alert('fetch error :' + error.message);
    }
}

async function infosTrafic(){
    try {
        let fetchTrafic = await fetch("http://localhost:8001/trafic");
        let traficJSON = await fetchTrafic.json();
        if (traficJSON === undefined) {
            alert("error : info trafic not found");
            return;
        }
        let traficList = [];
        traficJSON.incidents.forEach(incident => {
            let id = incident.id;
            let shortDescription = incident.short_description;
            let description = incident.description;
            let dateDebutFormat = incident.starttime;
            //changer le format de la date de début
            let dateDebut = dateDebutFormat.split("T")[0].split("-").reverse().join("/");
            //changer le format de la date de fin
            let dateFinFormat = incident.endtime;
            let dateFin = dateFinFormat.split("T")[0].split("-").reverse().join("/");
            let adresse = incident.location.location_description;
            let polyline = incident.location.polyline;
            let type = incident.type;
            let lat = polyline.split(" ")[0];
            let long = polyline.split(" ")[1];
            traficList.push({id, shortDescription, description, dateDebut, dateFin, adresse, lat, long, type})
        })
        return traficList;
    } catch (error) {
        alert('fetch error : info trafic');
    }

}

export default {
    infoStations,
    infoRestaurants,
    infosTrafic
}
