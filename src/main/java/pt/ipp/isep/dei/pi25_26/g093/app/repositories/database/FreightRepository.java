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

    private static final String SQL_FREIGHT =
            "SELECT FREIGHTID, STARTFACILITYID, ENDFACILITYID, FREIGHTDATE " +
                    "FROM FREIGHT";

    private static final String SQL_WAGONS =
            "SELECT WAGONID FROM WAGON_FREIGHT WHERE FREIGHTID = ?";

    public FreightRepository() {
        this.facilityRepository = new FacilityRepository();
        this.wagonRepository = new WagonRealRepository();
        loadAll();
    }


    private void loadAll() {

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_FREIGHT);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                int id = rs.getInt("FREIGHTID");

                Facility origin = facilityRepository.findById(
                        rs.getInt("STARTFACILITYID")
                );

                Facility destination = facilityRepository.findById(
                        rs.getInt("ENDFACILITYID")
                );

                LocalDate date = rs.getDate("FREIGHTDATE").toLocalDate();

                List<WagonReal> wagons = loadWagonsForFreight(conn, id);

                Freight f = new Freight(id, origin, destination, date, wagons);

                freights.put(id, f);
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

        try (PreparedStatement stmt = conn.prepareStatement(SQL_WAGONS)) {
            stmt.setInt(1, freightId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    WagonReal wr = wagonRepository.findById(rs.getInt("WAGONID"));
                    list.add(wr);
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
