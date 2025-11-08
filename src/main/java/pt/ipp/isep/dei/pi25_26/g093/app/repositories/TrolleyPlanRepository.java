package pt.ipp.isep.dei.pi25_26.g093.app.repositories;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.TrolleyPlan;

import java.util.ArrayList;
import java.util.List;

public class TrolleyPlanRepository {

    private final List<TrolleyPlan> plans = new ArrayList<>();

    public List<TrolleyPlan> getPlans() {
        return new ArrayList<>(plans);
    }

    public void setPlans(List<TrolleyPlan> newPlans) {
        plans.clear();
        if (newPlans != null) plans.addAll(newPlans);
    }

    public boolean isEmpty() {
        return plans.isEmpty();
    }
}

