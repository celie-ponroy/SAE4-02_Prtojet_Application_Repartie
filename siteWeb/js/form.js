let formRestorant = function () {
    //récupération et affichage du template stationInfo
    let htmlFormResto = document.getElementById("formResto");
    let formRestoTemplate = document.getElementById("creationRestoTemplate");
    let template = Handlebars.compile(formRestoTemplate.innerHTML);
    htmlFormResto.innerHTML = template({});
}

let callbackFormResto = async function (e) {
    e.preventDefault();

    // Récupèration des champ du formulaire
    let nomResto = document.getElementById('nomResto');
    let adresse = document.getElementById('adresse');
    let nombrePlaces = document.getElementById('nombrePlaces');

    // Récupèration des éléments permétant d'afficher les erreurs
    let errorNomResto = document.getElementById('errorNomResto');
    let errorAdresse = document.getElementById('errorAdresse');
    let errorNombrePlaces = document.getElementById('errorNombrePlaces');

    // Réinitialise des messages d'erreur
    errorNomResto.textContent = '';
    errorAdresse.textContent = '';
    errorNombrePlaces.textContent = '';

    let isValid = true;

    // Vérification des différents champ
    if (nomResto.value.trim() === '') {
        errorNomResto.textContent = 'Le champ "Nom du restaurant" doit être rempli';
        isValid = false;
    }


    /*let fetchAddresse = await fetch(`http://localhost:8001/adress/${adresse}`);
    let response = await fetchAddresse.json();
    console.log(response);
    /**if(response.code !== undefined && response.code == 400){
        errorAdresse.textContent = "Aucunne adresse corespondante n'as été trouvé";
        isValid = false;
    }else{
        console.log(response.features[0].properties.label);
    }*/

    if (adresse.value.trim() === '') {
        errorAdresse.textContent = 'Le champ "Adresse" doit être rempli';
        isValid = false;
    }

    if (nombrePlaces.value.trim() === '' || nombrePlaces.value <= 0) {
        errorNombrePlaces.textContent = 'Le champ "Nombre de places" doit être rempli avec une valeur positive';
        isValid = false;
    }

    // Submit formulaire
    if (isValid) {
        e.target.submit();
    }
}

export default {
    formRestorant: formRestorant,
    callbackFormResto: callbackFormResto
}

/*<div contextmenu="menu" id="box">
	Right click to get the context menu demo.
</div>

<menu type="context" id="menu">

	<menuitem type="checkbox" id="move">Moooove</menuitem>

	<menuitem type="command" label="Test me" id="custom">Hello World!</menuitem>

	<menuitem type="radio" name="aligngroup" value="left">Align Left</menuitem>

	<menuitem type="radio" name="aligngroup" value="right">Align Right</menuitem>

	<menuitem type="radio" name="aligngroup" checked="true" value="center">Align Center</menuitem>

	<menuitem type="checkbox" disabled>Disabled menu item</menuitem>
</menu>*/