public class ServiceRMI implements InterfaceService {

    InterfaceResto distributeurResto;
    InterfaceServiceRMI distributeurTrafic;

    public void enregistrerDistributeurResto(InterfaceResto distributeurResto) {
        this.distributeurResto = distributeurResto;
    }

    public void enregistrerDistributeurTrafic(InterfaceServiceRMI distributeurTrafic) {
        this.distributeurTrafic = distributeurTrafic;
    }

    public InterfaceResto demanderDistributeurResto() {
        return distributeurResto;
    }

    public InterfaceServiceRMI demanderDistributeurTrafic() {
        return distributeurTrafic;
    }

}