package pt.ipp.isep.dei.pi25_26.g093.app.domain;

public class Wagon {
    private int wagonID;
    private Cargo cargo;


    public Wagon(int wagonID, Cargo cargo) {
        this.wagonID = wagonID;
        this.cargo = cargo;
    }

    public Wagon(int wagonID, int boxID, int sku, int quantity, String expiryDate, String receivedAt){
        Cargo newCargo = new Cargo(boxID, sku, quantity, expiryDate, receivedAt);
        this.wagonID = wagonID;
        this.cargo = newCargo;
    }

    public int getwagonID(){
        return wagonID;
    }

    public Cargo getCargo(){
        return cargo;
    }

    public String toString(){
        return "Wagon " + wagonID + " : " + " : " + cargo.getExpiryDate();
    }
}
