package pt.ipp.isep.dei.pi25_26.g093.app.domain;

public class LocomotiveModel {

    private final int id;
    private final String maker;
    private final double powerKw;
    private final double maxSpeedKmh;
    private final int trackType;
    private final double gauge; // podes manter, mas não usamos
    private final double tareWeightTon;
    private final double maxWeightTon;
    private final int numberOfBogies;

    private final String tractionType; // <-- NOVO

    public LocomotiveModel(int id,
                           String maker,
                           double powerKw,
                           double maxSpeedKmh,
                           int trackType,
                           double gauge,
                           double tareWeightTon,
                           double maxWeightTon,
                           int numberOfBogies,
                           String tractionType) {

        this.id = id;
        this.maker = maker;
        this.powerKw = powerKw;
        this.maxSpeedKmh = maxSpeedKmh;
        this.trackType = trackType;
        this.gauge = gauge;
        this.tareWeightTon = tareWeightTon;
        this.maxWeightTon = maxWeightTon;
        this.numberOfBogies = numberOfBogies;
        this.tractionType = tractionType;

    }

    public int getId() { return id; }
    public String getMaker() { return maker; }
    public double getPowerKw() { return powerKw; }
    public double getMaxSpeedKmh() { return maxSpeedKmh; }
    public int getTrackType() { return trackType; }
    public double getGauge() { return gauge; }
    public double getTareWeightTon() { return tareWeightTon; }
    public double getMaxWeightTon() { return maxWeightTon; }
    public int getNumberOfBogies() { return numberOfBogies; }

    public double getLengthMeters() {
        return 19.0;
    }

    public String getTractionType() { return tractionType; }

    public String getName() {
        String type = (tractionType != null ? tractionType : "UNKNOWN");
        return maker + " " + type
                ;
    }
}
