package pt.ipp.isep.dei.pi25_26.g093.app.controller;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.Wagon;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.Repositories;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.WagonRepository;

import java.util.List;

public class WagonController {
    Repositories repositories = Repositories.getInstance();

    public WagonRepository getRepo(){
        return repositories.getWagonRepository();
    }

    public List<Wagon> getWagons(){
        return getRepo().getWagons();
    }

    public void addWagon(Wagon w){
        getRepo().add(w);
    }

    public void addWagon(int wagonID, int boxID, int sku, int quantity, String expiryDate, String receivedAt){
        Wagon newWagon = new Wagon(wagonID, boxID, sku, quantity, expiryDate, receivedAt);
        this.addWagon(newWagon);
    }
}
