package pt.ipp.isep.dei.pi25_26.g093.app.repositories.database;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.WagonModel;
import pt.ipp.isep.dei.pi25_26.g093.app.utilities.DatabaseConnection;

import java.sql.*;
import java.util.*;

public class WagonModelRepository {

    private final Map<Integer, WagonModel> models = new HashMap<>();

    private static final String SQL =
            "SELECT WAGONMODELID, WAGONMODELTYPEID, CAPACITY, VOLUMECAPACITY, " +
                    "LENGTH, HEIGHT, MAXSPEED, GAUGE, MAKER, TAREWEIGHT, NUMBEROFBOGIES " +
                    "FROM WAGON_MODEL";

    public WagonModelRepository() {
        loadAll();
    }

    private void loadAll() {

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                WagonModel wm = new WagonModel(
                        rs.getInt("WAGONMODELID"),
                        rs.getInt("WAGONMODELTYPEID"),
                        rs.getDouble("CAPACITY"),
                        rs.getDouble("VOLUMECAPACITY"),
                        rs.getDouble("LENGTH"),
                        rs.getDouble("HEIGHT"),
                        rs.getDouble("MAXSPEED"),
                        rs.getDouble("GAUGE"),
                        rs.getString("MAKER"),
                        rs.getDouble("TAREWEIGHT"),
                        rs.getInt("NUMBEROFBOGIES")
                );

                models.put(wm.getId(), wm);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error loading wagon models", e);
        }
    }

    public WagonModel findById(int id) {
        return models.get(id);
    }

    public Collection<WagonModel> getAll() {
        return models.values();
    }
}
