public class ServiceRMI implements InterfaceService {

    InterfaceResto distributeurResto;
    InterfaceTrafic distributeurTrafic;

    public void enregistrerDistributeurResto(InterfaceResto distributeurResto) {
        this.distributeurResto = distributeurResto;
    }

    public void enregistrerDistributeurTrafic(InterfaceTrafic distributeurTrafic) {
        this.distributeurTrafic = distributeurTrafic;
    }

    public InterfaceResto demanderDistributeurResto() {
        return distributeurResto;
    }

    public InterfaceTrafic demanderDistributeurTrafic() {
        return distributeurTrafic;
    }

}