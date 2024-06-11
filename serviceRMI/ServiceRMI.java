import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

public class ServiceRMI{
    public static URI uri;

    ServiceRMI() throws URISyntaxException {
        uri = new URI("https://carto.g-ny.org/data/cifs/cifs_waze_v2.json");
    }
    public static void main(String[] args) {
        /* 
        System.out.println("debut");
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("https://carto.g-ny.org/data/cifs/cifs_waze_v2.json"))
        .build();
        client.sendAsync(request, 
        BodyHandlers.ofFile(Paths.get("/home/celie/Documents/s4/Progrépartie-Archilogiciel/SAE/SAE4-02_Prtojet_Application_Repartie/serviceRMI/test.txt")))
        .thenApply(HttpResponse::body)
        .thenAccept(System.out::println);
        System.out.println("fin");
        */
        /*(fonctionne) 
        System.out.println("debut");

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://carto.g-ny.org/data/cifs/cifs_waze_v2.json"))
            .build();

        CountDownLatch latch = new CountDownLatch(1);

        client.sendAsync(request, BodyHandlers.ofFile(Paths.get("/home/celie/Documents/s4/Progrépartie-Archilogiciel/SAE/SAE4-02_Prtojet_Application_Repartie/serviceRMI/test.txt")))
            .thenApply(HttpResponse::body)
            .thenAccept(path -> {
                System.out.println("Fichier écrit à : " + path);
                latch.countDown(); // Réduire le compteur lorsqu'il est terminé
            })
            .exceptionally(e -> {
                e.printStackTrace();
                latch.countDown(); // Réduire le compteur en cas d'exception
                return null;
            });

        try {
            latch.await(); // Attendre que la tâche asynchrone soit terminée
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("fin");*/
        System.out.println("debut");

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://carto.g-ny.org/data/cifs/cifs_waze_v2.json"))
            .build();

        client.sendAsync(request, BodyHandlers.ofFile(Paths.get("/home/celie/Documents/s4/Progrépartie-Archilogiciel/SAE/SAE4-02_Prtojet_Application_Repartie/serviceRMI/test.txt")))
            .thenApply(HttpResponse::body)
            .thenAccept(path -> {
                System.out.println("Fichier écrit à : " + path);
            });

            System.out.println("fin");

/* 
        HttpClient client = HttpClient.newHttpClient();
        client.send(HttpRequest.newBuilder(uri),new BodyHandler.asString());*/
    }

    //creer un http client
    // fetch https://carto.g-ny.org/data/cifs/cifs_waze_v2.json
    //récupère les données et les redonne 

}
