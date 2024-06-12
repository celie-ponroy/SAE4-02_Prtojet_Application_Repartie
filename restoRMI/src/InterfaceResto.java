import org.json.JSONObject;

import java.rmi.Remote;
import java.rmi.RemoteException;

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
    String createRestaurant(String nomResto, String adresse, int nbPlaces, double xGPS, double yGPS) throws RemoteException;

    /**
     * Permet de supprimer un restaurant de la base de donnees
     * @param numResto Numero du restaurant
     *                 @return Un objet JSON contenant le resultat de la requete
     */
    String deleteRestaurant(int numResto) throws RemoteException;

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
    String setReservation(String nomClient, String prenomClient, int nbConvives, String numTel, int numRestaurant, String dateRes) throws RemoteException;

    /**
     * Permet de recuperer les restaurants de la base de donnees
     *
     * @return Un objet JSON contenant le resultat de la requete
     */
    String getRestaurants() throws RemoteException;

    /**
     * Permet de recuperer les restaurants proches d'une position GPS
     *
     * @param xGPS Coordonnee GPS en x
     * @param yGPS Coordonnee GPS en y
     * @param rayon Rayon de recherche
     * @return Un objet JSON contenant le resultat de la requete
     */
    String getRestaurantsByPos(double xGPS, double yGPS, int rayon) throws RemoteException;

    /**
     * Permet de recuperer les reservations d'un restaurant
     *
     * @param numRestaurant Numero du restaurant
     * @param dateRes       Date de la reservation
     * @return Un objet JSON contenant le resultat de la requete
     */
    String getRestaurantNbReservations(int numRestaurant, String dateRes) throws RemoteException;
}
