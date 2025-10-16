package pt.ipp.isep.dei.pi25_26.g093.app.domain;

public class Bay {
    private int id;
    private int capacity;

    public Bay(int id, int capacity){
        this.id = id;
        this.capacity = capacity;
    }

    public int getCapacity(){
        return capacity;
    }

    public void setCapacity(int capacity){
        this.capacity = capacity;
    }
}
