package pt.ipp.isep.dei.pi25_26.g093.app.ui;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.LineSegment;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.Locomotive;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.Station;
import pt.ipp.isep.dei.pi25_26.g093.app.service.TravelTimeService;

import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;

/**
 * Console UI for LAPR3 - USPL03: Estimated Travel Time Between Two Stations.
 * Now reads data from an Excel file chosen by the user.
 */
public class TravelTimeUI {

    public void run() {
        System.out.println("\n=== USPL03 - Travel Time Between Stations ===");

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Excel file path: ");
        String filePath = sc.nextLine().trim();

        TravelTimeService service = new TravelTimeService();

        try {
            service.loadFromExcel(filePath);
        } catch (IOException e) {
            System.out.println("Error reading Excel file: " + e.getMessage());
            return;
        }

        System.out.print("Origin station (id or name): ");
        String sA = sc.nextLine().trim();
        Optional<Station> originOpt = service.findStationByIdOrName(sA);
        if (originOpt.isEmpty()) {
            System.out.println("Station not found: " + sA);
            return;
        }

        System.out.print("Destination station (id or name): ");
        String sB = sc.nextLine().trim();
        Optional<Station> destOpt = service.findStationByIdOrName(sB);
        if (destOpt.isEmpty()) {
            System.out.println("Station not found: " + sB);
            return;
        }

        Station origin = originOpt.get();
        Station destination = destOpt.get();

        System.out.print("Locomotive id: ");
        String locoId = sc.nextLine().trim();
        Optional<Locomotive> locoOpt = service.findLocoById(locoId);
        if (locoOpt.isEmpty()) {
            System.out.println("Locomotive not found: " + locoId);
            return;
        }
        Locomotive loco = locoOpt.get();

        Optional<LineSegment> segOpt = service.findDirectSegment(origin, destination);
        if (segOpt.isEmpty()) {
            System.out.println("No direct connection between " + origin + " and " + destination + ".");
            System.out.println("Only direct connections are supported in USPL03.");
            return;
        }
        LineSegment seg = segOpt.get();

        try {
            double hours = service.calculateTravelTimeHours(origin, destination, loco, seg);
            String pretty = TravelTimeService.formatH(hours);

            System.out.println("\n--- RESULT ---");
            System.out.println("Segment:      " + seg);
            System.out.println("Locomotive:   " + loco);
            System.out.printf("Effective speed: %.0f km/h (min(loco %.0f, segment %.0f))%n",
                    Math.min(loco.getMaxSpeedKmH(), seg.getSpeedLimitKmH()),
                    loco.getMaxSpeedKmH(), seg.getSpeedLimitKmH());
            System.out.printf("Distance:     %.1f km%n", seg.getLengthKm());
            System.out.println("Travel time:  " + pretty);
            System.out.println("--------------");

        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
