package pt.ipp.isep.dei.pi25_26.g093.app.domain;

public class RailwayLineSegment {

    private final int id;
    private final RailwayLine line;
    private final int segmentOrder;

    private final boolean isElectrified;
    private final double maxWeight;
    private final double length;
    private final int numberTracks;
    private final Double sidingPosition;
    private final Double sidingLength;

    private final int maxSpeed;

    public RailwayLineSegment(int id, RailwayLine line, int segmentOrder, boolean isElectrified, double maxWeight, double length, int numberTracks, Double sidingPosition, Double sidingLength, int maxSpeed){

        this.id = id;
        this.line = line;
        this.segmentOrder = segmentOrder;
        this.isElectrified = isElectrified;
        this.maxWeight = maxWeight;
        this.length = length;
        this.numberTracks = numberTracks;
        this.sidingPosition = sidingPosition;
        this.sidingLength = sidingLength;
        this.maxSpeed = maxSpeed;

    }

    public int getId() { return id; }
    public RailwayLine getLine() { return line; }
    public int getSegmentOrder() { return segmentOrder; }
    public boolean isElectrified() { return isElectrified; }
    public double getMaxWeight() { return maxWeight; }
    public double getLength() { return length; }
    public int getNumberTracks() { return numberTracks; }
    public Double getSidingPosition() { return sidingPosition; }
    public Double getSidingLength() { return sidingLength; }
    public int getMaxSpeed() { return maxSpeed; }


    public boolean hasSiding() {
        return numberTracks == 1 && sidingLength != null && sidingLength > 0;
    }


}
