import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceService extends Remote {
    public void enregistrerDistributeurResto(InterfaceResto distributeurResto) throws RemoteException;

    public void enregistrerDistributeurTrafic(InterfaceTrafic distributeurTrafic) throws RemoteException;

    public InterfaceResto demanderDistributeurResto() throws RemoteException;

    public InterfaceTrafic demanderDistributeurTrafic() throws RemoteException;
}