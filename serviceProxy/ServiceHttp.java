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

public class ServiceHttp {
    InterfaceServiceRMI service;
    HttpServer httpServer;
    ServiceHttp(InterfaceServiceRMI serv) throws IOException, InterruptedException{
        this.service=serv;
        httpServer = HttpServer.create(new InetSocketAddress("localhost", 8001), 0);
        httpServer.createContext("/trafic",
                new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) throws IOException {
                    try {
                        demanderServiceTrafic(exchange);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                });
        httpServer.createContext("/resto",
                new HttpHandler() {
                    @Override
                    public void handle(HttpExchange exchange) throws IOException {
                        try {
                            demanderServiceResto(exchange);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void sendJsonResponse(HttpExchange exchange, String jsonResponse) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(200, jsonResponse.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(jsonResponse.getBytes());
        os.close();
    }



    //service resto(/resto) + service trafic (/trafic)
    void demanderServiceTrafic(HttpExchange exchange) throws IOException, InterruptedException {
        String response = this.service.lancerRequete();
        JSONObject obj = new JSONObject(response);
        sendJsonResponse(exchange, obj.toString());
        System.out.println(obj);

        //httpServeur qui utilise ^
        /*
        System.out.println(resp.body());
        System.out.println(resp.headers());
        System.out.println("b::::"+b);
         */
    }
    void demanderServiceResto(HttpExchange exchange) throws IOException, InterruptedException {
        //changer apres merge
        //String response = this.service.lancerRequete();
        //JSONObject obj = new JSONObject(response);
        //System.out.println(obj);

        //httpServeur qui utilise ^
        /*
        System.out.println(resp.body());
        System.out.println(resp.headers());
        System.out.println("b::::"+b);
         */
    }
}
