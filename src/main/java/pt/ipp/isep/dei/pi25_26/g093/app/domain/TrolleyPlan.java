package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa um trolley (carrinho) com limite de capacidade em kg.
 * Contém uma lista de TrolleyItem (produtos alocados a este trolley).
 * Esta classe é utilizada pela USEI03 (Picking Planner).
 */
public class TrolleyPlan {

    private final int trolleyNo;       // Número identificador do trolley
    private final double capacityKg;   // Capacidade máxima em kg
    private double usedWeight = 0.0;   // Peso atual ocupado
    private final List<TrolleyItem> items = new ArrayList<>(); // Lista de itens neste trolley

    /**
     * Construtor do TrolleyPlan
     * @param trolleyNo número identificador do trolley
     * @param capacityKg capacidade máxima em kg
     */
    public TrolleyPlan(int trolleyNo, double capacityKg) {
        this.trolleyNo = trolleyNo;
        this.capacityKg = capacityKg;
    }

    /**
     * Tenta adicionar um item ao trolley.
     * @param item item a adicionar
     * @return true se couber dentro da capacidade; false caso contrário
     */
    public boolean tryAdd(TrolleyItem item) {
        if (usedWeight + item.getWeight() <= capacityKg) {
            items.add(item);
            usedWeight += item.getWeight();
            return true;
        }
        return false;
    }

    /**
     * Percentagem de utilização do trolley.
     * @return percentagem (0–100)
     */
    public double getUtilization() {
        if (capacityKg == 0) return 0;
        return (usedWeight / capacityKg) * 100.0;
    }

    // Getters
    public int getTrolleyNo() { return trolleyNo; }
    public double getCapacityKg() { return capacityKg; }
    public double getUsedWeight() { return usedWeight; }
    public List<TrolleyItem> getItems() { return items; }

    /**
     * Descrição textual do trolley (para imprimir no relatório ou consola)
     */
    @Override
    public String toString() {
        return String.format("Trolley #%d [%.1f / %.1f kg = %.1f%% full]",
                trolleyNo, usedWeight, capacityKg, getUtilization());
    }
}

