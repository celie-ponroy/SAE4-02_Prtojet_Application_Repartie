import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Scanner sc = new Scanner(System.in);

        System.out.println("Bienvenue sur l'application Miaam!");
        System.out.println("Avant toutes choses veillez vous connecter:");
        Serveur serv =  connection(sc);
        if(serv!=null){
            serv.menu(sc);
        }else{
            System.out.println("La connection a échoué veuillez relancer l'application");
            sc.close();
        }
    }
    private static Serveur connection(Scanner sc) throws SQLException, ClassNotFoundException {
        System.out.println("veillez saisir votre status:\n1-gestionnaire \n2-serveur");
        int i = sc.nextInt();
        System.out.print("email:");
        String email = sc.next();//user1@mail.com
        System.out.print("password:");
        String mdp = sc.next();//$#;¿¿m$$$$$0
        if(i==2){
            System.out.println("serveur");
            return Serveur.connection(email,mdp);

            //pour simplifier voici des valeurs par défaut
            //return Serveur.connection("user1@mail.com","$#;¿¿m$$$$$0");
        }
        if(i==1){
            System.out.println("gestionnaire");
            return ServeurGestionnaire.connection(email,mdp);
            //pour simplifier voici des valeurs par défaut
            //return ServeurGestionnaire.connection("user1@mail.com","$#;¿¿m$$$$$0");
        }

        return null;
    }

}