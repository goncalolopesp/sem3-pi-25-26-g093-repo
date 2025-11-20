package pt.ipp.isep.dei.pi25_26.g093.app.repositories;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.Warehouse;

/**
 * Class that represents all the repositories in the application.
 */
public class Repositories {
    private static Repositories instance;
    private WagonRepository wagonRepository;
    private WarehouseRepository warehouseRepository;
    private ReturnItemRepository returnItemRepository;
    private TrolleyPlanRepository trolleyPlanRepository;
    private OrderRepository orderRepository;
    private FacilityRepository facilityRepository;
    private RailwayLineRepository railwayLineRepository;
    private RailwayLineSegmentRepository railwayLineSegmentRepository;
    private CompleteStationRepository completeStationRepository;


    /**
     * Creates a new Repositories object with WagonRepository and WarehouseRepository.
     */
    public Repositories(){

        wagonRepository = new WagonRepository();
        warehouseRepository = new WarehouseRepository();
        returnItemRepository = new ReturnItemRepository();
        trolleyPlanRepository = new TrolleyPlanRepository();
        orderRepository = new OrderRepository();
        facilityRepository = new FacilityRepository();
        railwayLineRepository = new RailwayLineRepository();
        railwayLineSegmentRepository = new RailwayLineSegmentRepository();
        completeStationRepository = new CompleteStationRepository();

    }

    /**
     * Gets the WagonRepository.
     * @return the WagonRepository.
     */
    public WagonRepository getWagonRepository(){
        return wagonRepository;
    }

    /**
     * Gets the WarehouseRepository.
     * @return the WarehouseRepository.
     */
    public WarehouseRepository getWarehouseRepository(){
        return warehouseRepository;
    }

    /**
     * Gets the ReturnItemRepository.
     * @return the ReturnItemRepository.
     */
    public ReturnItemRepository getReturnItemRepository(){

        return returnItemRepository;

    }

    /**
     * Gets the TrolleyPlanRepository.
     * @return the TrolleyPlanRepository.
     */
    public TrolleyPlanRepository getTrolleyPlanRepository(){
        return trolleyPlanRepository;
    }

    /**
     * Gets the OrderRepository.
     * @return the OrderRepository.
     */
    public OrderRepository getOrderRepository() {return orderRepository;}

    /**
     * Gets the FacilityRepository.
     * @return the FacilityRepository.
     */
    public FacilityRepository getFacilityRepository() {
        return facilityRepository;
    }

    /**
     * Gets the RailwayLineRepository.
     * @return the RailwayLineRepository.
     */
    public RailwayLineRepository getRailwayLineRepository() {
        return railwayLineRepository;
    }

    /**
     * Gets the RailwayLineSegmentRepository.
     * @return the RailwayLineSegmentRepository.
     */
    public RailwayLineSegmentRepository getRailwayLineSegmentRepository() {
        return railwayLineSegmentRepository;
    }

    /**
     * Gets the CompleteStationRepository.
     * @return the CompleteStationRepository.
     */
    public CompleteStationRepository getCompleteStationRepository() {
        return completeStationRepository;
    }

    /**
     * Gets the instance of the Repositories class.
     * @return the instance of the Repositories class.
     */
    public static Repositories getInstance() {
        if (instance == null) {
            synchronized (Repositories.class) {
                instance = new Repositories();
            }
        }
        return instance;
    }

}
