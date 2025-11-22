package pt.ipp.isep.dei.pi25_26.g093.app.repositories.database;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.Facility;
import pt.ipp.isep.dei.pi25_26.g093.app.utilities.DatabaseConnection;

import java.sql.*;
import java.util.*;

public class FacilityRepository {

    private final Map<Integer, Facility> facilities = new HashMap<>();

    private static final String SQL =
            "SELECT ID, NAME, POSX, POSY FROM FACILITY";

    public FacilityRepository() {
        loadAll();
    }

    private void loadAll() {

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

    public Facility findById(int id) {
        return facilities.get(id);
    }

    public Collection<Facility> getAll() {
        return facilities.values();
    }
}
