package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import java.util.ArrayList;
import java.util.List;

public class Bay {
    private int id;
    private int capacity;
    private List<Cargo> cargo;

    public Bay(int id, int capacity){
        this.id = id;
        this.capacity = capacity;
        this.cargo = new ArrayList<>();
    }

    public int getCapacity(){
        return capacity;
    }

    public void setCapacity(int capacity){
        this.capacity = capacity;
    }

    public void setCargo(List<Cargo> cargo){
        this.cargo = cargo;
    }

    public void addCargo(Cargo c){
        this.cargo.add(c);
    }

    public List<Cargo> getCargo(){
        return cargo;
    }

    public int getId() {
        return id;
    }
}
