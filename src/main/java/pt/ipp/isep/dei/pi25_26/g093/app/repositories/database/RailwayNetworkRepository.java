package pt.ipp.isep.dei.pi25_26.g093.app.repositories.database;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.*;
import pt.ipp.isep.dei.pi25_26.g093.app.utilities.DatabaseConnection;

import java.sql.*;
import java.util.*;

public class RailwayNetworkRepository {

    private final Map<Integer, Facility> facilities = new HashMap<>();
    private final Map<Integer, RailwayLine> lines = new HashMap<>();
    private final Map<Integer, List<RailwaySegment>> segmentsByLine = new HashMap<>();

    private static final String CALL_GET_LINES =
            "{ ? = call pkg_railway_network.get_lines }";

    private static final String CALL_GET_SEGMENTS =
            "{ ? = call pkg_railway_network.get_segments }";


    public RailwayNetworkRepository() {
        loadFacilities();
        loadLines();
        loadSegments();
        attachSegmentsToLines();
    }

    // ========================= FACILITIES =========================
    private void loadFacilities() {

        final String SQL = "SELECT ID, NAME FROM FACILITY";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Facility f = new Facility(
                        rs.getInt("ID"),
                        rs.getString("NAME")
                );
                facilities.put(f.getId(), f);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error loading facilities", e);
        }
    }

    // ========================= LINES ==============================
    private void loadLines() {

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt =
                     conn.prepareCall(CALL_GET_LINES)) {

            stmt.registerOutParameter(1, Types.REF_CURSOR);
            stmt.execute();

            try (ResultSet rs = (ResultSet) stmt.getObject(1)) {

                while (rs.next()) {

                    Facility start =
                            facilities.get(rs.getInt("STARTFACILITYID"));

                    Facility end =
                            facilities.get(rs.getInt("ENDFACILITYID"));

                    RailwayLine line = new RailwayLine(
                            rs.getInt("ID"),
                            rs.getString("NAME"),
                            start,
                            end,
                            0   // gauge ignorado
                    );

                    lines.put(line.getId(), line);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error loading railway lines", e);
        }
    }


    // ======================= SEGMENTS =============================
    private void loadSegments() {

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt =
                     conn.prepareCall(CALL_GET_SEGMENTS)) {

            stmt.registerOutParameter(1, Types.REF_CURSOR);
            stmt.execute();

            try (ResultSet rs = (ResultSet) stmt.getObject(1)) {

                while (rs.next()) {

                    int lineId = rs.getInt("RAILWAYLINEID");

                    double speedLimit = computeDefaultSpeedLimit(lineId);

                    RailwaySegment seg = new RailwaySegment(
                            rs.getInt("ID"),
                            rs.getInt("SEGMENTORDER"),
                            rs.getDouble("LENGTH"),
                            rs.getInt("NUMBERTRACKS"),
                            null,           // sidingPosition
                            null,           // sidingLength
                            rs.getDouble("WEIGHTLIMIT"),
                            speedLimit
                    );

                    segmentsByLine
                            .computeIfAbsent(lineId, k -> new ArrayList<>())
                            .add(seg);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error loading railway segments", e);
        }
    }



    // =================== CONNECT SEGMENTS TO LINES ================
    private void attachSegmentsToLines() {

        for (Map.Entry<Integer, List<RailwaySegment>> entry : segmentsByLine.entrySet()) {

            RailwayLine line = lines.get(entry.getKey());
            if (line == null) continue; // should not happen

            List<RailwaySegment> segs = entry.getValue();
            segs.sort(Comparator.comparingInt(RailwaySegment::getOrderInLine));

            for (RailwaySegment seg : segs) {
                seg.setRailwayLine(line);
                line.addSegment(seg);
            }
        }
    }

    private double computeDefaultSpeedLimit(int lineId) {

        RailwayLine line = lines.get(lineId);
        if (line == null) return 120;

        String name = line.getName().toLowerCase();

        if (name.contains("ramal"))
            return 80;
        if (name.contains("contumil") || name.contains("campanhã"))
            return 90;
        if (name.contains("leixões"))
            return 60;

        return 120; // default
    }


    // ===================== PUBLIC GETTERS ==========================
    public Facility findFacility(int id) { return facilities.get(id); }
    public Collection<Facility> getAllFacilities() { return facilities.values(); }
    public Collection<RailwayLine> getAllLines() { return lines.values(); }
    public RailwayLine getLine(int id) { return lines.get(id); }
}
