/*Fichier contenant les fonctions utiles pour la gestion du temps*/

//Fonction pour récupérer l'heure actuelle au format YYYY-MM-DD hh:00:00
export function getCurrentFormattedDate() {
    const date = new Date();
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0'); // Mois : 0 est janvier, donc +1
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    

    return `${year}-${month}-${day} ${hours}:00:00`;
}
