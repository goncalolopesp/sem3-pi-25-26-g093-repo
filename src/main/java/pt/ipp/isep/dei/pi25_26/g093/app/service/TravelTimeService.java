package pt.ipp.isep.dei.pi25_26.g093.app.service;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.*;
import pt.ipp.isep.dei.pi25_26.g093.app.utilities.ExcelDataReader;

import java.io.IOException;
import java.util.*;

/**
 * LAPR3 - USPL03
 * Calculates estimated travel time between two stations (direct connection only).
 * Reads data from Excel file sheets: facility, line, locomotives.
 */
public class TravelTimeService {

    private final Map<String, Station> stationsById = new HashMap<>();
    private final Map<String, Station> stationsByNameLower = new HashMap<>();
    private final List<LineSegment> segments = new ArrayList<>();
    private final Map<String, Locomotive> locomotivesById = new HashMap<>();

    /**
     * Loads data from the Excel file provided by the user.
     */
    public void loadFromExcel(String filePath) throws IOException {
        ExcelDataReader reader = new ExcelDataReader(filePath);

        // tenta várias alternativas de nomes para compatibilidade
        List<Map<String, String>> facilities = tryReadSheet(reader, "facility", "Facility", "Facilities");
        List<Map<String, String>> locos = tryReadSheet(reader, "locomotives", "Locomotive", "Locomotives");
        List<Map<String, String>> lines = tryReadSheet(reader, "line", "Line", "Lines");

        loadStations(facilities);
        loadLocomotives(locos);
        loadLineSegments(lines);

        reader.close();
    }

    private List<Map<String, String>> tryReadSheet(ExcelDataReader reader, String... possibleNames) throws IOException {
        for (String name : possibleNames) {
            try {
                return reader.readSheet(name);
            } catch (IOException e) {
                // tenta o próximo nome
            }
        }
        throw new IOException("Nenhuma das folhas encontradas: " + Arrays.toString(possibleNames));
    }


    private void loadStations(List<Map<String, String>> sheetData) {
        stationsById.clear();
        stationsByNameLower.clear();
        for (Map<String, String> row : sheetData) {
            String id = nonNull(row.get("idFacility"));
            String name = nonNull(row.get("name"));
            String loc = row.getOrDefault("district", "");
            if (id.isEmpty() || name.isEmpty()) continue;
            Station s = new Station(id, name, loc);
            stationsById.put(id, s);
            stationsByNameLower.put(name.toLowerCase(), s);
        }
    }

    private void loadLocomotives(List<Map<String, String>> sheetData) {
        locomotivesById.clear();
        for (Map<String, String> row : sheetData) {
            String id = nonNull(row.get("idLocomotive"));
            String type = nonNull(row.get("type"));
            double speed = parseDoubleSafe(row.get("maxSpeed"), 0);
            double gauge = parseDoubleSafe(row.get("gauge"), 0);
            if (id.isEmpty() || type.isEmpty() || speed <= 0 || gauge <= 0) continue;
            locomotivesById.put(id, new Locomotive(id, type, speed, gauge));
        }
    }

    private void loadLineSegments(List<Map<String, String>> sheetData) {
        segments.clear();
        for (Map<String, String> row : sheetData) {
            String id = nonNull(row.get("idLine"));
            String start = nonNull(row.get("idFacility1"));
            String end = nonNull(row.get("idFacility2"));
            double length = parseDoubleSafe(row.get("length"), 0);
            double limit = parseDoubleSafe(row.get("maxSpeed"), 0);
            double gauge = parseDoubleSafe(row.get("gauge"), 0);
            boolean electrified = parseBoolean(row.getOrDefault("electrified", "false"));

            Station a = stationsById.get(start);
            Station b = stationsById.get(end);

            if (a == null || b == null || length <= 0 || limit <= 0 || gauge <= 0) continue;
            segments.add(new LineSegment(id, a, b, length, limit, gauge, electrified));
        }
    }

    public Optional<Station> findStationByIdOrName(String token) {
        if (token == null || token.isBlank()) return Optional.empty();
        Station byId = stationsById.get(token);
        if (byId != null) return Optional.of(byId);
        return Optional.ofNullable(stationsByNameLower.get(token.toLowerCase()));
    }

    public Optional<Locomotive> findLocoById(String id) {
        if (id == null || id.isBlank()) return Optional.empty();
        return Optional.ofNullable(locomotivesById.get(id));
    }

    public Optional<LineSegment> findDirectSegment(Station a, Station b) {
        return segments.stream().filter(s -> s.connects(a, b)).findFirst();
    }

    /** Core calculation: time (hours) = length / min(locoSpeed, segmentLimit). */
    public double calculateTravelTimeHours(Station a, Station b, Locomotive loco, LineSegment seg) {
        double effectiveSpeed = Math.min(loco.getMaxSpeedKmH(), seg.getSpeedLimitKmH());
        if (!loco.isGaugeCompatible(seg.getGaugeMm())) {
            throw new IllegalArgumentException(String.format(
                    "Gauge mismatch: loco %.0f mm vs segment %.0f mm", loco.getGaugeMm(), seg.getGaugeMm()));
        }
        return seg.getLengthKm() / effectiveSpeed;
    }

    /** Formats hours into "Xh Ym". */
    public static String formatH(double hours) {
        if (hours < 0) return "0m";
        int h = (int) hours;
        int m = (int) Math.round((hours - h) * 60.0);
        if (m >= 60) { h++; m = 0; }
        if (h > 0 && m > 0) return String.format("%dh %dm", h, m);
        if (h > 0) return String.format("%dh", h);
        return String.format("%dm", m);
    }

    // Helpers
    private static String nonNull(String s) { return s == null ? "" : s.trim(); }
    private static double parseDoubleSafe(String s, double def) {
        try { return Double.parseDouble(nonNull(s)); } catch (Exception e) { return def; }
    }
    private static boolean parseBoolean(String s) {
        String v = nonNull(s).toLowerCase();
        return v.equals("1") || v.equals("true") || v.equals("yes") || v.equals("y");
    }
}
