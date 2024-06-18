

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;



public class ServiceRMI implements InterfaceServiceRMI {
    /**
     * Methode qui permet de lancer une requete pour acceder a Info Trafic et la reourne
     */
    public String lancerRequete() throws IOException, InterruptedException {//https://carto.g-ny.org/data/cifs/cifs_waze_v2.json

        //creer un http client
        HttpClient client = HttpClient.newHttpClient();
        //faire une requete sur l'api
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://carto.g-ny.org/data/cifs/cifs_waze_v2.json"))
            .build();
        // lancer la requete et recuperer la reponse
        HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

        // Convertir les octets en String en utilisant l'encodage UTF-8
        String responseBody = new String(response.body(), StandardCharsets.UTF_8);

        System.out.println("envoi du json:");
        System.out.println(responseBody);

        return responseBody;
    }
}
