package pt.ipp.isep.dei.pi25_26.g093.app.repositories;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.Wagon;

import java.util.ArrayList;
import java.util.List;

public class WagonRepository {

    private List<Wagon> l;

    public WagonRepository(){
        l = new ArrayList<>();
    }

    public List<Wagon> getWagons(){
        return l;
    }

    public void add(Wagon w){
        l.add(w);
    }

}
