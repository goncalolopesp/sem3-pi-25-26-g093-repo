package pt.ipp.isep.dei.pi25_26.g093.app.repositories;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.RailwayLine;
import java.util.ArrayList;
import java.util.List;

public class RailwayLineRepository {

    private final List<RailwayLine> lines;

    public RailwayLineRepository() {
        this.lines = new ArrayList<>();
    }

    public void add(RailwayLine line) {
        lines.add(line);
    }

    public RailwayLine getById(int id) {
        for (RailwayLine rl : lines) {
            if (rl.getId() == id) return rl;
        }
        return null;
    }

    public List<RailwayLine> getAll() {
        return lines;
    }
}
