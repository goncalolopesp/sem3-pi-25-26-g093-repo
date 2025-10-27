package pt.ipp.isep.dei.pi25_26.g093.app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the PickPathService class (USEI04).
 */
class PickPathServiceTest {

    private PickPathService service;
    private TrolleyPlan plan;

    @BeforeEach
    void setUp() {
        service = new PickPathService();
        plan = new TrolleyPlan(1, 100);

        // Adicionar itens de picking (orderId, sku, qty, weight, aisle, bay)
        plan.tryAdd(new TrolleyItem("ORD001", "SKU01", 5, 10.0, 1, 8));
        plan.tryAdd(new TrolleyItem("ORD001", "SKU02", 3, 8.0, 2, 2));
        plan.tryAdd(new TrolleyItem("ORD001", "SKU03", 2, 5.0, 3, 4));
    }

    @Test
    void ensureGeneratePickPathReturnsComparisonWithBothStrategies() {
        PickPathComparison comparison = service.generatePickPath(plan);

        assertNotNull(comparison, "Comparison should not be null");
        assertNotNull(comparison.getStrategyA(), "Strategy A must exist");
        assertNotNull(comparison.getStrategyB(), "Strategy B must exist");
        assertTrue(comparison.getStrategyA().getTotalDistance() > 0, "Distance A must be positive");
        assertTrue(comparison.getStrategyB().getTotalDistance() > 0, "Distance B must be positive");
    }

    @Test
    void ensureDuplicateCoordinatesAreMergedCorrectly() {
        TrolleyPlan duplicatePlan = new TrolleyPlan(2, 100);
        duplicatePlan.tryAdd(new TrolleyItem("ORD002", "SKU01", 2, 5.0, 1, 1));
        duplicatePlan.tryAdd(new TrolleyItem("ORD002", "SKU02", 3, 6.0, 1, 1)); // mesmo local
        duplicatePlan.tryAdd(new TrolleyItem("ORD002", "SKU03", 1, 3.0, 2, 3));

        PickPathComparison comparison = service.generatePickPath(duplicatePlan);
        assertNotNull(comparison);
        List<Coordinate> coords = comparison.getStrategyA().getPath();

        assertEquals(2, coords.size(), "Duplicate coordinates should be merged into one unique location");
    }

    @Test
    void ensureGeneratePickPathReturnsNullForNullPlan() {
        assertNull(service.generatePickPath(null), "Null plan should return null");
    }

    @Test
    void ensureGeneratePickPathReturnsNullForEmptyPlan() {
        TrolleyPlan emptyPlan = new TrolleyPlan(3, 100);
        assertNull(service.generatePickPath(emptyPlan), "Empty plan should return null");
    }

    @Test
    void ensureGenerateDetailedReportContainsAllSections() {
        String report = service.generateDetailedReport(plan);
        assertTrue(report.contains("PICK PATH SEQUENCING REPORT"), "Report must contain header");
        assertTrue(report.contains("STRATEGY A"), "Report must contain Strategy A section");
        assertTrue(report.contains("STRATEGY B"), "Report must contain Strategy B section");
        assertTrue(report.contains("COMPARISON"), "Report must contain comparison section");
    }

    @Test
    void ensureGenerateDetailedReportHandlesNullPlanGracefully() {
        String report = service.generateDetailedReport(null);
        assertEquals("Unable to generate picking path report (no data).", report);
    }

    @Test
    void ensureGetBestPickPathReturnsValidResult() {
        PickPathResult best = service.getBestPickPath(plan);
        assertNotNull(best, "Best path should not be null");
        assertTrue(best.getTotalDistance() > 0, "Best path should have positive distance");
    }

    @Test
    void ensureBestStrategyIsConsistentBetweenMethods() {
        PickPathComparison comparison = service.generatePickPath(plan);
        PickPathResult bestFromComparison = comparison.getBestStrategy();
        PickPathResult bestFromService = service.getBestPickPath(plan);

        assertEquals(bestFromComparison.getStrategyName(), bestFromService.getStrategyName(),
                "Best strategy should be consistent between comparison and service");
    }
}
