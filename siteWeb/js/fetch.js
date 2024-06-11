async function infoStation() {
    try {
        let fetchStation = await fetch("https://transport.data.gouv.fr/gbfs/nancy/station_information.json");
        let fetchStatusStation = await fetch("https://transport.data.gouv.fr/gbfs/nancy/station_status.json");
        let stationJSON = await fetchStation.json();
        let statusStationJSON = await fetchStatusStation.json();
        return {stationJSON, statusStationJSON}
    } catch (error) {
        console.log('fetch error :' + error.message);
    }
}

export default {
    infoStation: infoStation,
}
