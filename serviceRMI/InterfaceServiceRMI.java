import java.io.IOException;
import java.rmi.Remote;


public interface InterfaceServiceRMI extends Remote {
    public String lancerRequete(String url) throws IOException, InterruptedException ;
}
