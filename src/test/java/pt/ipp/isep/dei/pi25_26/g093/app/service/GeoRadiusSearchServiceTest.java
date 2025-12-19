package pt.ipp.isep.dei.pi25_26.g093.app.service;

import org.junit.Before;
import org.junit.Test;
import pt.ipp.isep.dei.pi25_26.g093.app.datastructures.AVL;
import pt.ipp.isep.dei.pi25_26.g093.app.datastructures.Tree;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.CompleteStation;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.StationDistance;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class GeoRadiusSearchServiceTest {

    private GeoRadiusSearchService service;
    private Tree<Double> mockKdTree;

    @Before
    public void setUp() {
        // 1. Criar uma KD-Tree real (vazia) com k=2 dimensões
        mockKdTree = new Tree<>(2);

        // 2. Criar Estações de Teste
        addStationToTree("Station Close", 40.004, -8.0, "PT", true);
        addStationToTree("Station Border", 40.08, -8.0, "ES", false);
        addStationToTree("Station Far", 41.0, -8.0, "FR", true);

        // 3. Inicializar o Serviço com a árvore populada
        service = new GeoRadiusSearchService(mockKdTree);
    }

    // Método auxiliar corrigido
    private void addStationToTree(String name, double lat, double lon, String country, boolean isCity) {

        // 1. Criar a Estação
        // CORREÇÃO: A ordem dos argumentos agora está correta:
        // (Country, TimeZone, TimeZoneGroup, StationName, Lat, Lon, ...)
        CompleteStation s = new CompleteStation(
                country,    // 1. Country (Antes estava 'name')
                "",         // 2. TimeZone (Antes estava 'country')
                "",         // 3. TimeZoneGroup
                name,       // 4. Station Name (Antes estava "")
                lat,        // 5. Latitude
                lon,        // 6. Longitude
                isCity,     // 7. isCity
                false,      // 8. isMainStation
                false       // 9. isAirport
        );

        // 2. Criar as coordenadas como ArrayList
        ArrayList<Double> coords = new ArrayList<>();
        coords.add(lat);
        coords.add(lon);

        // 3. Inserir na árvore
        mockKdTree.Insert(coords, s);
    }

    @Test
    public void testSearch_FindsStationsInsideRadius() {
        // Act
        GeoRadiusSearchService.Result result = service.search(40.0, -8.0, 10.0);
        AVL<StationDistance> tree = result.getStationsTree();

        // Assert
        assertFalse("A árvore de resultados não devia estar vazia", tree.isEmpty());

        int count = 0;
        for (StationDistance sd : tree.inOrder()) {
            count++;
        }

        assertEquals("Deve encontrar exatamente 2 estações dentro de 10km", 2, count);
    }

    @Test
    public void testSearch_CalculatesStatsCorrectly() {
        // Act
        GeoRadiusSearchService.Result result = service.search(40.0, -8.0, 10.0);

        // Assert
        Integer countPT = result.getCountryCounts().get("PT");
        Integer countES = result.getCountryCounts().get("ES");

        assertNotNull("Deve haver contagem para PT", countPT);
        assertNotNull("Deve haver contagem para ES", countES);

        assertEquals("Deve haver 1 estação PT", 1, countPT.intValue());
        assertEquals("Deve haver 1 estação ES", 1, countES.intValue());
    }

    @Test
    public void testSearch_NoStationsFound() {
        // Act
        GeoRadiusSearchService.Result result = service.search(0.0, 0.0, 5.0);

        // Assert
        assertTrue("A árvore deve vir vazia", result.getStationsTree().isEmpty());
        assertEquals("Contagem de cidades deve ser 0", 0, result.getCityCount());
    }
}