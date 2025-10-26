package pt.ipp.isep.dei.pi25_26.g093.app.controller;

import pt.ipp.isep.dei.pi25_26.g093.app.Bootstrap;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.Allocation;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.Date;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.DateTime;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.Eligibility;
import pt.ipp.isep.dei.pi25_26.g093.app.utilities.*;

import java.io.FileNotFoundException;
import java.util.*;

public class EligibilityAllocationController {

    private List<Allocation> allocations = new ArrayList<>();

    public boolean Allocate(String mode) throws FileNotFoundException {

        CSVParser csvOrders = new CSVParser("train_station_dataset/orders.csv");
        CSVParser csvOrderLines = new CSVParser("train_station_dataset/order_lines.csv");
        CSVParser csvWagons = new CSVParser("train_station_dataset/wagons.csv");
        CSVParser csvBays = new CSVParser("train_station_dataset/bays.csv");

        List<Map<String, String>> mapOrders = csvOrders.getCSVData();
        List<Map<String, String>> mapOrderLines = csvOrderLines.getCSVData();
        List<Map<String, String>> mapWagons = csvWagons.getCSVData();
        List<Map<String, String>> mapBays = csvBays.getCSVData();

        SetOrders(mapOrders);
        SetOrderLines(mapOrderLines);
        SetWagons(mapWagons);

        allocations.clear();
        List<Eligibility> eligibilities = new ArrayList<>();

        for (Map<String, String> mapOrder : mapOrders) {
            String order = mapOrder.get("orderId");
            int orderID = Integer.parseInt(order.substring(3));

            for (int l = 0; l < mapOrderLines.size(); l++) {
                String lineOrder = mapOrderLines.get(l).get("orderId");
                int lineOrderID = Integer.parseInt(lineOrder.substring(3));
                int lineNo = Integer.parseInt(mapOrderLines.get(l).get("lineNo"));
                List<Allocation> lineAllocations = new ArrayList<>();
                int requested = Integer.parseInt(mapOrderLines.get(l).get("qty"));
                int remaining = requested;

                if (orderID == lineOrderID) {
                    String sku = mapOrderLines.get(l).get("sku");

                    for(int w = 0; w < mapWagons.size(); w++)
                    {
                        if(mapWagons.get(w).get("sku").equals(sku))
                        {
                            for(int b = 0; b < mapBays.size(); b++)
                            {
                                int boxQty = Integer.parseInt(mapWagons.get(w).get("qty"));
                                int capacity = Integer.parseInt(mapBays.get(b).get("capacityBoxes"));
                                int allocated  = Math.min(remaining, boxQty);

                                if(capacity > 0 && allocated > 0)
                                {
                                    String boxID = mapWagons.get(w).get("boxId");
                                    int aisle = Integer.parseInt(mapBays.get(b).get("aisle"));
                                    int bay = Integer.parseInt(mapBays.get(b).get("bay"));
                                    Allocation allocation = new Allocation(order, lineNo, sku, allocated, boxID, aisle, bay);

                                    //Update Bay
                                    Object[] bayData = mapBays.get(b).values().toArray();
                                    bayData[3] = Integer.parseInt(bayData[3].toString()) - 1;
                                    Map<String, String> newBay = new HashMap<>();
                                    Object[] keys = mapBays.get(b).keySet().toArray();

                                    for(int i = 0; i < keys.length; i++)
                                        newBay.put(keys[i].toString(), bayData[i].toString());

                                    mapBays.set(b, newBay);

                                    //Update Box
                                    Object[] wagonData = mapWagons.get(w).values().toArray();
                                    wagonData[1] = Integer.parseInt(wagonData[1].toString()) - allocated;
                                    Map<String, String> newWagon = new HashMap<>();
                                    Object[] wagonKeys = mapWagons.get(w).keySet().toArray();

                                    for(int i = 0; i < wagonKeys.length; i++)
                                        newWagon.put(wagonKeys[i].toString(), wagonData[i].toString());

                                    mapWagons.set(w, newWagon);

                                    remaining -= allocated;

                                    lineAllocations.add(allocation);
                                }
                            }
                        }
                    }
                }

                Eligibility eligibility = new Eligibility(lineOrder, lineNo, mapOrderLines.get(l).get("sku"), requested, requested - remaining);

                if(remaining == 0)
                {
                    eligibility.setStatus("ELIGIBLE");
                    allocations.addAll(lineAllocations);
                }else
                {
                    if(remaining < requested && mode.equals("partial"))
                    {
                        eligibility.setStatus("PARTIAL");
                        allocations.addAll(lineAllocations);
                    }else
                    {
                        eligibility.setStatus("UNDISPATCHABLE");
                    }
                }

                eligibilities.add(eligibility);
            }
        }

        System.out.println("=== Eligibility ===");
        for(Eligibility e : eligibilities) {
            System.out.println(e.toString());
        }

        System.out.println("\n=== Allocations ===");
        for(Allocation a : allocations) {
            System.out.println(a.toString());
        }

        return true;

    }
    public List<Allocation> getAllocations() {
        return allocations;
    }

    private static void SetOrders(List<Map<String, String>> orders)
    {
        for (int i = 0; i < orders.size(); i++) {
            Object[] data = orders.get(i).values().toArray();
            DateTime date = new DateTime(data[1].toString());
            int priority = Integer.parseInt(data[2].toString());
            int order = Integer.parseInt(data[0].toString().substring(3));

            for (int j = 0; j < i; j++) {
                Object[] data2 = orders.get(j).values().toArray();
                DateTime date2 = new DateTime(data2[1].toString());
                int priority2 = Integer.parseInt(data2[2].toString());
                int order2 = Integer.parseInt(data2[0].toString().substring(3));

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
            Object[] data = orderLines.get(i).values().toArray();
            int lineNum = Integer.parseInt(data[0].toString());
            int order = Integer.parseInt(data[1].toString().substring(3));

            for (int j = 0; j < i; j++) {
                Object[] data2 = orderLines.get(j).values().toArray();
                int lineNum2 = Integer.parseInt(data2[0].toString());
                int order2 = Integer.parseInt(data2[1].toString().substring(3));

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

    private static void SetWagons(List<Map<String, String>> wagons)
    {
        for (int i = 0; i < wagons.size(); i++) {
            Object[] data = wagons.get(i).values().toArray();

            for (int j = 0; j < i; j++) {
                Object[] data2 = wagons.get(j).values().toArray();

                if(!data[0].toString().isEmpty() && data2[0].toString().isEmpty())
                {
                    Map<String, String> aux = wagons.get(i);
                    wagons.set(i, wagons.get(j));
                    wagons.set(j, aux);
                }

                if(!data[0].toString().isEmpty() && !data2[0].toString().isEmpty())
                {
                    Date date1 = new Date(data[0].toString());
                    Date date2 = new Date(data2[0].toString());

                    if(date1.compareTo(date2) == -1)
                    {
                        Map<String, String> aux = wagons.get(i);
                        wagons.set(i, wagons.get(j));
                        wagons.set(j, aux);
                    }
                }
            }
        }
    }
}
