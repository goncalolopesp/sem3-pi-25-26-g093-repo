package pt.ipp.isep.dei.pi25_26.g093.app.domain;

public class Coordinate {

    private final int aisle;
    private final int bay;

    public Coordinate(int aisle, int bay){
        this.aisle=aisle;
        this.bay=bay;
    }

    public int getAisle(){
        return aisle;
    }

    public int getBay(){
        return bay;
    }

    @Override
    public String toString(){
        return "("+aisle+" , "+bay+")";
    }

}
