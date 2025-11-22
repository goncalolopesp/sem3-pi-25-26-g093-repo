package pt.ipp.isep.dei.pi25_26.g093.app.domain;

public class WagonReal {

    private final int id;
    private final WagonModel model;
    private final String identifier;

    public WagonReal(int id, WagonModel model, String identifier) {
        this.id = id;
        this.model = model;
        this.identifier = identifier;
    }

    public int getId() { return id; }
    public WagonModel getModel() { return model; }
    public String getIdentifier() { return identifier; }

    public double getLengthMeters() { return model.getLengthMeters(); }

    public double getTareWeightTon() { return model.getTareWeightTon(); }

    public double getMaxLoadTon() { return model.getMaxLoadTon(); }

    public double getMaxSpeedKmh() { return model.getMaxSpeedKmh(); }

    public double getMaxTotalWeightTon() {
        return model.getTareWeightTon() + model.getMaxLoadTon();
    }

}
