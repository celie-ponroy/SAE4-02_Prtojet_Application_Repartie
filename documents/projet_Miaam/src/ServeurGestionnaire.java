import java.sql.*;
import java.util.Scanner;

public class ServeurGestionnaire extends Serveur{
    private ServeurGestionnaire(String mail,String nom){
        super(mail,nom);
    }
    public static ServeurGestionnaire connection(String email, String motdepass) throws SQLException, ClassNotFoundException {
        ServeurGestionnaire serv = null;
        Connection connection = ConnectBD.connection();
        connection.createStatement();
        PreparedStatement statement =  connection.prepareStatement("select passwd, nomserv,grade from serveur where email like ? ");
        statement.setString(1,email);
        ResultSet resultSet = statement.executeQuery();


        // Parcourir et afficher les résultas
        ResultSetMetaData rsMetaData = resultSet.getMetaData();

        if(resultSet.next()) {
            System.out.println();
            if(resultSet.getString(1).equals(motdepass)){
                if(resultSet.getString("grade").equals("gestionnaire")){
                    System.out.println("connection établie");
                    serv = new ServeurGestionnaire(email,resultSet.getString("nomserv"));
                }else{
                    System.out.println("Le serveur n'est pas gestionnaire veuillez réesayer en tant que tel");
                }

            }else{
                System.out.println("mot de passe non valide");
            }
        }else{
            System.out.println("email non valide");
        }
        return serv;
    }
    public void consltAffect() throws SQLException, ClassNotFoundException {
        Connection connection = ConnectBD.connection();
        connection.createStatement();
        Statement statement =  connection.createStatement();
        String sql = "SELECT DISTINCT affecter.numtab, serveur.nomserv, serveur.grade from affecter \n" +
                "inner join serveur on affecter.numserv = serveur.numserv\n" +
                "order by affecter.numtab";

        ResultSet resultSet = statement.executeQuery(sql);

        // Parcourir et afficher les résultas
        System.out.println("\nLISTE DES AFFECTATIONS :");
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
    public boolean affecter(int numtab,String date , int numserv) throws SQLException, ClassNotFoundException {
        Connection connection =ConnectBD.connection();
        // '10/09/2021'
            PreparedStatement preparedStatement = connection.prepareStatement("insert into affecter values(?,?,?);");
            preparedStatement.setInt(1, numtab);
            preparedStatement.setString(2, date);
            preparedStatement.setInt(3, numserv);

            // Exécution de la requête d'insertion
        int rows = preparedStatement.executeUpdate();
        return rows>0;
    }
    public double calculMontant(int numres) throws SQLException, ClassNotFoundException {
        Connection connection =ConnectBD.connection();
        String sql = "select sum(plat.prixunit *commande.quantite) from commande inner join plat on plat.numplat = commande.numplat\n" +
                "where numres = ?\n" +
                "group by numres";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, numres);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) {
            System.out.println("Aucune commande sur cette réservation.");
            return 0.0;
        }
        // Récupérer la valeur de la colonne numplat de la première ligne
        int somme = resultSet.getInt(1);

        PreparedStatement update = connection.prepareStatement("update RESERVATION set reservation.montcom = ? where numres = ? ");
        update.setInt(1,somme);//somme
        update.setInt(2,numres);//numres
        update.executeUpdate();
        return somme;
    }
    public void menu(Scanner sc) throws SQLException, ClassNotFoundException {
        boolean end = false;
        System.out.println("tappez 'tables' pour consulter les tables disponibles pour une date et heure données.");
        System.out.println("tappez 'reserver' pour Réserver une table pour une date et heure données");
        System.out.println("tappez 'plats' pour Consulter les plats disponibles pour une éventuelle commande.");
        System.out.println("tappez 'commander' pour Commander des plats.");
        System.out.println("tappez 'affectations' pour Commander des plats.");
        System.out.println("tappez 'affecter' pour Affecter des serveurs à des tables.");
        System.out.println("tappez 'calcul' pour Calculer le montant total d’une réservation consommée et mettre à jour la table RESERVATION pour l’encaissement.\n");

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
                case "affectations":
                    this.consltAffect();
                    break;
                case "affecter":
                    //demander table et num serv et date
                    System.out.print("date (dd/mm/yyyy) : ");
                    String dateaffect = sc.next();//10/09/2021
                    System.out.print("numéro de la table : ");
                    int numtabaffect = sc.nextInt();
                    System.out.print("numéro du serveur : ");
                    int numserv = sc.nextInt();
                    this.affecter(numtabaffect,dateaffect,numserv);
                    break;
                case "calcul":
                    System.out.print("numéro de le reservation : ");
                    int numrescalc = sc.nextInt();//100
                    System.out.println(this.calculMontant(numrescalc));
                    break;
                case "quit":
                    end=true;
                    break;
            }
        }
    }
}
