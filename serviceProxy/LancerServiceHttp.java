import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class LancerServiceHttp {

    public static void main(String[] args) throws IOException, NotBoundException, InterruptedException {
        //chercher le proxy
        /* se connecter à l'annuaire de la machine */
        Registry reg = LocateRegistry.getRegistry("localhost",1099);

        /* Récupération de la référence distante */
        /* le retour de la méthode est casté sur l'interface de Service "InterfaceService" */
        InterfaceServiceRMI objService = (InterfaceServiceRMI) reg.lookup("ServiceTrafic");

        ServiceHttp s = new ServiceHttp(objService);
        s.httpServer.start();
        /* Appel de la méthode distante */
        //lancer le service

        //service

    }
}
