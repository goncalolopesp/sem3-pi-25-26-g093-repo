package pt.ipp.isep.dei.pi25_26.g093.app.service;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.TrolleyItem;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.TrolleyPlan;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Classe responsável por criar planos de picking (USEI03).
 * Recebe alocações (AllocationRow) e distribui-as em trolleys respeitando a capacidade máxima.
 * Implementa as heurísticas:
 *  - First Fit (FF)
 *  - First Fit Decreasing (FFD)
 *  - Best Fit Decreasing (BFD)
 */
public class PickingPlanner {

    /**
     * Cria um plano de picking segundo a heurística escolhida.
     *
     * @param items lista de itens a alocar (futuramente virão da USEI02)
     * @param trolleyCapacity capacidade máxima (kg) de cada trolley
     * @param mode heurística: "FF", "FFD" ou "BFD"
     * @return lista de TrolleyPlan (cada um representa um trolley cheio)
     */
    public List<TrolleyPlan> generatePickingPlan(List<TrolleyItem> items, double trolleyCapacity, String mode) {
        // 🔸 Aqui depois vais substituir esta lista por items vindos da USEI02
        List<TrolleyItem> itemsCopy = new ArrayList<>(items);

        // Ordenação conforme a heurística
        if (mode.equalsIgnoreCase("FFD") || mode.equalsIgnoreCase("BFD")) {
            itemsCopy.sort(Comparator.comparingDouble(TrolleyItem::getWeight).reversed());
        }

        List<TrolleyPlan> trolleys = new ArrayList<>();

        for (TrolleyItem item : itemsCopy) {
            boolean placed = false;

            // 🔹 FF e FFD → tenta colocar no primeiro trolley onde couber
            if (mode.equalsIgnoreCase("FF") || mode.equalsIgnoreCase("FFD")) {
                for (TrolleyPlan trolley : trolleys) {
                    if (trolley.tryAdd(item)) {
                        placed = true;
                        break;
                    }
                }
            }
            // 🔹 BFD → coloca no trolley que ficar mais cheio sem ultrapassar
            else if (mode.equalsIgnoreCase("BFD")) {
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

            // 🔸 Se não coube em nenhum trolley → cria novo
            if (!placed) {
                TrolleyPlan newTrolley = new TrolleyPlan(trolleys.size() + 1, trolleyCapacity);
                newTrolley.tryAdd(item);
                trolleys.add(newTrolley);
            }
        }

        return trolleys;
    }

    /**
     * Mostra um relatório simples dos trolleys e respetiva utilização.
     */
    public void displayPlan(List<TrolleyPlan> trolleys) {
        System.out.println("=== Picking Plan Summary ===");
        for (TrolleyPlan trolley : trolleys) {
            System.out.println(trolley);
            for (TrolleyItem item : trolley.getItems()) {
                System.out.println("   -> " + item);
            }
        }
    }

    // 🔸 Exemplo de teste temporário (para correr antes da integração com USEI02)
    public static void main(String[] args) {
        List<TrolleyItem> items = new ArrayList<>();
        items.add(new TrolleyItem("O001", "SKU-A", 5, 10.0, 1, 1));
        items.add(new TrolleyItem("O001", "SKU-B", 3, 25.0, 1, 2));
        items.add(new TrolleyItem("O002", "SKU-C", 10, 15.0, 2, 1));
        items.add(new TrolleyItem("O003", "SKU-D", 2, 50.0, 3, 3));

        PickingPlanner planner = new PickingPlanner();

        List<TrolleyPlan> ff = planner.generatePickingPlan(items, 60.0, "FF");
        System.out.println("\n--- First Fit ---");
        planner.displayPlan(ff);

        List<TrolleyPlan> ffd = planner.generatePickingPlan(items, 60.0, "FFD");
        System.out.println("\n--- First Fit Decreasing ---");
        planner.displayPlan(ffd);

        List<TrolleyPlan> bfd = planner.generatePickingPlan(items, 60.0, "BFD");
        System.out.println("\n--- Best Fit Decreasing ---");
        planner.displayPlan(bfd);
    }
}

