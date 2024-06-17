import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class LancerServiceHttp {

    public static void main(String[] args) throws IOException, NotBoundException, InterruptedException {
        
        String serveur="localhost";    // par défaut le serveur est sur la même machine
        int port = 1099;                      // le port de la rmiregistry par défaut 
        if(args.length > 0)
            serveur = args[0];
        if(args.length > 1)
            port = Integer.parseInt(args[1]);

        //chercher le proxy Trafic
        Registry reg = LocateRegistry.getRegistry(serveur,port);
        InterfaceServiceRMI objService = (InterfaceServiceRMI) reg.lookup("ServiceTrafic");

        InterfaceResto objService2 = (InterfaceResto) reg.lookup("DistributeurResto");
        //Lancement du service HTTP
        ServiceHttp s = new ServiceHttp(objService, objService2);
    }
}
