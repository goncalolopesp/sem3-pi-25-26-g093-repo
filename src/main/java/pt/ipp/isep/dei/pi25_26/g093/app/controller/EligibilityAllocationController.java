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

    public static class Allocation {
        public String orderId;
        public int lineNo;
        public String sku;
        public int qty;
        public String boxId;
        public int aisle;
        public int bay;
    }

    static class Eligibility {
        String orderId;
        int lineNo;
        String sku;
        int requestedQty;
        int allocatedQty;
        String status;
    }

    public boolean Allocate(String mode) throws FileNotFoundException {

        CSVParser csvOrders = new CSVParser("train_station_dataset/orders.csv");
        CSVParser csvOrderLines = new CSVParser("train_station_dataset/order_lines.csv");

        List<Map<String, String>> mapOrders = csvOrders.getCSVData();
        List<Map<String, String>> mapOrderLines = csvOrderLines.getCSVData();

        Repositories repositories = Repositories.getInstance();
        WarehouseRepository warehouseRepository = repositories.getWarehouseRepository();
        OrderRepository orderRepository = repositories.getOrderRepository();

        List<Warehouse> warehouses = warehouseRepository.getWarehouseList();
        List<Order> orders = orderRepository.getOrders();

        SetOrders(mapOrders);
        SetOrderLines(mapOrderLines);

        allocations.clear();
        List<Eligibility> eligibilities = new ArrayList<>();

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

        System.out.println("=== Eligibility ===");
        for(Eligibility e : eligibilities) {
            System.out.printf("%s | line %d | %s | %d/%d | %s%n",
                    e.orderId, e.lineNo, e.sku, e.allocatedQty, e.requestedQty, e.status);
        }

        System.out.println("\n=== Allocations ===");
        for(Allocation a : allocations) {
            System.out.printf("%s | line %d | %s | qty=%d | box=%s | aisle=%s | bay=%s%n",
                    a.orderId, a.lineNo, a.sku, a.qty, a.boxId, a.aisle, a.bay);
        }

        return true;

    }

    public List<Allocation> getAllocations() {
        return allocations;
    }

    private static void SetOrders(List<Map<String, String>> orders)
    {
        for (int i = 0; i < orders.size(); i++) {
            DateTime date = new DateTime(orders.get(i).get("dueDate"));
            int priority = Integer.parseInt(orders.get(i).get("priority"));
            int order = Integer.parseInt(orders.get(i).get("orderId").substring(3));

            for (int j = 0; j < i; j++) {
                DateTime date2 = new DateTime(orders.get(j).get("dueDate"));
                int priority2 = Integer.parseInt(orders.get(j).get("priority"));
                int order2 = Integer.parseInt(orders.get(j).get("orderId").substring(3));

                if(priority2 > priority)
                {
                    Map<String, String> aux = orders.get(i);
                    orders.set(i, orders.get(j));
                    orders.set(j, aux);
                }

                if(date.compareTo(date2) == -1 && priority2 == priority)
                {
                    Map<String, String> aux = orders.get(i);
                    orders.set(i, orders.get(j));
                    orders.set(j, aux);
                }

                if(date.compareTo(date2) == 0 && priority2 == priority && order < order2)
                {
                    Map<String, String> aux = orders.get(i);
                    orders.set(i, orders.get(j));
                    orders.set(j, aux);
                }
            }
        }
    }

    private static void SetOrderLines(List<Map<String, String>> orderLines)
    {
        for (int i = 0; i < orderLines.size(); i++) {
            int lineNum = Integer.parseInt(orderLines.get(i).get("lineNo"));
            int order = Integer.parseInt(orderLines.get(i).get("orderId").substring(3));

            for (int j = 0; j < i; j++) {
                int lineNum2 = Integer.parseInt(orderLines.get(j).get("lineNo"));
                int order2 = Integer.parseInt(orderLines.get(j).get("orderId").substring(3));

                if(order2 > order)
                {
                    Map<String, String> aux = orderLines.get(i);
                    orderLines.set(i, orderLines.get(j));
                    orderLines.set(j, aux);
                }

                if(lineNum2 > lineNum && order2 == order)
                {
                    Map<String, String> aux = orderLines.get(i);
                    orderLines.set(i, orderLines.get(j));
                    orderLines.set(j, aux);
                }
            }
        }

    }
}
