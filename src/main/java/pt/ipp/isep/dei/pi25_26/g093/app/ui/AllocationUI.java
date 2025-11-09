package pt.ipp.isep.dei.pi25_26.g093.app.ui;

import pt.ipp.isep.dei.pi25_26.g093.app.controller.EligibilityAllocationController;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class AllocationUI {
    /**
     * Runs the code
     */
    public void run() throws FileNotFoundException {
        EligibilityAllocation();
    }

    /**
     * Adds wagons to Bays in Aisles in Warehouses and outputs the success or failure of the operation.
     */
    private void EligibilityAllocation() throws FileNotFoundException {

        String mode;

        Scanner sc = new Scanner(System.in);
        System.out.print("Proceed with strict mode (default) or switch to partial mode? Enter P for partial, anything else for default: ");
        mode = sc.nextLine();

        if(mode.equalsIgnoreCase("P") || mode.equalsIgnoreCase("partial"))
            mode = "partial";

        EligibilityAllocationController controller = new EligibilityAllocationController();
        boolean allocated = controller.Allocate(mode);

        if (allocated){
            System.out.println("Orders have been allocated successfully!");

            System.out.println("=== Eligibility ===");
            for(EligibilityAllocationController.Eligibility e : controller.getEligibilities()) {
                System.out.printf("%s | line %d | %s | %d/%d | %s%n",
                        e.orderId, e.lineNo, e.sku, e.allocatedQty, e.requestedQty, e.status);
            }

            System.out.println("\n=== Allocations ===");
            for(EligibilityAllocationController.Allocation a : controller.getAllocations()) {
                System.out.printf("%s | line %d | %s | qty=%d | box=%s | aisle=%s | bay=%s%n",
                        a.orderId, a.lineNo, a.sku, a.qty, a.boxId, a.aisle, a.bay);
            }
        } else {
            System.out.println("An error has occurred while allocating orders.");
        }
    }
    
}
