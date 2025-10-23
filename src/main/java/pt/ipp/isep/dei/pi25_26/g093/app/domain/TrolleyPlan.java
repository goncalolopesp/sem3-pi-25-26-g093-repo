package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import java.util.ArrayList;
import java.util.List;

public class TrolleyPlan { // representa um trolley, (kg)

    private final int trolleyNo;       // Número identificador do trolley
    private final double capacityKg;   // Capacidade máxima em kg
    private double usedWeight = 0.0;   // Peso atual ocupado
    private final List<TrolleyItem> items = new ArrayList<>(); // Lista de itens neste trolley

    public TrolleyPlan(int trolleyNo, double capacityKg) {
        this.trolleyNo = trolleyNo;
        this.capacityKg = capacityKg;
    }

    public boolean tryAdd(TrolleyItem item) {  //tenta adicionar um item ao trolley
        if (usedWeight + item.getWeight() <= capacityKg) {
            items.add(item);
            usedWeight += item.getWeight();
            return true;
        }
        return false;
    }

    public double getUtilization() { //percentagem de utilização do trolley
        if (capacityKg == 0) return 0;
        return (usedWeight / capacityKg) * 100.0;
    }

    // Getters
    public int getTrolleyNo() { return trolleyNo; }
    public double getCapacityKg() { return capacityKg; }
    public double getUsedWeight() { return usedWeight; }
    public List<TrolleyItem> getItems() { return items; }

    public String toString() { //descrição do trolley
        return String.format("Trolley #%d [%.1f / %.1f kg = %.1f%% full]",
                trolleyNo, usedWeight, capacityKg, getUtilization());
    }
}

