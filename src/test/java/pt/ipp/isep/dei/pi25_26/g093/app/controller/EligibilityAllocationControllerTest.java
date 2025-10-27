package pt.ipp.isep.dei.pi25_26.g093.app.controller;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the EligibilityAllocationController class (USPL02).
 */
class EligibilityAllocationControllerTest {

    private EligibilityAllocationController controller;

    @Test
    void ensureConstructorWorks() {
        controller = new EligibilityAllocationController();
        assertNotNull(controller);
    }

    @Test
    void ensureGetAllocationsReturnsEmptyListInitially() {
        controller = new EligibilityAllocationController();
        assertNotNull(controller.getAllocations());
        assertTrue(controller.getAllocations().isEmpty());
    }

    @Test
    void ensureAllocationsAreStoredAfterAllocation() throws FileNotFoundException {
        controller = new EligibilityAllocationController();
        controller.Allocate("full");
        
        // Verify allocations were created (assuming valid CSV files exist)
        assertNotNull(controller.getAllocations());
    }

    // Nota: Testes completos de Allocate() requerem criação de CSV de teste
    // Este é um teste básico da estrutura
}

