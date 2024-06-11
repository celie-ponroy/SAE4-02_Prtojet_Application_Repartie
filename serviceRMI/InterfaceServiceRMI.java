import java.io.IOException;
import java.net.http.HttpResponse;
import java.rmi.Remote;

public interface InterfaceServiceRMI extends Remote {
    public HttpResponse<String> lancerRequete() throws IOException, InterruptedException ;
}
