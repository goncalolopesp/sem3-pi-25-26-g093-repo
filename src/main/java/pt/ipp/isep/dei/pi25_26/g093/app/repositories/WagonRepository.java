package pt.ipp.isep.dei.pi25_26.g093.app.repositories;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.Wagon;

import java.util.ArrayList;
import java.util.List;

public class WagonRepository {

    private List<Wagon> l;

    /**
     * Creates a new instance of the WagonRepository and creates an empty List of Wagons
     */
    public WagonRepository(){
        l = new ArrayList<>();
    }

    /**
     * Gets the wagons in the WagonRepository
     * @return wagons in the WagonRepository
     */
    public List<Wagon> getWagons(){
        return l;
    }

    /**
     * Adds a new Wagon to the WagonRepository
     * @param w Wagon to be added
     */
    public void add(Wagon w){
        l.add(w);
    }

}
