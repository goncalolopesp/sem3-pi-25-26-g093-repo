package pt.ipp.isep.dei.pi25_26.g093.app.repositories.database;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.*;
import pt.ipp.isep.dei.pi25_26.g093.app.utilities.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class FreightRepository {

    private final Map<Integer, Freight> freights = new HashMap<>();

    private final FacilityRepository facilityRepository;
    private final WagonRealRepository wagonRepository;

    private static final String CALL_GET_ALL =
            "{ ? = call pkg_freight.get_all }";

    private static final String CALL_GET_WAGONS =
            "{ ? = call pkg_freight.get_wagons_for_freight(?) }";

    public FreightRepository() {
        this.facilityRepository = new FacilityRepository();
        this.wagonRepository = new WagonRealRepository();
        loadAll();
    }


    private void loadAll() {

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt =
                     conn.prepareCall(CALL_GET_ALL)) {

            stmt.registerOutParameter(1, Types.REF_CURSOR);
            stmt.execute();

            try (ResultSet rs = (ResultSet) stmt.getObject(1)) {

                while (rs.next()) {

                    int id = rs.getInt("FREIGHTID");

                    Facility origin =
                            facilityRepository.findById(
                                    rs.getInt("STARTFACILITYID"));

                    Facility destination =
                            facilityRepository.findById(
                                    rs.getInt("ENDFACILITYID"));

                    LocalDate date =
                            rs.getDate("FREIGHTDATE").toLocalDate();

                    List<WagonReal> wagons =
                            loadWagonsForFreight(conn, id);

                    Freight freight =
                            new Freight(id, origin, destination, date, wagons);

                    freights.put(id, freight);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error loading freights", e);
        }
    }


    public List<Freight> getAvailableAtFacility(int facilityId) {
        return freights.values().stream()
                .filter(f -> f.getCurrentFacilityId() == facilityId)
                .toList();
    }

    public void markDelivered(Freight freight) {
        // Atualiza a localização para a facility destino
        freight.setCurrentFacilityId(freight.getDestination().getId());

        // Tira este freight do mapa de pendentes
        freights.remove(freight.getId());
    }



    private List<WagonReal> loadWagonsForFreight(Connection conn, int freightId)
            throws SQLException {

        List<WagonReal> list = new ArrayList<>();

        try (CallableStatement stmt =
                     conn.prepareCall(CALL_GET_WAGONS)) {

            stmt.registerOutParameter(1, Types.REF_CURSOR);
            stmt.setInt(2, freightId);
            stmt.execute();

            try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
                while (rs.next()) {
                    int wagonId = rs.getInt("WAGONID");
                    WagonReal wr = wagonRepository.findById(wagonId);
                    if (wr != null) {
                        list.add(wr);
                    }
                }
            }
        }

        return list;
    }


    public Optional<Freight> findById(int id) {
        return Optional.ofNullable(freights.get(id));
    }

    public Collection<Freight> getAll() {
        return freights.values();
    }

    public void remove(int id) {
        freights.remove(id);
    }

}
