import java.rmi.AccessException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;

public class Debug {
    public static void main(String[] args) throws SQLException {
        String serveur = "localhost";
        int port = 1099;
        if (args.length > 0)
            serveur = args[0];
        if (args.length > 1)
            port = Integer.parseInt(args[1]);

        try {
            Registry reg = LocateRegistry.getRegistry(serveur, port);

            InterfaceResto rd = (InterfaceResto) reg.lookup("DistributeurResto");

            System.out.println(rd.getRestaurants());
        }
        catch (java.rmi.ConnectException e) {
            System.out.println("Connexion impossible : " + e.getMessage());
        }
        catch (java.rmi.NotBoundException e) {
            System.out.println("Le service distant n'existe pas : " + e.getMessage());
        }
        catch (java.rmi.UnmarshalException e) {
            System.out.println("L'interface avec laquelle vous communiquez semble incorrecte : " + e.getMessage());
        } catch (AccessException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
