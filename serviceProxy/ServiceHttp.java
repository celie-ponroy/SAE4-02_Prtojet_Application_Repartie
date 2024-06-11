import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServiceHttp {
    InterfaceServiceRMI service;
    HttpServer httpServer;
    ServiceHttp(InterfaceServiceRMI serv) throws IOException {
        this.service=serv;
        httpServer = HttpServer.create(new InetSocketAddress("localhost", 8001), 0);
        httpServer.createContext("/trafic",
                new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) throws IOException {
                    demanderServiceTrafic();
                }
                });
    }
    //service resto(/resto) + service trafic (/trafic)
    void demanderServiceTrafic(){
        JSONObject requeteIncident = this.service.lancerRequete();
        System.out.println(requeteIncident);

        //httpServeur qui utilise ^
        /*
        System.out.println(resp.body());
        System.out.println(resp.headers());
        System.out.println("b::::"+b);
         */
    }

    public static void main(String[] args) throws RemoteException, NotBoundException {
        //chercher le proxy
        /* se connecter à l'annuaire de la machine */
        Registry reg = LocateRegistry.getRegistry("localhost",1099);

        /* Récupération de la référence distante */
        /* le retour de la méthode est casté sur l'interface de Service "InterfaceService" */
        InterfaceServiceRMI objService = (InterfaceServiceRMI) reg.lookup("ServiceTrafic");

        /* Appel de la méthode distante */
        //lancer le service

        //service

    }
}
