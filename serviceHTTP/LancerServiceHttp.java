import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class LancerServiceHttp {

    public static void main(String[] args) throws IOException, NotBoundException, InterruptedException {

        String serveur = "localhost";
        int port = 1099;
        if (args.length > 0) {
            serveur = args[0];
            if (args.length > 1)
                port = Integer.parseInt(args[1]);
        }

        Registry reg = LocateRegistry.getRegistry(serveur, port);
        InterfaceService distributeurServices = (InterfaceService) reg.lookup("DistributeurServices");

        InterfaceResto objService = distributeurServices.demanderDistributeurResto();
        InterfaceTrafic objService2 = distributeurServices.demanderDistributeurTrafic();

        ServiceHttp s = new ServiceHttp(objService2, objService);

        System.out.println("Service HTTP lance sur le port " + s.port + " (connecte a : " + serveur + " sur le port " + port + ")");
    }
}
