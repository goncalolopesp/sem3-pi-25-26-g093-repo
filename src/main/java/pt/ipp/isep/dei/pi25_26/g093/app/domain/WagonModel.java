package pt.ipp.isep.dei.pi25_26.g093.app.domain;

public class  WagonModel {

    private final int id;
    private final WagonModelType type;
    private final double capacityTon;
    private final double volumeCapacityM3;
    private final double lengthMeters;
    private final double heightMeters;
    private final double maxSpeedKmh;
    private final double gaugeMm;
    private final String maker;
    private final double tareWeightTon;
    private final int numberOfBogies;

    public WagonModel(int id, int typeId,
                      double capacityTon, double volumeCapacityM3,
                      double lengthMeters, double heightMeters,
                      double maxSpeedKmh, double gaugeMm,
                      String maker, double tareWeightTon, int numberOfBogies) {

        this.id = id;
        this.type = new WagonModelType(typeId);
        this.capacityTon = capacityTon;
        this.volumeCapacityM3 = volumeCapacityM3;
        this.lengthMeters = lengthMeters;
        this.heightMeters = heightMeters;
        this.maxSpeedKmh = maxSpeedKmh;
        this.gaugeMm = gaugeMm;
        this.maker = maker;
        this.tareWeightTon = tareWeightTon;
        this.numberOfBogies = numberOfBogies;
    }

    public int getId() { return id; }

    public String getMaker() { return maker; }

    public double getLengthMeters() { return lengthMeters; }

    public double getTareWeightTon() { return tareWeightTon; }

    public double getMaxLoadTon() { return capacityTon; }

    public double getMaxSpeedKmh() { return maxSpeedKmh; }

    public WagonModelType getType() { return type; }
}
