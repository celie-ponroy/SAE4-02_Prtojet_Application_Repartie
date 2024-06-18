import {iconRestos} from "./index.js";
let formRestorant = function (event) {
    //récupération et affichage du template stationInfo
    let htmlFormResto = document.getElementById("formResto");
    let formRestoTemplate = document.getElementById("creationRestoTemplate");
    let template = Handlebars.compile(formRestoTemplate.innerHTML);
    htmlFormResto.innerHTML = template({});
    document.getElementById("lat").value = event.latlng.lat;
    document.getElementById("lng").value = event.latlng.lng;
    L.marker([event.latlng.lat, event.latlng.lng],{icon:iconRestos}).addTo(map)
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

    if (lng.value.trim() === '' || lng.value <= 0) {
        errorLng.textContent = 'Le champ "Longitude" doit être rempli avec une valeur positive';
        isValid = false;
    }

    if (lat.value.trim() === '' || lat.value <= 0) {
        errorLat.textContent = 'Le champ "Latitude" doit être rempli avec une valeur positive';
        isValid = false;
    }

    // Création dans la bdd
    if (isValid) {
        try {
            // Resto avec données du formulaire

            let resto = {
                nom: nomResto.value,
                adresse: adresse.value,
                nbPlaces: nombrePlaces.value,
                xGPS: lng.value,
                yGPS: lng.value
            };

            console.log(resto);

            // Envoye requête POST au serveur
            let response = await fetch('http://localhost:8001/createRestaurant', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8',
                },
                body: JSON.stringify(resto)
            });

            console.log(response);

            if (response.ok) {
                let jsonResponse = await response.json();
                console.log('Restaurant créé avec succès:', jsonResponse);
                // Submit apres succès
                e.target.submit();
            } else {
                console.error('Erreur lors de la création du restaurant:', response.statusText);
            }
        } catch (error) {
            console.error('Erreur lors de la requête:', error);
        }
    }
}

export default {
    formRestorant,
    callbackFormResto,
}
