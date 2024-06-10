import java.sql.*;
import java.util.Scanner;

public class Serveur {
    private String email;
    private String nomserv;
    protected Serveur(String mail,String nom ){
        this.email = mail;
        this.nomserv = nom;
    }
    public static Serveur connection(String email, String motdepass) throws SQLException, ClassNotFoundException {
        Serveur serv = null;
        Connection connection = ConnectBD.connection();
        connection.createStatement();
        PreparedStatement statement =  connection.prepareStatement("select passwd, nomserv,grade from serveur where email like ? ");
        statement.setString(1,email);
        ResultSet resultSet = statement.executeQuery();


        // Parcourir et afficher les résultats
        ResultSetMetaData rsMetaData = resultSet.getMetaData();

        if(resultSet.next()) {
            System.out.println();
            if(resultSet.getString(1).equals(motdepass)){
                System.out.println("connection établie");
                serv = new Serveur(email,resultSet.getString("nomserv"));
            }else{
                System.out.println("mot de passe non valide");
            }
        }else{
            System.out.println("email non valide");
        }
        return serv;
    }

    //a. Consulter les tables disponibles pour une date et heure données.
    //'dd/mm/yyyy' format de la date
    //heure au format 24h
    public void consultTables(String date,String heure) throws SQLException, ClassNotFoundException {
        Connection connection = ConnectBD.connection();
        connection.createStatement();
        PreparedStatement statement =  connection.prepareStatement(" SELECT numtab,tabl.nbplace\n" +
                "    FROM tabl WHERE numtab NOT IN(\n" +
                "        SELECT numtab\n" +
                "        FROM reservation \n" +
                "        WHERE to_char(datres, 'dd/mm/yyyy')LIKE ? AND to_char(datres, 'HH24') LIKE ?)");
        statement.setString(1,date);
        statement.setString(2,heure);
        ResultSet resultSet = statement.executeQuery();

        // Parcourir et afficher les résultas
        System.out.println("\nLISTE DES TABLES DISPONIBLES LE "+ date +":");
        ResultSetMetaData rsMetaData = resultSet.getMetaData();
        for (int i = 1; i <= rsMetaData.getColumnCount(); i++)
            System.out.printf("%-12s\t", rsMetaData.getColumnName(i));
        System.out.println();

        while (resultSet.next()) {
            for (int i = 1; i <= rsMetaData.getColumnCount(); i++)
                System.out.printf("%-12s\t", resultSet.getObject(i));
            System.out.println();
        }
    }

