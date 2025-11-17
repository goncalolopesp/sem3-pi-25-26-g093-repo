package pt.ipp.isep.dei.pi25_26.g093.app.repositories;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.RailwayLineSegment;

import java.util.ArrayList;
import java.util.List;

public class RailwayLineSegmentRepository {

    private final List<RailwayLineSegment> segments;

    public RailwayLineSegmentRepository() {
        this.segments = new ArrayList<>();
    }

    public void add(RailwayLineSegment seg) {
        segments.add(seg);
    }

    public List<RailwayLineSegment> getAll() {
        return segments;
    }
}
