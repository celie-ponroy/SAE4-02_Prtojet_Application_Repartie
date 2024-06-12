import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class is used to manage the connection to the database.
 */
public class DBConnection {

    /**
     * The unique instance of the connection to the database.
     */
    private static Connection instance;

    /**
     * Constructor for the DBConnection class.
     *
     * @throws SQLException           If an error occurs while connecting to the database.
     * @throws ClassNotFoundException If the Oracle JDBC driver is not found.
     */
    DBConnection() throws SQLException, ClassNotFoundException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection connection = DriverManager.getConnection(Credentials.APIENDPOINT, Credentials.USERNAME, Credentials.PASSWORD);
        connection.setAutoCommit(false);
        instance = connection;
        System.out.println("Connected to the database");
    }

    /**
     * This method is used to get the unique instance of the connection to the database.
     *
     * @return The unique instance of the connection to the database.
     */
    public static synchronized Connection getInstance() {
        if (instance == null) {
            try {
                new DBConnection();
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    /**
     * This method is used to close the connection to the database.
     *
     * @throws SQLException If an error occurs while closing the connection to the database.
     */
    public static void close() throws SQLException {
        getInstance().close();
    }

}
