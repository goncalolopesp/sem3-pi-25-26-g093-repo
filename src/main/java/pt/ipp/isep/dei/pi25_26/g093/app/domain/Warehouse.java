package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import java.util.List;

public class Warehouse {
    private int id;
    private List<Aisle> aisles;

    public Warehouse(int id, List<Aisle> aisles){
        this.id = id;
        this.aisles = aisles;
    }

    public List<Aisle> getAisles(){
        return aisles;
    }

    public void setAisles(List<Aisle> aisles){
        this.aisles = aisles;
    }

    public int getId() {
        return id;
    }
}
