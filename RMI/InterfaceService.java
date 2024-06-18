import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceService extends Remote {
    public void enregistrerDistributeurResto(InterfaceResto distributeurResto) throws RemoteException;

    public void enregistrerDistributeurTrafic(InterfaceServiceRMI distributeurTrafic) throws RemoteException;

    public InterfaceResto demanderDistributeurResto() throws RemoteException;

    public InterfaceServiceRMI demanderDistributeurTrafic() throws RemoteException;
}