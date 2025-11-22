package pt.ipp.isep.dei.pi25_26.g093.app.domain;

public class RailwaySegment {

    private final int id;
    private final int orderInLine;
    private final double lengthMeters;
    private final int numberTracks;
    private final Double sidingPositionMeters;
    private final Double sidingLengthMeters;    
    private final double weightLimitTon;
    private RailwayLine line;
    private double speedLimitKmh;


    public RailwaySegment(int id,
                          int orderInLine,
                          double lengthMeters,
                          int numberTracks,
                          Double sidingPositionMeters,
                          Double sidingLengthMeters,
                          double weightLimitTon,  double speedLimitKmh) {

        this.id = id;
        this.orderInLine = orderInLine;
        this.lengthMeters = lengthMeters;
        this.numberTracks = numberTracks;
        this.sidingPositionMeters = sidingPositionMeters;
        this.sidingLengthMeters = sidingLengthMeters;
        this.weightLimitTon = weightLimitTon;
        this.speedLimitKmh = speedLimitKmh;

    }

    public int getId() { return id; }
    public int getOrderInLine() { return orderInLine; }
    public double getLengthMeters() { return lengthMeters; }
    public int getNumberTracks() { return numberTracks; }
    public Double getSidingPositionMeters() { return sidingPositionMeters; }
    public Double getSidingLengthMeters() { return sidingLengthMeters; }
    public double getWeightLimitTon() { return weightLimitTon; }

    public boolean hasSiding() {
        return sidingLengthMeters != null && sidingLengthMeters > 0;
    }

    public RailwayLine getRailwayLine() {
        return line;
    }

    public void setRailwayLine(RailwayLine line) {
        this.line = line;
    }

    public double getSpeedLimitKmh() {
        return speedLimitKmh;
    }

}
