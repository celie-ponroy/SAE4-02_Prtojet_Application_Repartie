import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class LancerSerciceRMI {
    public static void main(String[] args) {

        int port = 1099;
        String host = "localhost";
        boolean doProxy = false;
        if (args.length > 0) {
            doProxy = Boolean.parseBoolean(args[0]);
            if (args.length > 1) {
                host = args[1];
                if (args.length > 2)
                    port = Integer.parseInt(args[2]);
            }
        }

        try {
            ServiceRMI service = new ServiceRMI(doProxy);
            InterfaceServiceRMI rd = (InterfaceServiceRMI) UnicastRemoteObject.exportObject(service, 0);

            Registry reg = LocateRegistry.getRegistry(host, port);
            InterfaceService distributeurServices = (InterfaceService) reg.lookup("DistributeurServices");

            distributeurServices.enregistrerDistributeurTrafic(rd);

            System.out.println("Service Trafic pret (connecte a : " + host + " sur le port " + port + ")");
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
        } catch (java.rmi.NotBoundException e) {
            System.out.println("Erreur RMI : " + e.getMessage());
        }
    }
}
