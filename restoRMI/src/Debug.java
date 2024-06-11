import org.json.JSONObject;

import java.sql.SQLException;

public class Debug {
    public static void main(String[] args) throws SQLException {
        Serveur serv = new Serveur();
        JSONObject add = serv.createRestaurant("test", "test", 10, 6.2, 49.2);
        System.out.println(add);
        JSONObject res = serv.getRestaurants();
        System.out.println(res);
    }
}
