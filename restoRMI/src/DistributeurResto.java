import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class DistributeurResto {
    public static void main(String[] args) {

        int port = 1099;
        String host = "localhost";
        if (args.length > 0) {
            host = args[1];
            if (args.length > 1)
                port = Integer.parseInt(args[0]);
        }

        try {
            InterfaceResto service = new Serveur();
            InterfaceResto rd = (InterfaceResto) UnicastRemoteObject.exportObject(service, 0);

            Registry reg = LocateRegistry.getRegistry(host, port);
            InterfaceService distributeurServices = (InterfaceService) reg.lookup("DistributeurServices");

            distributeurServices.enregistrerDistributeurResto(rd);

            System.out.println("Service RestoRMI pret (connecte a : " + host + " sur le port " + port + ")");
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
