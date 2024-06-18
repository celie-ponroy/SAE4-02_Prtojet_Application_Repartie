import java.net.URI;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.io.IOException;


public class ServiceRMI implements InterfaceServiceRMI {
    /**
     * Methode qui permet de lancer une requete pour acceder a Info Trafic et la reourne
     */
    public String lancerRequete() throws IOException, InterruptedException {
        System.out.println("Debut requete Info Trafic");

        // creer un http client
        HttpClient client = HttpClient.newBuilder()
                .proxy(ProxySelector.of(new InetSocketAddress("www-cache.iutnc.univ-lorraine.fr", 3128)))
                .build();

        // faire une requete sur l'api
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://carto.g-ny.org/data/cifs/cifs_waze_v2.json"))
                .build();

        // lancer la requete et recuperer la reponse
        HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

        // Convertir les octets en String en utilisant l'encodage UTF-8
        String responseBody = new String(response.body(), StandardCharsets.UTF_8);

        System.out.println("Fin de requete Info Trafic, renvoi du resultat :");
        System.out.println(responseBody);

        return responseBody;
    }
}
