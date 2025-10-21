package pt.ipp.isep.dei.pi25_26.g093.app.domain;

public class Wagon {
    private int wagonID;
    private Cargo cargo;

    /**
     * Creates a new Wagon with the given parameters.
     * @param wagonID ID of the Wagon
     * @param cargo Cargo of the Wagon.
     */
    public Wagon(int wagonID, Cargo cargo) {
        this.wagonID = wagonID;
        this.cargo = cargo;
    }

    /**
     * Creates a new Wagon with the given parameters.
     * @param wagonID ID of the Wagon
     * @param boxID ID of the box
     * @param sku ID of the item in the box
     * @param quantity Quantity of items in the box
     * @param expiryDate Date of expiry (YYYY-MM-DD)
     * @param receivedAt Date of arrival (YYYY-MM-DDTHH:MM:SS)
     */
    public Wagon(int wagonID, int boxID, int sku, int quantity, String expiryDate, String receivedAt){
        Cargo newCargo = new Cargo(boxID, sku, quantity, expiryDate, receivedAt);
        this.wagonID = wagonID;
        this.cargo = newCargo;
    }

    /**
     * Gets the ID of the Wagon
     * @return ID of the Wagon
     */
    public int getwagonID(){
        return wagonID;
    }

    /**
     * Gets the cargo of the Wagon
     * @return cargo of the wagon.
     */
    public Cargo getCargo(){
        return cargo;
    }

    /**
     * Gets a String representation of the Wagon.
     * @return a String representation of the Wagon.
     */
    public String toString(){
        return "Wagon " + wagonID;
    }
}
