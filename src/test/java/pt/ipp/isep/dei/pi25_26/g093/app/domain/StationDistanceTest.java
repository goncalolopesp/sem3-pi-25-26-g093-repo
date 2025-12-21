package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Locale;

public class StationDistanceTest {

    // Helper para criar estações falsas (MOCK)
    private CompleteStation createMockStation(String name) {
        return new CompleteStation(
                "Country", // 1. Country
                "",        // 2. TimeZone
                "",        // 3. TimeZoneGroup
                name,      // 4. STATION NAME
                0.0,       // 5. Latitude
                0.0,       // 6. Longitude
                false,     // 7. isCity
                false,     // 8. isMain
                false      // 9. isAirport
        );
    }

    @Test
    public void testCompareTo_DifferentDistances() {
        // Arrange
        CompleteStation s1 = createMockStation("Station A");
        CompleteStation s2 = createMockStation("Station B");

        // s1 está mais perto (10km) que s2 (20km)
        StationDistance sd1 = new StationDistance(s1, 10.0);
        StationDistance sd2 = new StationDistance(s2, 20.0);

        // Act & Assert
        // O resultado deve ser negativo porque sd1 é menor (mais perto) que sd2
        assertTrue("Menor distância deve vir primeiro na ordenação", sd1.compareTo(sd2) < 0);
    }

    @Test
    public void testCompareTo_SameDistance_SortByNameDESC() {
        // Arrange
        // Duas estações à mesma distância exata (5.0 km)
        CompleteStation sA = createMockStation("Aveiro");
        CompleteStation sZ = createMockStation("Zurich");

        StationDistance sdA = new StationDistance(sA, 5.0);
        StationDistance sdZ = new StationDistance(sZ, 5.0);

        // Act
        // O requisito pede Nome DESC (Z -> A).
        // Isto significa que "Zurich" deve aparecer ANTES de "Aveiro" na lista ordenada.
        // Em Java Comparable, "vir antes" significa que compareTo devolve um valor negativo (< 0).
        int result = sdZ.compareTo(sdA);

        // Assert
        assertTrue("Para distâncias iguais, 'Zurich' deve vir antes de 'Aveiro' (Ordenação Z-A)", result < 0);
    }

    @Test
    public void testToStringFormat() {
        CompleteStation s = createMockStation("Porto");
        StationDistance sd = new StationDistance(s, 12.3456);

        String output = sd.toString();

        // Verifica se o toString inclui o que é pedido
        assertTrue("O toString deve conter a distância arredondada (ex: 12.35)", output.contains("12.35"));
        assertTrue("O toString deve conter o nome da estação", output.contains("Porto"));
    }
}