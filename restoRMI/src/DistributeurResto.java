import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

public class DistributeurResto {
    public static void main(String[] args) {

        int port = 1099;
        String host = "localhost";
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
            if (args.length > 1) {
                host = args[1];
            }
        }

        try {
            Serveur distributeur = new Serveur();
            InterfaceResto rd = (InterfaceResto) UnicastRemoteObject.exportObject(distributeur, 0);

            Registry reg = LocateRegistry.getRegistry(host, port);
            reg.rebind("DistributeurResto", rd);

            System.out.println("Serveur RestoRMI pret (connecte a : + " + host + " sur le port " + port + ")");
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
