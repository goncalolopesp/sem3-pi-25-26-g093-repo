package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import java.util.List;

public class Aisle {
    private int id;
    private List<Bay> bays;

    public Aisle(int id, List<Bay> bays){
        this.id = id;
        this.bays = bays;
    }

    public List<Bay> getBays(){
        return bays;
    }

    public void setBays(List<Bay> bays){
        this.bays = bays;
    }
}
