package pt.ipp.isep.dei.pi25_26.g093.app.repositories.database;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.LocomotiveModel;
import pt.ipp.isep.dei.pi25_26.g093.app.utilities.DatabaseConnection;

import java.sql.*;
import java.util.*;

public class LocomotiveModelRepository {

    private final Map<Integer, LocomotiveModel> models = new HashMap<>();

    private static final String CALL_GET_ALL =
            "{ ? = call pkg_locomotive_model.get_all }";

    public LocomotiveModelRepository() {
        loadAll();
    }

    private void loadAll() {
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(CALL_GET_ALL)) {

            stmt.registerOutParameter(1, Types.REF_CURSOR);
            stmt.execute();

            try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
                while (rs.next()) {

                    LocomotiveModel lm = new LocomotiveModel(
                            rs.getInt("LOCOMOTIVEMODELID"),
                            rs.getString("MAKER"),
                            rs.getDouble("POWER"),
                            rs.getDouble("TOPSPEED"),
                            rs.getInt("TRACKTYPE"),
                            0.0, // gauge ignorado
                            rs.getDouble("TAREWEIGHT"),
                            rs.getDouble("MAXWEIGHT"),
                            rs.getInt("NUMBEROFBOGIES"),
                            rs.getString("TRACTIONTYPE") // <-- NOVO
                    );

                    models.put(lm.getId(), lm);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error loading locomotive models (PL/SQL)", e);
        }
    }

    public LocomotiveModel findById(int id) {
        return models.get(id);
    }

    public Collection<LocomotiveModel> getAll() {
        return models.values();
    }
}

