import java.sql.*;

public class ConnectBD {
    private static Connection connect;
    static Connection connection () throws ClassNotFoundException, SQLException {
        if(connect==null){
            // Charger le driver JDBC
            Class.forName("oracle.jdbc.driver.OracleDriver");
            System.out.println("Driver loaded");
            // Se connecter à la base de données
            Connection connection = (Connection) DriverManager.getConnection("jdbc:oracle:thin:@charlemagne.iutnc.univ-lorraine.fr:1521:infodb", "ponroy1u", "pastabox");
            System.out.println("Database connected");
            // Créer une instruction
            //Statement statement =  connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            connect =connection;
        }
        return connect;
    }
}
