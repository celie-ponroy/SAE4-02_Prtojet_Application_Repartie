/*Fichier contenant les fonctions de récupération des données des stations de vélo et de la météo*/
import { getCurrentFormattedDate } from './timeUtil.js';

//Fonction pour récupérer les informations des stations de vélo Vlib
export async function infoStations() {
    try {
        //fetch pour récupère les infos des stations
        let fetchStation = await fetch("https://transport.data.gouv.fr/gbfs/nancy/station_information.json");
        let fetchStatusStation = await fetch("https://transport.data.gouv.fr/gbfs/nancy/station_status.json");
        //passage en json
        let stationJSON = await fetchStation.json();
        let statusStationJSON = await fetchStatusStation.json();
        //extraction des données json
        let stationList = [];
        //Boucle pour recuperer les infos des stations
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

//Fonction pour récupérer les données météo de la date courante
export async function fetchMeteo() {
    try {
        let response = await fetch("https://www.infoclimat.fr/public-api/gfs/json?_ll=48.67103,6.15083&_auth=ARsDFFIsBCZRfFtsD3lSe1Q8ADUPeVRzBHgFZgtuAH1UMQNgUTNcPlU5VClSfVZkUn8AYVxmVW0Eb1I2WylSLgFgA25SNwRuUT1bPw83UnlUeAB9DzFUcwR4BWMLYwBhVCkDb1EzXCBVOFQoUmNWZlJnAH9cfFVsBGRSPVs1UjEBZwNkUjIEYVE6WyYPIFJjVGUAZg9mVD4EbwVhCzMAMFQzA2JRMlw5VThUKFJiVmtSZQBpXGtVbwRlUjVbKVIuARsDFFIsBCZRfFtsD3lSe1QyAD4PZA%3D%3D&_c=19f3aa7d766b6ba91191c8be71dd1ab2");
        let meteoData = await response.json();

        let currentHourOnly = getCurrentFormattedDate(); // Heure courante au format YYYY-MM-DD hh:00:00
        let currentDate = new Date(currentHourOnly);
        const temperatures = []; // Liste des températures après l'heure actuelle
        
        // Récupére les températures et les icônes du jour courant
        for (let hourIncrement = 2; hourIncrement < 24; hourIncrement += 3) {
            const targetDate = new Date(currentDate);
            targetDate.setHours(2 + hourIncrement); // Commence à 2h du matin et incrémente par 3 heures

            const targetDateString = targetDate.toISOString().slice(0, 19).replace('T', ' '); // Formate comme YYYY-MM-DD hh:00:00
            const formattedTime = targetDateString.slice(11, 16); // Coupe la date pour avoir seulement l'heure

            if (meteoData[targetDateString]) {
                const data = meteoData[targetDateString];
                const temperature = data.temperature['2m'];
                const nebulosity = data.nebulosite.totale;
                const pluie = data.pluie;

                // Sélection de l'icône en fonction du temps
                let icon = 'soleil.png';
                if (formattedTime == '05:00' ) {
                    icon = 'aube.png';
                } else if (formattedTime >= '21:00' || formattedTime <= '05:00') {
                    icon = 'lune.png';
                }else if (pluie > 1) {
                    icon = 'pluie.png';
                } else if (nebulosity > 50) {
                    icon = 'nuage.png';
                } 
                

                temperatures.push({
                    time: formattedTime,
                    temperature: (temperature - 273.15).toFixed(1), // Conversion de Kelvin à Celsius
                    icon: `./img/${icon}` // Chemin de l'icône
                });
            }
        }

        return temperatures;

    } catch (error) {
        console.error('Erreur de fetch : ' + error.message);
    }
}

