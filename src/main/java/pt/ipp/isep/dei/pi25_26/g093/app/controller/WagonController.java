package pt.ipp.isep.dei.pi25_26.g093.app.controller;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.Wagon;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.Repositories;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.WagonRepository;

import java.util.List;

public class WagonController {
    Repositories repositories = Repositories.getInstance();

    /**
     * Gets the WagonRepository
     * @return WagonRepository
     */
    public WagonRepository getRepo(){
        return repositories.getWagonRepository();
    }

    /**
     * Gets all the stored wagons
     * @return Stored Wagons in the WagonRepository
     */
    public List<Wagon> getWagons(){
        return getRepo().getWagons();
    }

    /**
     * Adds a new wagon to the WagonRepository
     * @param w Wagon to be added to the WagonRepository
     */
    public void addWagon(Wagon w){
        getRepo().add(w);
    }

    /**
     * Adds a new wagon to the WagonRepository using the parameters provided.
     * @param wagonID the ID of the Wagon
     * @param boxID the ID of the box
     * @param sku the ID of the item
     * @param quantity The quantity of items in a box
     * @param expiryDate The Expiry Date of the items
     * @param receivedAt The Date the items were received
     */
    public void addWagon(int wagonID, int boxID, int sku, int quantity, String expiryDate, String receivedAt){
        Wagon newWagon = new Wagon(wagonID, boxID, sku, quantity, expiryDate, receivedAt);
        this.addWagon(newWagon);
    }
}
