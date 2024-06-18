import ui from "./ui";

let formRestorant = function (event) {
    //récupération et affichage du template stationInfo
    console.log(event);
    let htmlFormResto = document.getElementById("formResto");
    let formRestoTemplate = document.getElementById("creationRestoTemplate");
    let template = Handlebars.compile(formRestoTemplate.innerHTML);
    htmlFormResto.innerHTML = template({});
    document.getElementById("lat").value = event.latlng.lat;
    document.getElementById("lng").value = event.latlng.lng;
}

let callbackFormResto = async function (e) {
    e.preventDefault();

    // Récupèration des champ du formulaire
    let nomResto = document.getElementById('nomResto');
    let adresse = document.getElementById('adresse');
    let nombrePlaces = document.getElementById('nombrePlaces');
    let lat = document.getElementById('lat');
    let lng = document.getElementById('lng');

    // Récupèration des éléments permétant d'afficher les erreurs
    let errorNomResto = document.getElementById('errorNomResto');
    let errorAdresse = document.getElementById('errorAdresse');
    let errorNombrePlaces = document.getElementById('errorNombrePlaces');
    let errorLat = document.getElementById('errorLat');
    let errorLng = document.getElementById('errorLng');

    // Réinitialise des messages d'erreur
    errorNomResto.textContent = '';
    errorAdresse.textContent = '';
    errorNombrePlaces.textContent = '';
    errorLng.textContent = '';
    errorLat.textContent = '';

    let isValid = true;

    // Vérification des différents champ
    if (nomResto.value.trim() === '') {
        errorNomResto.textContent = 'Le champ "Nom du restaurant" doit être rempli';
        isValid = false;
    }

    if (adresse.value.trim() === '') {
        errorAdresse.textContent = 'Le champ "Adresse" doit être rempli';
        isValid = false;
    }

    if (nombrePlaces.value.trim() === '' || nombrePlaces.value <= 0) {
        errorNombrePlaces.textContent = 'Le champ "Nombre de places" doit être rempli avec une valeur positive';
        isValid = false;
    }

    if (lng.value.trim() === '' || nombrePlaces.value <= 0) {
        errorLng.textContent = 'Le champ "Longitude" doit être rempli avec une valeur positive';
        isValid = false;
    }

    if (lat.value.trim() === '' || nombrePlaces.value <= 0) {
        errorLat.textContent = 'Le champ "Latitude" doit être rempli avec une valeur positive';
        isValid = false;
    }

    let marker = L.marker([station.lat, station.lon]);
    marker.on('click', function () {
        //affichage des infos de la station apres un click
        ui.displayInfoStation(station)
        //changement des icones du dernier et du nouveau marker clické
        if (lastClicked !== undefined) {
            lastClicked.setIcon(myIcon2)
        }
        this.setIcon(myIcon)
    }).addTo(map)

    // Submit formulaire
    if (isValid) {
        e.target.submit();
    }
}

export default {
    formRestorant,
    callbackFormResto,
}
