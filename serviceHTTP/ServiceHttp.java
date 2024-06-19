import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.nio.charset.StandardCharsets;

/**
 * ServiceHttp : classe permettant de gérer les requêtes HTTP
 */
public class ServiceHttp {
    InterfaceTrafic serviceTrafic;
    InterfaceResto serviceResto;
    HttpServer httpServer;
    int port = 8001;

    static int compteurTrafic = 0;
    static int compteurResto = 0;

    ServiceHttp(InterfaceTrafic servT, InterfaceResto serviceR) throws IOException, InterruptedException {
        this.serviceResto = serviceR;
        this.serviceTrafic = servT;
        httpServer = HttpServer.create(new InetSocketAddress("127.0.0.1", port), 0);

        // Gestion Trafic

        httpServer.createContext("/trafic",
                new HttpHandler() {
                    @Override
                    public void handle(HttpExchange exchange) throws IOException {
                        handleRequest(exchange, (e) -> demanderServiceTrafic(e));
                    }
                });

        // Gestion Resto

        // GET requests

        httpServer.createContext("/getRestaurants",
                new HttpHandler() {
                    @Override
                    public void handle(HttpExchange exchange) throws IOException {
                        handleRequest(exchange, (e) -> serviceRestoGetRestaurants(e));
                    }
                });
        httpServer.createContext("/getRestaurant",
                new HttpHandler() {
                    @Override
                    public void handle(HttpExchange exchange) throws IOException {
                        String path = exchange.getRequestURI().getPath();
                        String[] parts = path.split("/");
                        if (parts.length > 2) {
                            String id = parts[2];
                            int idInt = Integer.parseInt(id);
                            handleRequest(exchange, (e) -> serviceRestoGetRestaurant(e, idInt));
                        } else {
                            handleRequest(exchange, (e) -> serviceRestoGetRestaurants(e));
                        }
                    }
                });
        httpServer.createContext("/deleteRestaurant",
                new HttpHandler() {
                    @Override
                    public void handle(HttpExchange exchange) throws IOException {
                        String path = exchange.getRequestURI().getPath();
                        String[] parts = path.split("/");
                        if (parts.length > 2) {
                            String id = parts[2];
                            int idInt = Integer.parseInt(id);
                            handleRequest(exchange, (e) -> serviceRestoDeleteRestaurant(e, idInt));
                        } else {
                            handleRequest(exchange, (e) -> serviceRestoGetRestaurants(e));
                        }
                    }
                });

        // POST requests

        httpServer.createContext("/createRestaurant",
                new HttpHandler() {
                    @Override
                    public void handle(HttpExchange exchange) throws IOException {
                        handleRequest(exchange, (e) -> {
                            String body = new String(e.getRequestBody().readAllBytes());
                            JSONObject obj = new JSONObject(body);
                            String nom = obj.getString("nom");
                            String adresse = obj.getString("adresse");
                            int nbPlaces = obj.getInt("nbPlaces");
                            double xGPS = obj.getDouble("xGPS");
                            double yGPS = obj.getDouble("yGPS");
                            serviceRestoCreateRestaurant(e, nom, adresse, nbPlaces, xGPS, yGPS);
                        });
                    }
                });

        httpServer.createContext("/setReservation",
                new HttpHandler() {
                    @Override
                    public void handle(HttpExchange exchange) throws IOException {
                        handleRequest(exchange, (e) -> {
                            String body = new String(e.getRequestBody().readAllBytes());
                            JSONObject obj = new JSONObject(body);
                            String nomClient = obj.getString("nomClient");
                            String prenomClient = obj.getString("prenomClient");
                            int nbConvives = obj.getInt("nbConvives");
                            String numTel = obj.getString("numTel");
                            int numRestaurant = obj.getInt("numRestaurant");
                            String date = obj.getString("date");
                            serviceRestoSetReservation(e, nomClient, prenomClient, nbConvives, numTel, numRestaurant, date);
                        });
                    }
                });

        httpServer.createContext("/getRestaurantNbReservations",
                new HttpHandler() {
                    @Override
                    public void handle(HttpExchange exchange) throws IOException {
                        handleRequest(exchange, (e) -> {
                            String body = new String(e.getRequestBody().readAllBytes());
                            JSONObject obj = new JSONObject(body);
                            int numRestaurant = obj.getInt("numRestaurant");
                            String date = obj.getString("date");
                            serviceRestoGetRestaurantNbReservations(e, numRestaurant, date);
                        });
                    }
                });

        httpServer.start();
    }

    private interface RequestHandler {
        void handle(HttpExchange exchange) throws IOException, InterruptedException;
    }

