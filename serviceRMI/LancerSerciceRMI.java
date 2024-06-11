import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class LancerSerciceRMI {
    public static void main(String[] args) throws IOException {

        //lancer le Service RMI
        InterfaceServiceRMI service = new ServiceRMI(); // Créer une instance de Compteur
        InterfaceServiceRMI rd = (InterfaceServiceRMI) UnicastRemoteObject.exportObject(service, 0);
        //Un_port = un entier particulier ou 0 pour auto-assigné

        Registry reg = LocateRegistry.getRegistry(1099); //Récupération/Création de l'annuaire
        reg.rebind("ServiceTrafic", rd);//Enregistrement de la référence sous le nom "CompteurDistant"

        //new ServiceRMI().lancerRequete();

    }
}
