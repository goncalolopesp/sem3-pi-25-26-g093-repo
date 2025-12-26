package pt.ipp.isep.dei.pi25_26.g093.app.repositories.database;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.*;
import pt.ipp.isep.dei.pi25_26.g093.app.utilities.DatabaseConnection;

import java.sql.*;
import java.util.*;

public class WagonRealRepository {

    private final Map<Integer, WagonReal> wagons = new HashMap<>();
    private final WagonModelRepository modelRepo;

    private static final String CALL =
            "{ ? = call pkg_wagon_real.get_all }";


    public WagonRealRepository() {
        this.modelRepo = new WagonModelRepository();
        loadAll();
    }

    private void loadAll() {

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cs = conn.prepareCall(CALL)) {

            cs.registerOutParameter(1, Types.REF_CURSOR);
            cs.execute();

            try (ResultSet rs = (ResultSet) cs.getObject(1)) {

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
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error loading wagons (PL/SQL)", e);
        }
    }


    public WagonReal findById(int id) {
        return wagons.get(id);
    }

    public Collection<WagonReal> getAll() {
        return wagons.values();
    }
}