    //b. Réserver une table pour une date et heure données
    //format dateHeure = 'dd/mm/yyyy hh24:mi')
    public boolean reserver(String dateHeure,int nbTable,int nbPers) throws SQLException, ClassNotFoundException {//10/09/2021 19:00
        //insert into reservation values(100,10,to_date('10/09/2021 19:00','dd/mm/yyyy hh24:mi'),2,to_date('10/09/2021 20:50','dd/mm/yyyy hh24:mi'),'Carte',null);
        Connection connection =ConnectBD.connection();
        PreparedStatement preparedStatement = connection.prepareStatement("insert into reservation(numtab, datres,nbpers) values(?,to_date(?,'dd/mm/yyyy hh24:mi'),?)");

        preparedStatement.setInt(1,nbTable);
        preparedStatement.setString(2, dateHeure);
        preparedStatement.setInt(3,nbPers);
        //preparedStatement.setString(4, dateHeure);

        int rowsAffected = preparedStatement.executeUpdate();
        return rowsAffected>0;

    }
    //c. Consulter les plats disponibles pour une éventuelle commande.
    public void platDispo() throws SQLException, ClassNotFoundException {
        Connection connection = ConnectBD.connection();

        //for each si qte plats >0
        String sql = "SELECT * FROM plat";
        Statement statement =  connection.createStatement();

        ResultSet resultSet = statement.executeQuery(sql);

        // Parcourir et afficher les résultas
        System.out.println("\nLISTE DES PLATS DISPONIBLES:");
        ResultSetMetaData rsMetaData = resultSet.getMetaData();
        for (int i = 1; i <= rsMetaData.getColumnCount(); i++)
            System.out.printf("%-12s\t", rsMetaData.getColumnName(i));
        System.out.println();

        while (resultSet.next()) {
            if(resultSet.getInt("qteservie")>0) {
                for (int i = 1; i <= rsMetaData.getColumnCount(); i++)
                    System.out.printf("%-12s\t", resultSet.getObject(i));
                System.out.println();
            }
        }
    }
    public boolean commander(int numres, String libelle, int qte) throws SQLException, ClassNotFoundException {
        Connection connection =ConnectBD.connection();
        //récuperrer le numplat de libelle
        PreparedStatement statement =  connection.prepareStatement(" SELECT numplat from plat where libelle like ?");
        statement.setString(1,libelle);
        ResultSet resultSet = statement.executeQuery();
        if (!resultSet.next()) {
            System.out.println("Aucun plat n'as ce nom.");
            return false;
        }
        // Récupérer la valeur de la colonne numplat de la première ligne
        int numplat = resultSet.getInt("numplat");
        //vérifier res
        PreparedStatement statement2 =  connection.prepareStatement(" SELECT * from reservation where numres like ?");
        statement2.setInt(1,numres);
        ResultSet resultSet2 = statement2.executeQuery();
        if (!resultSet2.next()) {
            System.out.println("Numéro de réservation invalide.");
            return false;
        }
        //update ou insert
        PreparedStatement preparedStatement = null;
        int rows =0;
        try{
            preparedStatement = connection.prepareStatement("INSERT INTO commande (numres, numplat, quantite) VALUES (?, ?, ?)");
            preparedStatement.setInt(1, numres);
            preparedStatement.setInt(2, numplat);
            preparedStatement.setInt(3, qte);

            // Exécution de la requête d'insertion
            rows = preparedStatement.executeUpdate();
        }catch (java.sql.SQLIntegrityConstraintViolationException e){
            //cas d'un update, on modifie la quantité
            preparedStatement = connection.prepareStatement("UPDATE commande\n" +
                    "SET quantite = ?" +
                    "WHERE numres = ? and numplat = ?");

            preparedStatement.setInt(1,qte);
            preparedStatement.setInt(2,numres);
            preparedStatement.setInt(3,numplat);
            // Exécution de la requête d'insertion
            rows = preparedStatement.executeUpdate();
        }

        return rows>0;


    }

    public void menu(Scanner sc) throws SQLException, ClassNotFoundException {
        boolean end = false;
        System.out.println("tappez 'tables' pour consulter les tables disponibles pour une date et heure données.");
        System.out.println("tappez 'reserver' pour Réserver une table pour une date et heure données");
        System.out.println("tappez 'plats' pour Consulter les plats disponibles pour une éventuelle commande.");
        System.out.println("tappez 'commander' pour Commander des plats.");

        System.out.println("tappez 'quit' pour quitter");

        while(!end){
            String action= sc.next();
            switch (action){
                case "tables":
                    //demander date et heure
                    System.out.print("date (dd/mm/yyyy): ");
                    String date = sc.next();//10/09/2021
                    System.out.print("heure (hh)(format 24h): ");
                    String heure = sc.next();//10/09/2021

                    this.consultTables(date,heure);
                    break;
                case"reserver":
                    //demander dateheure , table et nbpers
                    System.out.print("date (dd/mm/yyyy hh:mi):");
                    String dateheure = sc.next();//10/09/2021 19:00
                    System.out.print("numéro de la table:");
                    int numtab = sc.nextInt();
                    System.out.print("nombre de personnes:");
                    int nbpers = sc.nextInt();
                    System.out.println(this.reserver(dateheure,numtab,nbpers));
                    break;

                case "plats":
                    this.platDispo();
                    break;
                case "commander":
                    System.out.print("numéro de reservation:");
                    int numres = sc.nextInt();
                    System.out.print("libellé du plat:");
                    String lib = sc.next();//lib
                    System.out.print("quantité:");
                    int qte = sc.nextInt();
                    System.out.println(this.commander(numres,lib,qte));
                    break;
                case "quit":
                    end=true;
                    break;
            }
        }
    }


    @Override
    public String toString() {
        return "Serveur{" +
                "email='" + email + '\'' +
                ", nomserv='" + nomserv + '\'' +
                '}';
    }
}
