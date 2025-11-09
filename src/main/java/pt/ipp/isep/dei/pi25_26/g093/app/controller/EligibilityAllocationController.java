package pt.ipp.isep.dei.pi25_26.g093.app.controller;

import pt.ipp.isep.dei.pi25_26.g093.app.Bootstrap;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.*;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.Date;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.OrderRepository;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.Repositories;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.WarehouseRepository;
import pt.ipp.isep.dei.pi25_26.g093.app.utilities.CSVParser;

import java.io.FileNotFoundException;
import java.util.*;

public class EligibilityAllocationController {

    private List<Allocation> allocations = new ArrayList<>();
    private List<Eligibility> eligibilities = new ArrayList<>();

    public static class Allocation {
        public String orderId;
        public int lineNo;
        public String sku;
        public int qty;
        public String boxId;
        public int aisle;
        public int bay;
    }

    public static class Eligibility {
        public String orderId;
        public int lineNo;
        public String sku;
        public int requestedQty;
        public int allocatedQty;
        public String status;
    }

    public boolean Allocate(String mode) {

        Repositories repositories = Repositories.getInstance();
        WarehouseRepository warehouseRepository = repositories.getWarehouseRepository();
        OrderRepository orderRepository = repositories.getOrderRepository();

        List<Warehouse> warehouses = warehouseRepository.getWarehouseList();
        List<Order> orders = orderRepository.getOrders();

        if(orders == null || orders.isEmpty())
            return false;

        SetOrders(orders);

        for(Order order : orders)
            SetOrderLines(order);

        allocations.clear();

        for (Order order : orders) {
            String orderID = order.getId();

            for (OrderLine line : order.getLines()) {
                int lineNo = line.getLineNo();
                List<Allocation> lineAllocations = new ArrayList<>();
                int requested = line.getQty();
                int remaining = requested;

                String sku = line.getSku();

                for (Warehouse warehouse : warehouses) {
                    for(Aisle aisle : warehouse.getAisles())
                    {
                        for(Bay bay : aisle.getBays())
                        {
                            for(Cargo cargo : bay.getCargo())
                            {
                                if(cargo.getSku() == Integer.parseInt(sku.substring(3)))
                                {
                                    int cargoQty = cargo.getQuantity();
                                    int capacity = bay.getCapacity();
                                    int allocated = Math.min(remaining, cargoQty);

                                    if(capacity > 0 && allocated > 0)
                                    {
                                        Allocation allocation = new Allocation();
                                        allocation.orderId = orderID;
                                        allocation.lineNo = lineNo;
                                        allocation.sku = sku;
                                        allocation.qty = allocated;
                                        allocation.boxId = String.valueOf(cargo.getBoxID());
                                        allocation.aisle = aisle.getId();
                                        allocation.bay = bay.getId();

                                        bay.setCapacity(capacity - 1);
                                        cargo.setQuantity(cargoQty - allocated);

                                        remaining -= allocated;
                                        lineAllocations.add(allocation);
                                    }
                                }
                            }
                        }
                    }
                }

                Eligibility eligibility = new Eligibility();
                eligibility.orderId = orderID;
                eligibility.lineNo = lineNo;
                eligibility.sku = sku;
                eligibility.requestedQty = requested;
                eligibility.allocatedQty = requested - remaining;

                if (remaining == 0) {
                    eligibility.status = "ELIGIBLE";
                    allocations.addAll(lineAllocations);
                } else {
                    if (remaining < requested && mode.equals("partial")) {
                        eligibility.status = "PARTIAL";
                        allocations.addAll(lineAllocations);
                    } else {
                        eligibility.status = "UNDISPATCHABLE";
                    }
                }

                eligibilities.add(eligibility);
            }
        }

        return true;
    }

    public List<Allocation> getAllocations() {
        return allocations;
    }

    public List<Eligibility> getEligibilities() {
        return eligibilities;
    }

    private static void SetOrders(List<Order> orders)
    {
        for (int i = 0; i < orders.size(); i++) {
            Order order1 = orders.get(i);
            int priority = order1.getPriority();
            Date date = order1.getDueDate();
            int orderId = order1.getIntId();

            for (int j = 0; j < i; j++) {
                Order order2 = orders.get(j);
                int priority2 = order2.getPriority();
                Date date2 = order2.getDueDate();
                int orderId2 = order2.getIntId();

                if(priority2 > priority)
                {
                    Order aux = orders.get(i);
                    orders.set(i, orders.get(j));
                    orders.set(j, aux);
                }

                if(date.compareTo(date2) == -1 && priority2 == priority)
                {
                    Order aux = orders.get(i);
                    orders.set(i, orders.get(j));
                    orders.set(j, aux);
                }

                if(date.compareTo(date2) == 0 && priority2 == priority && orderId < orderId2)
                {
                    Order aux = orders.get(i);
                    orders.set(i, orders.get(j));
                    orders.set(j, aux);
                }
            }
        }
    }

    private static void SetOrderLines(Order order)
    {
        List<OrderLine> orderLines = order.getLines();
        for (int i = 0; i < orderLines.size(); i++) {
            int lineNum = orderLines.get(i).getLineNo();
            for (int j = 0; j < i; j++) {
                int lineNum2 = orderLines.get(j).getLineNo();

                if(lineNum2 > lineNum)
                {
                    OrderLine aux = orderLines.get(i);
                    orderLines.set(i, orderLines.get(j));
                    orderLines.set(j, aux);
                }
            }
        }
    }
}
