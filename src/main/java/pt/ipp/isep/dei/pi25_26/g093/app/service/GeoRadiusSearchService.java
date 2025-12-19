package pt.ipp.isep.dei.pi25_26.g093.app.service;

import pt.ipp.isep.dei.pi25_26.g093.app.datastructures.AVL;
import pt.ipp.isep.dei.pi25_26.g093.app.datastructures.Tree;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.CompleteStation;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.StationDistance;

import java.util.*;

public class GeoRadiusSearchService {

    private final Tree<Double> kdTree;

    /**
     * O Service precisa da KD-Tree para funcionar.
     */
    public GeoRadiusSearchService(Tree<Double> kdTree) {
        this.kdTree = kdTree;
    }

    /** ================= RESULT STRUCT ================= */
    public static class Result { // D T O

        // CORREÇÃO: AVL leva apenas 1 tipo genérico
        private final AVL<StationDistance> stationsTree;
        private final Map<String, Integer> countryCounts;
        private final int cityCount;
        private final int nonCityCount;

        public Result(AVL<StationDistance> stationsTree,
                      Map<String, Integer> countryCounts,
                      int cityCount,
                      int nonCityCount) {
            this.stationsTree = stationsTree;
            this.countryCounts = countryCounts;
            this.cityCount = cityCount;
            this.nonCityCount = nonCityCount;
        }

        public AVL<StationDistance> getStationsTree() { return stationsTree; }
        public Map<String, Integer> getCountryCounts() { return countryCounts; }
        public int getCityCount() { return cityCount; }
        public int getNonCityCount() { return nonCityCount; }
    }

    /** =================== SEARCH LOGIC ====================== */
    public Result search(double lat, double lon, double radiusKm) {

        if (kdTree == null || kdTree.isEmpty()) // criei a caixa para traduzir o 'Raio' em limites (min/max) que a KD-Tree entende
            throw new IllegalStateException("KD-Tree not built.");

        // 1 grau de latitude ~= 111 km
        double latDelta = radiusKm / 111.0;
        // Ajuste da longitude consoante a latitude (cosseno)
        double lonDelta = radiusKm / (111.0 * Math.cos(Math.toRadians(lat)));

        ArrayList<Double> lower = new ArrayList<>();
        lower.add(lat - latDelta);// Latitude Mínima mais a sul
        lower.add(lon - lonDelta);// Longitude Mínima mais a oeste

        ArrayList<Double> upper = new ArrayList<>();
        upper.add(lat + latDelta);// Latitude Máxima mais a norte
        upper.add(lon + lonDelta);// Longitude Máxima mais a este

        // 2. PESQUISA RÁPIDA (KD-Tree)
        // O método RangeSearch devolve os nós dentro do quadrado
        List<Tree.TreeNode<Double>> candidates = kdTree.RangeSearch(lower, upper);//

        AVL<StationDistance> resultAVL = new AVL<>();

        Map<String, Integer> perCountry = new HashMap<>();
        int cityCount = 0;
        int nonCityCount = 0;

        // 3. VERIFICAÇÃO EXATA (Haversine)
        for (Tree.TreeNode<Double> node : candidates) { // 'candidates' é a lista que veio da KD-Tree (a Bounding Box quadrada)

            //cada nó contém uma coleção de objetos (payloads) e eu percorro essa coleção para garantir que processo todas as estações
            Collection<Object> payloads = node.getPayloads();

            for (Object obj : payloads) {
                if (obj instanceof CompleteStation) {
                    CompleteStation s = (CompleteStation) obj;

                    double d = distanceKm(lat, lon, s.getLatitude(), s.getLongitude()); // serve para filtar quadrado-circulo

                    if (d <= radiusKm) {
                        // A AVL ordena automaticamente ao inserir baseada no compareTo do StationDistance
                        resultAVL.insert(new StationDistance(s, d));

                        // Atualiza Estatísticas
                        perCountry.put(s.getCountry(), perCountry.getOrDefault(s.getCountry(), 0) + 1);

                        if (s.isCity()) cityCount++;
                        else nonCityCount++;
                    }
                }
            }
        }

        return new Result(resultAVL, perCountry, cityCount, nonCityCount);
    }

    /**
     * Fórmula de Haversine para precisão em km.
     */
    private double distanceKm(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371.0; // Raio da Terra
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}
 /**
  * a complexidade temporal da minha search é 0( (raiz de N) + K), onde N é o numero de estações
  * K é numero de estações encontradas no quadrado
  * raiz de N É o tempo que demoras a ENCONTRAR os nós. (O esforço de navegar na árvore).
  * K é o número de estações que realmente encontraste (o output).
  * A A Raiz divide em Lat < x e x > 40
  */