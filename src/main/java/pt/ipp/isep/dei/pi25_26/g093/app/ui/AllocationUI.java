package pt.ipp.isep.dei.pi25_26.g093.app.ui;

import pt.ipp.isep.dei.pi25_26.g093.app.controller.EligibilityAllocationController;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class AllocationUI {
    /**
     * Runs the code
     */
    public void run() throws FileNotFoundException {
        ElibilityAllocation();
    }

    /**
     * Allocates orders based on capacity and quantity
     */
    private void ElibilityAllocation() throws FileNotFoundException {

        String mode;

        Scanner sc = new Scanner(System.in);
        System.out.print("Proceed with strict mode (default) or switch to partial mode? Enter P for partial, anything else for default: ");
        mode = sc.nextLine();

        if(mode.equalsIgnoreCase("P") || mode.equalsIgnoreCase("partial"))
            mode = "partial";

        EligibilityAllocationController a = new EligibilityAllocationController();
        boolean allocated = a.Allocate(mode);

        if (allocated){
            System.out.println("Orders have been allocated successfully!");
        } else {
            System.out.println("An error has occurred while allocating orders.");
        }
    }
    
}
