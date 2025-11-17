package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RailwayLine {

    private final int id;
    private final String name;
    private final String owner;
    private final double gauge;

    private final Facility startFacility;
    private final Facility endFacility;

    private final List<RailwayLineSegment> segments = new ArrayList<>();

    public RailwayLine(int id, String name, String owner, double gauge,Facility startFacility, Facility endFacility) {

        this.id = id;
        this.owner = owner;
        this.gauge = gauge;
        this.name = name;
        this.startFacility = startFacility;
        this.endFacility = endFacility;

    }


    public int getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public double getGauge() {
        return gauge;
    }

    public Facility getStartFacility() {
        return startFacility;
    }
    public Facility getEndFacility() {
        return endFacility;
    }

    public void addSegment(RailwayLineSegment segment) {
        segments.add(segment);
    }

    public List<RailwayLineSegment> getSegments() {
        return segments;
    }

    public String getName() {
        return name;
    }

    public void sortSegmentsByOrder() {
        segments.sort(Comparator.comparingInt(RailwayLineSegment::getSegmentOrder));
    }

}
