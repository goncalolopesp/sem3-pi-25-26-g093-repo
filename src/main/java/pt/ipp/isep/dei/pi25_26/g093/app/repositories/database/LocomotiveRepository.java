package pt.ipp.isep.dei.pi25_26.g093.app.repositories.database;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.*;
import pt.ipp.isep.dei.pi25_26.g093.app.utilities.DatabaseConnection;

import java.sql.*;
import java.util.*;

public class LocomotiveRepository {

    private final Map<Integer, Locomotive> locomotives = new HashMap<>();
    private final LocomotiveModelRepository modelRepository;

    private static final String SQL =
            "SELECT LOCOMOTIVEID, LOCOMOTIVEMODELID, IDENTIFIER, OPERATORID, MODEL " +
                    "FROM LOCOMOTIVE";

    public LocomotiveRepository() {
        this.modelRepository = new LocomotiveModelRepository();
        loadAll();
    }

    public List<Locomotive> getAvailableAtFacility(int facilityId) {
        return locomotives.values().stream()
                .filter(l -> l.getCurrentFacilityId() == facilityId)
                .toList();
    }


    private void loadAll() {

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                int modelId = rs.getInt("LOCOMOTIVEMODELID");
                LocomotiveModel model = modelRepository.findById(modelId);

                // Todas começam em Leixões (ID 50)
                int leixoesId = 50;

                Locomotive loco = new Locomotive(
                        rs.getInt("LOCOMOTIVEID"),
                        rs.getString("IDENTIFIER"),
                        rs.getString("MODEL"),            // CP1900, E4000, Eurosprinter...
                        model,
                        rs.getInt("OPERATORID"),
                        leixoesId                         // NOVO
                );

                locomotives.put(loco.getId(), loco);
            }


        } catch (SQLException e) {
            throw new RuntimeException("Error loading locomotives", e);
        }
    }

    public Locomotive findById(int id) {
        return locomotives.get(id);
    }

    public Collection<Locomotive> getAll() {
        return locomotives.values();
    }
}
