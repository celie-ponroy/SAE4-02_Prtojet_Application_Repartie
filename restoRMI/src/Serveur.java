import java.sql.*;

import org.json.JSONArray;
import org.json.JSONObject;

public class Serveur implements InterfaceResto {

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
    public String createRestaurant(String nomResto, String adresse, int nbPlaces, double xGPS, double yGPS) {
        JSONObject res = new JSONObject();
        try {
            PreparedStatement statement = DBConnection.getInstance().prepareStatement("""
                    INSERT INTO RESTAURANT (NOMRESTO, ADRESSE, NBPLACES, XGPS, YGPS) VALUES (?, ?, ?, ?, ?)
                    """);

            statement.setString(1, nomResto);
            statement.setString(2, adresse);
            statement.setInt(3, nbPlaces);
            statement.setDouble(4, xGPS);
            statement.setDouble(5, yGPS);

            if (!(49.100 < xGPS && xGPS < 49.300 && 6.100 < yGPS && yGPS < 6.300)) {
                res.put("error", "Coordinates are not in the allowed range");
            }

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                res.put("success", "true");
                DBConnection.getInstance().commit();
            } else {
                res.put("success", "false");
            }
            res.put("error", "");
            return res.toString();
        } catch (SQLException e) {
            e.printStackTrace();
            return res.put("success", "false").put("error", e.getMessage()).toString();
        }
    }

    /**
     * Permet de supprimer un restaurant de la base de donnees
     *
     * @param numResto Numero du restaurant
     * @return Un objet JSON contenant le resultat de la requete
     */
    public String deleteRestaurant(int numResto) {
        JSONObject res = new JSONObject();
        try {
            PreparedStatement statement = DBConnection.getInstance().prepareStatement("""
                    DELETE FROM RESTAURANT WHERE NUMRESTO = ?
                    """);

            statement.setInt(1, numResto);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                res.put("success", "true");
                DBConnection.getInstance().commit();
            } else {
                res.put("success", "false");
            }
            res.put("error", "");
            return res.toString();
        } catch (SQLException e) {
            e.printStackTrace();
            return res.put("success", "false").put("error", e.getMessage()).toString();
        }
    }

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
    public String setReservation(String nomClient, String prenomClient, int nbConvives, String numTel, int numRestaurant, String dateRes) {
        JSONObject res = new JSONObject();
        try {
            JSONObject restaurantReservations = new JSONObject(this.getRestaurantNbReservations(numRestaurant, dateRes));

            if (!restaurantReservations.getString("success").equals("true")) {
                return res.put("success", "false").put("error", restaurantReservations.getString("error")).toString();
            }

            int totalReservations = restaurantReservations.getInt("nbReservations");
            int nbPlaces = restaurantReservations.getInt("nbPlaces");

            if (totalReservations + nbConvives > nbPlaces) {
                return res.put("success", "false").put("error", "Not enough places in the restaurant").toString();
            }

            PreparedStatement statement = DBConnection.getInstance().prepareStatement("""
                    INSERT INTO RESERVATION (NUMRESTO, NOMCLIENT, PRENOMCLIENT, NBCONVIVES, NUMTEL, DATERES) VALUES (?, ?, ?, ?, ?, TO_DATE(?, 'DD/MM/YYYY HH24:MI'))
                    """);

            statement.setInt(1, numRestaurant);
            statement.setString(2, nomClient);
            statement.setString(3, prenomClient);
            statement.setInt(4, nbConvives);
            statement.setString(5, numTel);
            statement.setString(6, dateRes);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                res.put("success", "true");
                DBConnection.getInstance().commit();
            } else {
                res.put("success", "false");
            }
            res.put("error", "");
            return res.toString();
        } catch (SQLException e) {
            e.printStackTrace();
            return res.put("success", "false").put("error", e.getMessage()).toString();
        }
    }

    /**
     * Permet de recuperer les restaurants de la base de donnees
     *
     * @return Un objet JSON contenant le resultat de la requete
     */
    public String getRestaurants() {
        JSONObject res = new JSONObject();
        try {
            Statement statement = DBConnection.getInstance().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM RESTAURANT");

            JSONArray restaurants = new JSONArray();
            while (resultSet.next()) {
                restaurants.put(new JSONObject()
                        .put("numResto", resultSet.getInt("numResto"))
                        .put("nomResto", resultSet.getString("nomResto"))
                        .put("adresse", resultSet.getString("adresse"))
                        .put("xGPS", resultSet.getString("xgps"))
                        .put("yGPS", resultSet.getString("ygps"))
                        .put("nbPlaces", resultSet.getInt("nbPlaces"))
                );
                res.put("restaurants", restaurants);
            }
            if (restaurants.length() == 0) {
                res.put("success", "false");
            } else {
                res.put("success", "true");
            }
            res.put("error", "");
            return res.toString();
        } catch (SQLException e) {
            e.printStackTrace();
            return res.put("success", "false").put("error", e.getMessage()).toString();
        }
    }

    /**
     * Permet de recuperer les restaurants proches d'une position GPS
     *
     * @param xGPS  Coordonnee GPS en x
     * @param yGPS  Coordonnee GPS en y
     * @param rayon Rayon de recherche
     * @return Un objet JSON contenant le resultat de la requete
     */
    public String getRestaurantsByPos(double xGPS, double yGPS, int rayon) {
        JSONObject res = new JSONObject();
        try {
            PreparedStatement statement = DBConnection.getInstance().prepareStatement("""
                    SELECT * FROM RESTAURANT
                        WHERE SQRT(POWER(xGPS - ?, 2) + POWER(yGPS - ?, 2)) < ?
                    """);

            statement.setDouble(1, xGPS);
            statement.setDouble(2, yGPS);
            statement.setInt(3, rayon);

            ResultSet resultSet = statement.executeQuery();

            JSONArray restaurants = new JSONArray();
            while (resultSet.next()) {
                restaurants.put(new JSONObject()
                        .put("numResto", resultSet.getInt("numResto"))
                        .put("nomResto", resultSet.getString("nomResto"))
                        .put("adresse", resultSet.getString("adresse"))
                        .put("xGPS", resultSet.getString("xgps"))
                        .put("yGPS", resultSet.getString("ygps"))
                );
                res.put("restaurants", restaurants);
            }
            if (restaurants.length() == 0) {
                res.put("success", "false");
            } else {
                res.put("success", "true");
            }
            res.put("error", "");
            return res.toString();
        } catch (SQLException e) {
            e.printStackTrace();
            return res.put("success", "false").put("error", e.getMessage()).toString();
        }
    }

    /**
     * Permet de recuperer les informations d'un restaurant
     *
     * @param numRestaurant Numero du restaurant
     */
    public String getRestaurant(int numRestaurant) {
        JSONObject res = new JSONObject();
        try {
            PreparedStatement statement = DBConnection.getInstance().prepareStatement("""
                    SELECT * FROM RESTAURANT WHERE NUMRESTO = ?
                    """);

            statement.setInt(1, numRestaurant);

            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                return res.put("success", "false").put("error", "Restaurant does not exist").toString();
            }

            res.put("numResto", resultSet.getInt("numResto"));
            res.put("nomResto", resultSet.getString("nomResto"));
            res.put("adresse", resultSet.getString("adresse"));
            res.put("xGPS", resultSet.getString("xGPS"));
            res.put("yGPS", resultSet.getString("yGPS"));
            res.put("nbPlaces", resultSet.getInt("nbPlaces"));
            res.put("success", "true");

            return res.toString();
        } catch (SQLException e) {
            e.printStackTrace();
            return res.put("success", "false").put("error", e.getMessage()).toString();
        }
    }

    /**
     * Permet de recuperer les reservations d'un restaurant
     *
     * @param numRestaurant Numero du restaurant
     * @param dateRes       Date de la reservation
     * @return Un objet JSON contenant le resultat de la requete
     */
    public String getRestaurantNbReservations(int numRestaurant, String dateRes) {
        JSONObject res = new JSONObject();
        try {
            PreparedStatement statementCheck = DBConnection.getInstance().prepareStatement("""
                    SELECT NBPLACES FROM RESTAURANT WHERE NUMRESTO = ?
                    """);

            statementCheck.setInt(1, numRestaurant);

            ResultSet resultSet = statementCheck.executeQuery();
            if (!resultSet.next()) {
                return res.put("success", "false").put("error", "Restaurant does not exist").toString();
            }

            int nbPlaces = resultSet.getInt("NBPLACES");
            PreparedStatement statementCheckReservations = DBConnection.getInstance().prepareStatement("""
                    SELECT SUM(NBCONVIVES) AS TOTAL FROM RESERVATION WHERE NUMRESTO = ? AND DATERES = TO_DATE(?, 'DD/MM/YYYY HH24:MI')
                    """);

            statementCheckReservations.setInt(1, numRestaurant);
            statementCheckReservations.setString(2, dateRes);

            ResultSet resultSetReservations = statementCheckReservations.executeQuery();

            int totalReservations = resultSetReservations.getInt("TOTAL");

            res.put("nbReservations", totalReservations);
            res.put("nbPlaces", nbPlaces);
            res.put("success", "true");

            return res.toString();
        } catch (SQLException e) {
            e.printStackTrace();
            return res.put("success", "false").put("error", e.getMessage()).toString();
        }
    }
}
