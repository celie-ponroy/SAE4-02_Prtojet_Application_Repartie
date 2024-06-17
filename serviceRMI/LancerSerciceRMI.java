import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class LancerSerciceRMI {
    public static void main(String[] args) throws IOException, RemoteException {
        int port = 1099;
        String host = "localhost";
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
            if (args.length > 1)
                host = args[1];
        }
        //lancer le Service RMI
        ServiceRMI service = new ServiceRMI(); // Créer une instance de Compteur
        InterfaceServiceRMI rd = (InterfaceServiceRMI) UnicastRemoteObject.exportObject(service, 0);
        //Un_port = un entier particulier ou 0 pour auto-assigné

        Registry reg = LocateRegistry.getRegistry(host, port); //Récupération/Création de l'annuaire
        reg.rebind("ServiceTrafic", rd);//Enregistrement de la référence sous le nom "CompteurDistant"

        System.out.println("Service RMI lance sur le port " + port + " (connecte a : " + host + ")");
    }
}
