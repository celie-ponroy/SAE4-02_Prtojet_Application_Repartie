import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class LancerRMI {
    public static void main(String[] args) {

        String serveur = "localhost";
        int port = 1099;
        if (args.length > 0) {
            serveur = args[0];
            if (args.length > 1)
                port = Integer.parseInt(args[1]);
        }

        try {
            ServiceRMI service = new ServiceRMI();
            InterfaceService rd = (InterfaceService) UnicastRemoteObject.exportObject(service, 0);

            Registry reg = LocateRegistry.getRegistry(serveur, port);
            reg.rebind("DistributeurServices", rd);

            System.out.println("Service RMI lance sur le port " + port + " (connecte a : " + serveur + ")");
        } catch (java.rmi.ConnectException e) {
            System.out.println("Serveur RMI non accessible : " + e.getMessage());
        } catch (java.rmi.UnmarshalException e) {
            System.out.println("L'interface avec laquelle vous communiquez semble incorrecte : " + e.getMessage());
        } catch (java.rmi.ConnectIOException e) {
            System.out.println("La connexion a echoue : " + e.getMessage());
        } catch (java.rmi.AccessException e) {
            System.out.println("Acces refuse : " + e.getMessage());
        } catch (java.rmi.RemoteException e) {
            System.out.println("Erreur RMI : " + e.getMessage());
        }
    }
}