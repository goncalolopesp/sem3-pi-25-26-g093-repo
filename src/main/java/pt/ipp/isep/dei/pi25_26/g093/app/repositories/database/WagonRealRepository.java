package pt.ipp.isep.dei.pi25_26.g093.app.repositories.database;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.*;
import pt.ipp.isep.dei.pi25_26.g093.app.utilities.DatabaseConnection;

import java.sql.*;
import java.util.*;

public class WagonRealRepository {

    private final Map<Integer, WagonReal> wagons = new HashMap<>();
    private final WagonModelRepository modelRepo;

    private static final String SQL =
            "SELECT WAGONID, WAGONMODELID, IDENTIFIER " +
                    "FROM WAGON";

    public WagonRealRepository() {
        this.modelRepo = new WagonModelRepository();
        loadAll();
    }

    private void loadAll() {

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                WagonModel model = modelRepo.findById(
                        rs.getInt("WAGONMODELID")
                );

                WagonReal wr = new WagonReal(
                        rs.getInt("WAGONID"),
                        model,
                        rs.getString("IDENTIFIER")
                );

                wagons.put(wr.getId(), wr);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error loading wagons", e);
        }
    }

    public WagonReal findById(int id) {
        return wagons.get(id);
    }

    public Collection<WagonReal> getAll() {
        return wagons.values();
    }
}

