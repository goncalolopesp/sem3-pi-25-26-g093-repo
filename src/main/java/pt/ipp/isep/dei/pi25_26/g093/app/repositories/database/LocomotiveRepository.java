package pt.ipp.isep.dei.pi25_26.g093.app.repositories.database;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.*;
import pt.ipp.isep.dei.pi25_26.g093.app.utilities.DatabaseConnection;

import java.sql.*;
import java.util.*;

public class LocomotiveRepository {

    private final Map<Integer, Locomotive> locomotives = new HashMap<>();
    private final LocomotiveModelRepository modelRepository;

    private static final String CALL =
            "{ ? = call pkg_locomotive.get_all }";

    private static final String SQL_LEIXOES_ID =
            "SELECT id FROM Facility WHERE name = 'Leixões'";

    private int getLeixoesId(Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(SQL_LEIXOES_ID);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        }
        throw new IllegalStateException("Facility 'Leixões' not found.");
    }


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
             CallableStatement stmt = conn.prepareCall(CALL)) {

            int leixoesId = getLeixoesId(conn);

            stmt.registerOutParameter(1, Types.REF_CURSOR);
            stmt.execute();

            try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
                while (rs.next()) {

                    int modelId = rs.getInt("LOCOMOTIVEMODELID");
                    LocomotiveModel model = modelRepository.findById(modelId);

                    Locomotive loco = new Locomotive(
                            rs.getInt("LOCOMOTIVEID"),
                            rs.getString("IDENTIFIER"),
                            (model != null ? model.getName() : "UNKNOWN"), // modelo derivado
                            model,
                            rs.getInt("OPERATORID"),
                            leixoesId // <-- todas em Leixões
                    );

                    locomotives.put(loco.getId(), loco);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error loading locomotives (PL/SQL)", e);
        }
    }


    public Locomotive findById(int id) {
        return locomotives.get(id);
    }

    public Collection<Locomotive> getAll() {
        return locomotives.values();
    }
}
