import org.json.JSONObject;

import java.sql.SQLException;

public class Debug {
    public static void main(String[] args) throws SQLException {
        Serveur serv = new Serveur();
        JSONObject res = serv.platDispo();
        System.out.println(res);
    }
}
