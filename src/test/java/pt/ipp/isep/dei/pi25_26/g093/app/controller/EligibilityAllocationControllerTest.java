package pt.ipp.isep.dei.pi25_26.g093.app.controller;

import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.Assert.*;

/**
 * Unit tests for the EligibilityAllocationController class (USPL02).
 */
public class EligibilityAllocationControllerTest {

    private EligibilityAllocationController controller;

    @Before
    public void setUp() {
        controller = new EligibilityAllocationController();
    }

    @Test
    public void ensureConstructorWorks() {
        assertNotNull(controller);
    }

    @Test
    public void ensureGetAllocationsReturnsEmptyListInitially() {
        assertNotNull(controller.getAllocations());
        assertTrue(controller.getAllocations().isEmpty());
    }

}
