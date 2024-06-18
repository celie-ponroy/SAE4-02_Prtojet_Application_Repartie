import {iconRestos, lastClickedRestaurant} from "./index.js";

let formRestorant = function (event) {
    //récupération et affichage du template stationInfo
    let htmlFormResto = document.getElementById("formResto");
    let formRestoTemplate = document.getElementById("creationRestoTemplate");
    let template = Handlebars.compile(formRestoTemplate.innerHTML);
    htmlFormResto.innerHTML = template({});
    document.getElementById("lat").value = event.latlng.lat;
    document.getElementById("lng").value = event.latlng.lng;
    L.marker([event.latlng.lat, event.latlng.lng], {icon: iconRestos}).addTo(map)
}

let formReserver = function () {
    //récupération et affichage du template stationInfo
    let htmlStationInfo = document.getElementById("stationInfo");
    let formReservationRestoTemplate = document.getElementById("reservationRestoTemplate");
    let template = Handlebars.compile(formReservationRestoTemplate.innerHTML);
    htmlStationInfo.innerHTML = template({
        nomResto: lastClickedRestaurant.name,
        address: lastClickedRestaurant.address,
        nbPlaces: lastClickedRestaurant.nbPlaces,
    });
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
                xGPS: lat.value,
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

let callbackFormReservationResto = async function (e) {
    e.preventDefault();

    // Récupèration des champs du formulaire
    let nomClient = document.getElementById('nomClient');
    let prenomClient = document.getElementById('prenomClient');
    let nbConvives = document.getElementById('nbConvives');
    let numTel = document.getElementById('numTel');
    let numRestaurant = lastClickedRestaurant.id;
    let date = document.getElementById('date');

    // Récupèration des éléments permétant d'afficher les erreurs
    let errorNomClient = document.getElementById('errorNomClient');
    let errorPrenomClient = document.getElementById('errorPrenomClient');
    let errorNbConvives = document.getElementById('errorNbConvives');
    let errorNumTel = document.getElementById('errorNumTel');
    let errorDate = document.getElementById('errorDate');

    // Réinitialise des messages d'erreur
    errorNomClient.textContent = '';
    errorPrenomClient.textContent = '';
    errorNbConvives.textContent = '';
    errorNumTel.textContent = '';
    errorDate.textContent = '';

    let isValid = true;

    // Vérification des différents champ

    // Vérification des différents champs
    if (nomClient.value.trim() === '') {
        errorNomClient.textContent = 'Le champ "Nom du client" doit être rempli';
        isValid = false;
    }
    if (prenomClient.value.trim() === '') {
        errorPrenomClient.textContent = 'Le champ "Prénom du client" doit être rempli';
        isValid = false;
    }
    if (nbConvives.value.trim() === '' || nbConvives.value <= 0) {
        errorNbConvives.textContent = 'Le champ "Nombre de convives" doit être rempli avec une valeur positive';
        isValid = false;
    }
    if (numTel.value.trim() === '' || !/^\d{10}$/.test(numTel.value)) {
        errorNumTel.textContent = 'Le champ "Numéro de téléphone" doit être rempli avec un numéro valide de 10 chiffres';
        isValid = false;
    }
    if (date.value.trim() === '') {
        errorDate.textContent = 'Le champ "Date de réservation" doit être rempli';
        isValid = false;
    }

    // Création dans la bdd
    if (isValid) {
        try {
            // Resto avec données du formulaire
            let reservation = {
                nomClient: nomClient.value,
                prenomClient: prenomClient.value,
                nbConvives: nbConvives.value,
                numTel: numTel.value,
                numRestaurant: numRestaurant,
                date: date.value
            };

            // Envoye requête POST au serveur
            let response = await fetch('http://localhost:8001/setReservation', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8',
                },
                body: JSON.stringify(reservation)
            });

            console.log(response);

            if (response.ok) {
                let jsonResponse = await response.json();
                console.log('Réservation créé avec succès:', jsonResponse);
                // Submit apres succès
                e.target.submit();
            } else {
                console.error('Erreur lors de la création de la réservation:', response.statusText);
            }
        } catch (error) {
            console.error('Erreur lors de la requête:', error);
        }
    }
}

export default {
    formRestorant,
    callbackFormResto,
    formReserver,
    callbackFormReservationResto,
}
