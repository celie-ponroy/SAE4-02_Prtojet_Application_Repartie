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

/**
 * ServiceHttp : classe permettant de gérer les requêtes HTTP
 */
public class ServiceHttp {
    InterfaceServiceRMI serviceTrafic;
    InterfaceResto serviceResto;
    HttpServer httpServer;

    static int compteurTrafic = 0;

    static int compteurResto = 0;

    ServiceHttp(InterfaceServiceRMI servT, InterfaceResto serviceR) throws IOException, InterruptedException {
        this.serviceResto = serviceR;
        this.serviceTrafic = servT;
        httpServer = HttpServer.create(new InetSocketAddress("localhost", 8001), 0);
        httpServer.createContext("/trafic",
                new HttpHandler() {
                    @Override
                    public void handle(HttpExchange exchange) throws IOException {
                        handleRequest(exchange, (e) -> demanderServiceTrafic(e));
                    }
                });
        httpServer.createContext("/resto",
                new HttpHandler() {
                    @Override
                    public void handle(HttpExchange exchange) throws IOException {
                        handleRequest(exchange, (e) -> demanderServiceResto(e));
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
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");

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
     * @param exchange
     * @param jsonResponse
     * @throws IOException
     */
    private void sendJsonResponse(HttpExchange exchange, String jsonResponse) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(200, jsonResponse.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(jsonResponse.getBytes());
        os.close();
    }


    /**
     * demanderServiceTrafic : récuperre les données du trafic et les envoi
     * @param exchange
     * @throws IOException
     * @throws InterruptedException
     */
    void demanderServiceTrafic(HttpExchange exchange) throws IOException, InterruptedException {
        String response = this.serviceTrafic.lancerRequete();
        JSONObject obj = new JSONObject(response);
        sendJsonResponse(exchange, obj.toString());
        compteurTrafic++;
        System.out.println("Nombre de demandes de trafic : "+compteurTrafic);
    }

    /**
     * demanderServiceResto : récuperre les données du restaurant et les envoi
     * @param exchange
     * @throws IOException
     * @throws InterruptedException
     */
    void demanderServiceResto(HttpExchange exchange) throws IOException, InterruptedException {
        String response = serviceResto.getRestaurants();
        JSONObject obj = new JSONObject(response);
        sendJsonResponse(exchange, obj.toString());
        compteurResto++;
        System.out.println("Nombre de demandes de resto : "+compteurResto);
    }
}
