package pt.ipp.isep.dei.pi25_26.g093.app.service;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.TrolleyItem;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.TrolleyPlan;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import pt.ipp.isep.dei.pi25_26.g093.app.controller.EligibilityAllocationController;
import java.io.FileNotFoundException;


public class PickingPlanner { //Cria um plano de picking segundo a heurística escolhida. "FF", "FFD" ou "BFD

    public List<TrolleyPlan> generatePickingPlan(double trolleyCapacity, String mode) throws FileNotFoundException {
        //cria um controlador e corre o allocate
        EligibilityAllocationController controller = new EligibilityAllocationController();
        controller.Allocate("partial"); // ou "strict" se quiseres modo total

        //localizar as allocacions geradas
        List<EligibilityAllocationController.Allocation> allocations = controller.getAllocations();

        //converter allocations em TrolleyItem
        List<TrolleyItem> items = new ArrayList<>();
        for (EligibilityAllocationController.Allocation alloc : allocations) {
            double unitWeight = 2.0; // Exemplo temporário, muda quando tiveres peso real
            double totalWeight = alloc.qty * unitWeight;

            TrolleyItem item = new TrolleyItem(
                    alloc.orderId,
                    alloc.sku,
                    alloc.qty,
                    totalWeight,
                    alloc.aisle,
                    alloc.bay
            );

            items.add(item);
        }

        //mantém o algoritmo de heurística exatamente igual
        List<TrolleyItem> itemsCopy = new ArrayList<>(items);

        if (mode.equalsIgnoreCase("FFD") || mode.equalsIgnoreCase("BFD")) {
            itemsCopy.sort(Comparator.comparingDouble(TrolleyItem::getWeight).reversed());
        }

        List<TrolleyPlan> trolleys = new ArrayList<>();

        for (TrolleyItem item : itemsCopy) {
            boolean placed = false;

            if (mode.equalsIgnoreCase("FF") || mode.equalsIgnoreCase("FFD")) {
                for (TrolleyPlan trolley : trolleys) {
                    if (trolley.tryAdd(item)) {
                        placed = true;
                        break;
                    }
                }
            } else if (mode.equalsIgnoreCase("BFD")) {
                TrolleyPlan bestFit = null;
                double minRemaining = Double.MAX_VALUE;

                for (TrolleyPlan trolley : trolleys) {
                    double remaining = trolley.getCapacityKg() - trolley.getUsedWeight();
                    if (item.getWeight() <= remaining && remaining - item.getWeight() < minRemaining) {
                        bestFit = trolley;
                        minRemaining = remaining - item.getWeight();
                    }
                }

                if (bestFit != null) {
                    bestFit.tryAdd(item);
                    placed = true;
                }
            }

            if (!placed) {
                TrolleyPlan newTrolley = new TrolleyPlan(trolleys.size() + 1, trolleyCapacity);
                newTrolley.tryAdd(item);
                trolleys.add(newTrolley);
            }
        }

        return trolleys;
    }


    //mostra um relatório simples dos trolleys

    public void displayPlan(List<TrolleyPlan> trolleys) {
        System.out.println("=== Picking Plan Summary ===");
        for (TrolleyPlan trolley : trolleys) {
            System.out.println(trolley);
            for (TrolleyItem item : trolley.getItems()) {
                System.out.println("   -> " + item);
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        PickingPlanner planner = new PickingPlanner();

        List<TrolleyPlan> plans = planner.generatePickingPlan(60.0, "FFD");

        System.out.println("\n--- Picking Plan (dados reais da USEI02) ---");
        planner.displayPlan(plans);
    }
}


