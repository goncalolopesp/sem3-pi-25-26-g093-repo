package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Responsible for computing the picking sequence (pick path)
 * inside the warehouse, based on the coordinates (aisle, bay) of the boxes.
 * <p>
 * Implements the two strategies defined in USEI004:
 * <ul>
 *   <li><b>Strategy A</b> – Deterministic Sweep (sorted by aisle and bay)</li>
 *   <li><b>Strategy B</b> – Nearest Neighbour (greedy approach)</li>
 * </ul>
 */
public class PickPathSequencer {

    /**
     * Calculates the distance between two coordinates according to the model rules:
     * <ul>
     *   <li>If both coordinates are in the same aisle: D = |b1 - b2|</li>
     *   <li>If they are in different aisles: D = b1 + |a1 - a2| * 3 + b2</li>
     * </ul>
     *
     * @param c1 first coordinate
     * @param c2 second coordinate
     * @return distance between the two coordinates
     */
    private static int distanceCoordinates(Coordinate c1, Coordinate c2) {
        int a1 = c1.getAisle();
        int b1 = c1.getBay();
        int a2 = c2.getAisle();
        int b2 = c2.getBay();

        if (a1 == a2) {
            return Math.abs(b1 - b2);
        } else {
            return b1 + Math.abs(a1 - a2) * 3 + b2;
        }
    }

    /**
     * Computes the total distance to visit all coordinates in the given order.
     * <p>
     * The route always starts from the entrance point (0,0).
     *
     * @param coor ordered list of coordinates to visit
     * @return total distance to follow the path
     */
    public int totalDistance(List<Coordinate> coor) {
        if (coor == null || coor.isEmpty()) {
            return 0;
        }

        int totalDis = 0;
        Coordinate start = new Coordinate(0, 0);

        // Distance from entrance (0,0) to the first coordinate
        totalDis += distanceCoordinates(start, coor.get(0));

        // Add distances between consecutive coordinates
        for (int i = 0; i < coor.size() - 1; i++) {
            totalDis += distanceCoordinates(coor.get(i), coor.get(i + 1));
        }

        return totalDis;
    }

    /**
     * Strategy A — Deterministic Sweep.
     * <p>
     * Sorts the list of bays by ascending aisle and, in case of a tie,
     * by ascending bay. Then computes the total distance for that sequence.
     *
     * @param bays list of coordinates (bays) to visit
     * @return total distance according to Strategy A
     */
    public int strategy1Dis(List<Coordinate> bays) {
        if (bays == null || bays.isEmpty()) {
            return 0;
        }

        // Copy list to avoid mutating the original one
        List<Coordinate> ordered = new ArrayList<>(bays);

        // Comparator that sorts by aisle first, then by bay
        Comparator<Coordinate> comp = new Comparator<Coordinate>() {
            @Override
            public int compare(Coordinate c1, Coordinate c2) {
                if (c1.getAisle() < c2.getAisle()) {
                    return -1;
                } else if (c1.getAisle() > c2.getAisle()) {
                    return 1;
                } else {
                    // Same aisle -> compare by bay
                    return Integer.compare(c1.getBay(), c2.getBay());
                }
            }
        };

        // Sort according to Strategy A rules
        ordered.sort(comp);

        // Compute total distance after sorting
        return totalDistance(ordered);
    }

    /**
     * Strategy B — Nearest Neighbour (greedy algorithm).
     * <p>
     * Starts at the entrance (0,0) and repeatedly selects
     * the nearest unvisited bay until all have been visited.
     *
     * @param bays list of coordinates (bays) to visit
     * @return total distance according to Strategy B
     */
    public int strategy2Dis(List<Coordinate> bays) {
        if (bays == null || bays.isEmpty()) {
            return 0;
        }

        Coordinate start = new Coordinate(0, 0);
        List<Coordinate> noVisited = new ArrayList<>(bays);
        List<Coordinate> visited = new ArrayList<>();

        // Repeat until all bays are visited
        while (!noVisited.isEmpty()) {
            Coordinate nearest = null;
            int shortestDistance = Integer.MAX_VALUE;

            // Find the nearest coordinate to the current position
            for (Coordinate c : noVisited) {
                int dist = distanceCoordinates(start, c);
                if (dist < shortestDistance) {
                    shortestDistance = dist;
                    nearest = c;
                }
            }

            // Add to the final visiting sequence
            visited.add(nearest);
            // Remove from unvisited list
            noVisited.remove(nearest);
            // Update current position
            start = nearest;
        }

        // Compute the total distance of the resulting path
        return totalDistance(visited);
    }
}
