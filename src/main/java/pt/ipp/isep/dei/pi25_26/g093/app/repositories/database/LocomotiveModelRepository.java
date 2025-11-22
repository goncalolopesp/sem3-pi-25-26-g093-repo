package pt.ipp.isep.dei.pi25_26.g093.app.repositories.database;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.LocomotiveModel;
import pt.ipp.isep.dei.pi25_26.g093.app.utilities.DatabaseConnection;

import java.sql.*;
import java.util.*;

public class LocomotiveModelRepository {

    private final Map<Integer, LocomotiveModel> models = new HashMap<>();

    private static final String SQL =
            "SELECT LOCOMOTIVEMODELID, POWER, TOPSPEED, TRACKTYPE, " +
                    "GAUGE, TAREWEIGHT, MAXWEIGHT, NUMBEROFBOGIES, MAKER " +
                    "FROM LOCOMOTIVE_MODEL";

    public LocomotiveModelRepository() {
        loadAll();
    }

    private void loadAll() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                LocomotiveModel lm = new LocomotiveModel(
                        rs.getInt("LOCOMOTIVEMODELID"),
                        rs.getString("MAKER"),
                        rs.getDouble("POWER"),
                        rs.getDouble("TOPSPEED"),
                        rs.getInt("TRACKTYPE"),
                        rs.getDouble("GAUGE"),
                        rs.getDouble("TAREWEIGHT"),
                        rs.getDouble("MAXWEIGHT"),
                        rs.getInt("NUMBEROFBOGIES")
                );

                models.put(lm.getId(), lm);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error loading locomotive models", e);
        }
    }

    public LocomotiveModel findById(int id) {
        return models.get(id);
    }

    public Collection<LocomotiveModel> getAll() {
        return models.values();
    }
}

