import org.json.HTTP;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.rmi.Remote;


public class ServiceRMI implements InterfaceServiceRMI {
    public JSONObject lancerRequete() throws IOException, InterruptedException {
        System.out.println("debut");
        
        //creer un http client
        HttpClient client = HttpClient.newHttpClient();
        //faire une requete sur l'api
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://carto.g-ny.org/data/cifs/cifs_waze_v2.json"))
            .build();
        //lancer la requete et recuperer la reponse
        HttpResponse<String> resp = client.send(request, BodyHandlers.ofString());

        System.out.println(resp.body());
        //on créé le json pour le return
        JSONObject json = new JSONObject(resp.body());
        System.out.println(json);
        return json;
    }
}
