package pt.ipp.isep.dei.pi25_26.g093.app.ui;

import pt.ipp.isep.dei.pi25_26.g093.app.controller.WagonController;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.Date;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.DateTime;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.Wagon;

import java.util.*;

public class WagonUI {
    private static final String MAX_DATE_STRING = "31-12-9999";

    private static Comparator<String> dateStringComparator = (s1, s2) -> {
        // Treat MAX_DATE_STRING as largest
        boolean m1 = s1.equals(MAX_DATE_STRING);
        boolean m2 = s2.equals(MAX_DATE_STRING);
        if (m1 && !m2) return 1;
        if (!m1 && m2) return -1;
        // fall back to try to compare by parsing via Date.compareTo using Date objects
        Date d1 = new Date(s1);
        Date d2 = new Date(s2);
        return d1.compareTo(d2);
    };

    public void run(){
        System.out.println("WagonUI");
        WagonController wg = new WagonController();

        // 1) Group wagons by expiry date string (canonical key) using a TreeMap for sorted expiry order
        Map<String, List<Wagon>> wagonsByExpiryStr = new TreeMap<>(dateStringComparator);
        for (Wagon w : wg.getWagons()) {
            String expiryStr = new Date(w.getCargo().getExpiryDate()).toString();
            wagonsByExpiryStr.computeIfAbsent(expiryStr, k -> new ArrayList<>()).add(w);
        }

        // 2) Within each expiry group, sort the list by full received DateTime (stable)
        for (List<Wagon> list : wagonsByExpiryStr.values()) {
            list.sort((w1, w2) -> {
                DateTime r1 = new DateTime(w1.getCargo().getReceivedAt());
                DateTime r2 = new DateTime(w2.getCargo().getReceivedAt());
                int cmp = r1.compareTo(r2);
                if (cmp != 0) return cmp;
                // tie-breaker: expiry (should be same) then BoxID
                return Integer.compare(w1.getCargo().getBoxID(), w2.getCargo().getBoxID());
            });
        }

        // 3) Split into nested map: expiryStr -> receivedStr -> List<Wagon>
        Map<String, Map<String, List<Wagon>>> nested = new TreeMap<>(dateStringComparator);
        for (Map.Entry<String, List<Wagon>> e : wagonsByExpiryStr.entrySet()) {
            String expiryStr = e.getKey();
            Map<String, List<Wagon>> byReceived = new TreeMap<>(dateStringComparator);
            for (Wagon w : e.getValue()) {
                String receivedStr = new Date(w.getCargo().getReceivedAt()).toString();
                byReceived.computeIfAbsent(receivedStr, k -> new ArrayList<>()).add(w);
            }
            nested.put(expiryStr, byReceived);
        }

        // 4) Final: sort each final List<Wagon> by BoxID as requested
        for (Map<String, List<Wagon>> receivedMap : nested.values()) {
            for (List<Wagon> wagonList : receivedMap.values()) {
                wagonList.sort(Comparator.comparingInt(w -> w.getCargo().getBoxID()));
            }
        }

        // Print everything using Date objects for display (construct from string)
        for (Map.Entry<String, Map<String, List<Wagon>>> outer : nested.entrySet()) {
            String expiryStr = outer.getKey();
            Date expiryDate = new Date(expiryStr);
            System.out.println("Expiry Date: " + expiryDate);

            Map<String, List<Wagon>> receivedMap = outer.getValue();
            for (Map.Entry<String, List<Wagon>> inner : receivedMap.entrySet()) {
                String receivedStr = inner.getKey();
                Date receivedDate = new Date(receivedStr);
                System.out.println("  Received Date: " + receivedDate);

                for (Wagon wagon : inner.getValue()) {
                    System.out.println("    " + wagon);
                }
            }
        }
    }
}
