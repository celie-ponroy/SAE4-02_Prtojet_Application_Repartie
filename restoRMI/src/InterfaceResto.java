import org.json.JSONObject;

import java.rmi.Remote;

public interface InterfaceResto extends Remote {
    /**
     * Permet de creer un restaurant dans la base de donnees
     *
     * @param nomResto Nom du restaurant
     * @param adresse  Adresse du restaurant
     * @param nbPlaces Nombre de places du restaurant
     * @param xGPS     Coordonnee GPS en x
     * @param yGPS     Coordonnee GPS en y
     * @return Un objet JSON contenant le resultat de la requete
     */
    JSONObject createRestaurant(String nomResto, String adresse, int nbPlaces, double xGPS, double yGPS);

    /**
     * Permet de supprimer un restaurant de la base de donnees
     * @param numResto Numero du restaurant
     *                 @return Un objet JSON contenant le resultat de la requete
     */
    JSONObject deleteRestaurant(int numResto);

    /**
     * Permet de creer une reservation pour un restaurant
     *
     * @param nomClient     Nom du client
     * @param prenomClient  Prenom du client
     * @param nbConvives    Nombre de convives
     * @param numTel        Numero de telephone
     * @param numRestaurant Numero du restaurant
     * @param dateRes       Date de la reservation
     * @return Un objet JSON contenant le resultat de la requete
     */
    JSONObject setReservation(String nomClient, String prenomClient, int nbConvives, String numTel, int numRestaurant, String dateRes);

    /**
     * Permet de recuperer les restaurants de la base de donnees
     *
     * @return Un objet JSON contenant le resultat de la requete
     */
    JSONObject getRestaurants();

    /**
     * Permet de recuperer les restaurants proches d'une position GPS
     *
     * @param xGPS Coordonnee GPS en x
     * @param yGPS Coordonnee GPS en y
     * @param rayon Rayon de recherche
     * @return Un objet JSON contenant le resultat de la requete
     */
    JSONObject getRestaurantsByPos(double xGPS, double yGPS, int rayon);

    /**
     * Permet de recuperer les reservations d'un restaurant
     *
     * @param numRestaurant Numero du restaurant
     * @param dateRes       Date de la reservation
     * @return Un objet JSON contenant le resultat de la requete
     */
    JSONObject getRestaurantNbReservations(int numRestaurant, String dateRes);
}
