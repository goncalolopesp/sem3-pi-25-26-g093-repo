package pt.ipp.isep.dei.pi25_26.g093.app.repositories;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderRepository {

    private List<Order> orders;

    /**
     * Creates a new instance of the OrderRepository and creates an empty List of Orders
     */
    public OrderRepository(){
        orders = new ArrayList<>();
    }

    /**
     * Gets the orders in the OrderRepository
     * @return orders in the OrderRepository
     */
    public List<Order> getOrders(){
        return orders;
    }

    /**
     * Adds a new Order to the OrderRepository
     * @param o Order to be added
     */
    public void add(Order o){
        orders.add(o);
    }

}
