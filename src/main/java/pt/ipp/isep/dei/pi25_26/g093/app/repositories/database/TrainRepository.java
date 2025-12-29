package pt.ipp.isep.dei.pi25_26.g093.app.repositories.database;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.*;
import pt.ipp.isep.dei.pi25_26.g093.app.utilities.DatabaseConnection;

import java.sql.*;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.Date;

public class TrainRepository {

    private final Map<Integer, Train> trains = new HashMap<>();

    private final LocomotiveRepository locomotiveRepository;
    private final FreightRepository freightRepository;

    private static final String CALL_GET_ALL =
            "{ ? = call pkg_train.get_all }";

    private static final String CALL_GET_LOCOMOTIVES =
            "{ ? = call pkg_train.get_locomotives(?) }";

    private static final String CALL_GET_FREIGHTS =
            "{ ? = call pkg_train.get_freights(?) }";


    private int nextId = 1;

    public TrainRepository() {
        this.locomotiveRepository = new LocomotiveRepository();
        this.freightRepository = new FreightRepository();
        loadAll();
    }

    private void loadAll() {

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(CALL_GET_ALL)) {

            stmt.registerOutParameter(1, Types.REF_CURSOR);
            stmt.execute();

            try (ResultSet rs = (ResultSet) stmt.getObject(1)) {

                while (rs.next()) {

                    int trainId = rs.getInt("TRAINID");

                    int pathId = rs.getInt("PATHID");
                    int operatorId = rs.getInt("OPERATORID");

                    Date sqlDate = rs.getDate("STARTDATE");
                    LocalDate startDate =
                            (sqlDate != null) ? ((java.sql.Date) sqlDate).toLocalDate() : null;

                    LocalTime startTime = null;

                    List<Locomotive> locomotives =
                            loadLocomotivesForTrain(trainId);

                    List<Freight> freights =
                            loadFreightsForTrain(trainId);

                    Train train = new Train(
                            trainId,
                            String.valueOf(trainId),
                            locomotives,
                            freights,
                            startDate,
                            startTime
                    );

                    trains.put(trainId, train);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error loading trains", e);
        }
    }


    private List<Locomotive> loadLocomotivesForTrain(int trainId) throws SQLException {
        List<Locomotive> locomotives = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(CALL_GET_LOCOMOTIVES)) {

            stmt.registerOutParameter(1, Types.REF_CURSOR);
            stmt.setInt(2, trainId);
            stmt.execute();

            try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
                while (rs.next()) {
                    int locoId = rs.getInt("LOCOMOTIVEID");

                    Locomotive loco = locomotiveRepository.findById(locoId);

                    if (loco != null) {
                        locomotives.add(loco);
                    }
                }
            }
        }

        return locomotives;
    }

    private List<Freight> loadFreightsForTrain(int trainId) throws SQLException {
        List<Freight> freights = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(CALL_GET_FREIGHTS)) {

            stmt.registerOutParameter(1, Types.REF_CURSOR);
            stmt.setInt(2, trainId);
            stmt.execute();

            try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
                while (rs.next()) {
                    int freightId = rs.getInt("FREIGHTID");

                    // FreightRepository no teu código devolve Optional<Freight>
                    Freight f = freightRepository.findById(freightId).orElse(null);

                    if (f != null) {
                        freights.add(f);
                    }
                }
            }
        }

        return freights;
    }

    public Collection<Train> getAll() {
        return trains.values();
    }

    public Optional<Train> findById(int trainId) {
        return Optional.ofNullable(trains.get(trainId));
    }

    public Train save(Train train) {

        int id = train.getId();

        if (id <= 0) {
            id = nextId++;
        }

        Train stored = new Train(
                id,
                train.getNumber(),
                train.getLocomotives(),
                train.getFreights(),
                train.getStartDate(),
                train.getStartTime()
        );

        trains.put(id, stored);
        return stored;
    }

}
