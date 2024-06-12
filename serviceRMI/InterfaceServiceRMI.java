import java.io.IOException;
import java.rmi.Remote;


public interface InterfaceServiceRMI extends Remote {
    public String lancerRequete() throws IOException, InterruptedException ;
}
