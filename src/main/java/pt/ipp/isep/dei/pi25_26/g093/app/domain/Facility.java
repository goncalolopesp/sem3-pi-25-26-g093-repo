package pt.ipp.isep.dei.pi25_26.g093.app.domain;

public class Facility {

    private final int id;
    private final String name;


    public Facility(int id, String name) {
        this.id = id;
        this.name = name;

    }

    public int getId() { return id; }

    public String getName() { return name; }

}
