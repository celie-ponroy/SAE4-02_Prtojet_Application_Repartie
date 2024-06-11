import org.json.JSONObject;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.rmi.Remote;

public interface InterfaceServiceRMI extends Remote {
    public JSONObject lancerRequete() throws IOException, InterruptedException ;
}
