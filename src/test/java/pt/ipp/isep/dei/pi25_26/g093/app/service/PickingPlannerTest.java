package pt.ipp.isep.dei.pi25_26.g093.app.service;

import org.junit.Test;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.TrolleyPlan;

import java.util.List;

import static org.junit.Assert.*;

public class PickingPlannerTest {

    @Test
    public void testGeneratePickingPlan_withFF() throws Exception {
        PickingPlanner planner = new PickingPlanner();
        double trolleyCapacity = 100.0; // Definir a capacidade do trolley
        String mode = "FF"; // Usar a heurística First Fit

        // Gerar o plano de picking
        List<TrolleyPlan> trolleys = planner.generatePickingPlan(trolleyCapacity, mode);

        // Verificar se o plano foi gerado e se contém trolleys
        assertNotNull(trolleys);
        assertTrue(trolleys.size() > 0);  // Espera-se que pelo menos um trolley seja criado

        // Verificar a distribuição de itens nos trolleys
        for (TrolleyPlan trolley : trolleys) {
            assertTrue(trolley.getUsedWeight() <= trolley.getCapacityKg());  // O peso no trolley não pode ultrapassar a capacidade
        }
    }

    @Test
    public void testGeneratePickingPlan_withFFD() throws Exception {
        PickingPlanner planner = new PickingPlanner();
        double trolleyCapacity = 100.0; // Definir a capacidade do trolley
        String mode = "FFD"; // Usar a heurística First Fit Decreasing

        // Gerar o plano de picking
        List<TrolleyPlan> trolleys = planner.generatePickingPlan(trolleyCapacity, mode);

        // Verificar se o plano foi gerado e se contém trolleys
        assertNotNull(trolleys);
        assertTrue(trolleys.size() > 0);

        // Verificar a distribuição de itens nos trolleys
        for (TrolleyPlan trolley : trolleys) {
            assertTrue(trolley.getUsedWeight() <= trolley.getCapacityKg());
        }
    }

    @Test
    public void testGeneratePickingPlan_withBFD() throws Exception {
        PickingPlanner planner = new PickingPlanner();
        double trolleyCapacity = 100.0; // Definir a capacidade do trolley
        String mode = "BFD"; // Usar a heurística Best Fit Decreasing

        // Gerar o plano de picking
        List<TrolleyPlan> trolleys = planner.generatePickingPlan(trolleyCapacity, mode);

        // Verificar se o plano foi gerado e se contém trolleys
        assertNotNull(trolleys);
        assertTrue(trolleys.size() > 0);

        // Verificar a distribuição de itens nos trolleys
        for (TrolleyPlan trolley : trolleys) {
            assertTrue(trolley.getUsedWeight() <= trolley.getCapacityKg());
        }
    }
}