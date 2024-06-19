import java.io.IOException;
import java.rmi.Remote;


public interface InterfaceTrafic extends Remote {
    public String lancerRequete() throws IOException, InterruptedException ;
}
