package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import java.util.ArrayList;
import java.util.List;

public class RailwayLine {

    private final int id;
    private final String name;
    private final double gaugeMm;
    private final Facility start;
    private final Facility end;

    private final List<RailwaySegment> segments = new ArrayList<>();

    public RailwayLine(int id,
                       String name,
                       Facility start,
                       Facility end,
                       double gaugeMm) {

        this.id = id;
        this.name = name;
        this.start = start;
        this.end = end;
        this.gaugeMm = gaugeMm;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public Facility getStart() { return start; }
    public Facility getEnd() { return end; }
    public double getGaugeMm() { return gaugeMm; }

    public List<RailwaySegment> getSegments() { return List.copyOf(segments); }

    public void addSegment(RailwaySegment seg) {
        segments.add(seg);
    }
}