    private void handleRequest(HttpExchange exchange, RequestHandler handler) throws IOException {
        // Add CORS headers to the response
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");

        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1); // No content for OPTIONS request
            return;
        }

        try {
            handler.handle(exchange);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * sendJsonResponse : envoi d'une réponse JSON à un client HTTP
     *
     * @param exchange
     * @param jsonResponse
     * @throws IOException
     */
    private void sendJsonResponse(HttpExchange exchange, String jsonResponse) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        // Convert jsonResponse to bytes using UTF-8 encoding
        byte[] responseBytes = jsonResponse.getBytes(StandardCharsets.UTF_8);

        // Set response status and headers
        exchange.sendResponseHeaders(200, responseBytes.length);

        // Write the response bytes to the output stream
        OutputStream os = exchange.getResponseBody();
        os.write(responseBytes);
        os.close();
    }


    /**
     * demanderServiceTrafic : récuperre les données du trafic et les envoi
     *
     * @param exchange
     * @throws IOException
     * @throws InterruptedException
     */
    void demanderServiceTrafic(HttpExchange exchange) throws IOException, InterruptedException {
        String response = this.serviceTrafic.lancerRequete();
        JSONObject obj = new JSONObject(response);
        sendJsonResponse(exchange, obj.toString());
        compteurTrafic++;
        System.out.println("Nombre de demandes de trafic : " + compteurTrafic);
    }

    /**
     * serviceRestoGetRestaurants : retourne la liste des restaurants
     *
     * @param exchange
     * @throws IOException
     * @throws InterruptedException
     */
    void serviceRestoGetRestaurants(HttpExchange exchange) throws IOException, InterruptedException {
        String response = serviceResto.getRestaurants();
        JSONObject obj = new JSONObject(response);
        sendJsonResponse(exchange, obj.toString());
        compteurResto++;
        System.out.println("Nombre de demandes de resto : " + compteurResto);
    }

    /**
     * serviceRestoGetRestaurant : retourne un restaurant
     *
     * @param exchange
     * @param id
     * @throws IOException
     */
    void serviceRestoGetRestaurant(HttpExchange exchange, int id) throws IOException, InterruptedException {
        String response = serviceResto.getRestaurant(id);
        JSONObject obj = new JSONObject(response);
        sendJsonResponse(exchange, obj.toString());
        compteurResto++;
        System.out.println("Nombre de demandes de resto : " + compteurResto);
    }

    /**
     * serviceRestoDeleteRestaurant : supprime un restaurant
     *
     * @param exchange
     * @param id
     * @throws IOException
     */
    void serviceRestoDeleteRestaurant(HttpExchange exchange, int id) throws IOException {
        String response = serviceResto.deleteRestaurant(id);
        JSONObject obj = new JSONObject(response);
        sendJsonResponse(exchange, obj.toString());
        compteurResto++;
        System.out.println("Nombre de demandes de resto : " + compteurResto);
    }

    /**
     * serviceRestoGetRestaurantNbReservations : retourne le nombre de réservations d'un restaurant
     *
     * @param exchange
     * @param id
     * @param date
     * @throws IOException
     */
    void serviceRestoGetRestaurantNbReservations(HttpExchange exchange, int id, String date) throws IOException {
        String response = serviceResto.getRestaurantNbReservations(id, date);
        JSONObject obj = new JSONObject(response);
        sendJsonResponse(exchange, obj.toString());
        compteurResto++;
        System.out.println("Nombre de demandes de resto : " + compteurResto);
    }

    /**
     * serviceRestoCreateRestaurant : crée un restaurant
     *
     * @param exchange
     * @param nom
     * @param adresse
     * @param nbPlaces
     * @param xGPS
     * @param yGPS
     * @throws IOException
     */
    void serviceRestoCreateRestaurant(HttpExchange exchange, String nom, String adresse, int nbPlaces, double xGPS, double yGPS) throws IOException {
        String response = serviceResto.createRestaurant(nom, adresse, nbPlaces, xGPS, yGPS);
        JSONObject obj = new JSONObject(response);
        sendJsonResponse(exchange, obj.toString());
        compteurResto++;
        System.out.println("Nombre de demandes de resto : " + compteurResto);
    }

    /**
     * serviceRestoSetReservation : crée une réservation
     *
     * @param exchange
     * @param nomClient
     * @param prenomClient
     * @param nbConvives
     * @param numTel
     * @param numRestaurant
     * @param date
     * @throws IOException
     */
    void serviceRestoSetReservation(HttpExchange exchange, String nomClient, String prenomClient, int nbConvives, String numTel, int numRestaurant, String date) throws IOException {
        String response = serviceResto.setReservation(nomClient, prenomClient, nbConvives, numTel, numRestaurant, date);
        JSONObject obj = new JSONObject(response);
        sendJsonResponse(exchange, obj.toString());
        compteurResto++;
        System.out.println("Nombre de demandes de resto : " + compteurResto);
    }
}