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
        let fetchBD = await fetch("http://localhost:8001/getRestaurants");
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
            let address = restaurant.addresse;
            let lat = restaurant.xGPS;
            let lon = restaurant.yGPS;
            restaurantList.push({ id, name, address, lat, lon })
        })
        return restaurantList;
    } catch (error) {
        alert('fetch error :' + error.message);
    }
}

export default {
    infoStations,
    infoRestaurants
}
