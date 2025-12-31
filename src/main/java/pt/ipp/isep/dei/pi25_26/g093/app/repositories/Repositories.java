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
    private CompleteStationRepository completeStationRepository;
    private GraphRepository graphRepository;


    /**
     * Creates a new Repositories object with WagonRepository and WarehouseRepository.
     */
    public Repositories(){

        wagonRepository = new WagonRepository();
        warehouseRepository = new WarehouseRepository();
        returnItemRepository = new ReturnItemRepository();
        trolleyPlanRepository = new TrolleyPlanRepository();
        orderRepository = new OrderRepository();
        completeStationRepository = new CompleteStationRepository();
        graphRepository = new GraphRepository();

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
     * Gets the CompleteStationRepository.
     * @return the CompleteStationRepository.
     */
    public CompleteStationRepository getCompleteStationRepository() {
        return completeStationRepository;
    }

    public GraphRepository getGraphRepository() {
        return graphRepository;
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
