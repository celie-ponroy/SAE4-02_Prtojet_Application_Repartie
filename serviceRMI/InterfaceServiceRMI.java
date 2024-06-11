

import java.io.IOException;
import java.net.http.HttpResponse;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceServiceRMI extends Remote {
    public String lancerRequete() throws IOException, InterruptedException ;
}
