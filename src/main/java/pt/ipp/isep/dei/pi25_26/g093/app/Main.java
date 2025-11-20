package pt.ipp.isep.dei.pi25_26.g093.app;

import pt.ipp.isep.dei.pi25_26.g093.app.controller.WagonController;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.Inventory;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.Item;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.Wagon;
import pt.ipp.isep.dei.pi25_26.g093.app.ui.MainMenuUI;
import pt.ipp.isep.dei.pi25_26.g093.app.utilities.CSVParser;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

/**
 * Class that contains the main method to run the program.
 */
public class Main {
    /**
     *
     * Runs the code, starting by running the Bootstrap class.
     *
     * @param args Arguments/Variables sent through the console when executing the .jar program.
     * @throws FileNotFoundException if there are no files in the train_station_dataset folder.
     */
    public static void main(String[] args) throws FileNotFoundException {
        Bootstrap bootstrap = Bootstrap.getInstance();
        bootstrap.run();
        MainMenuUI mainMenuUI = new MainMenuUI();
        mainMenuUI.showMainMenu();
    }
}
