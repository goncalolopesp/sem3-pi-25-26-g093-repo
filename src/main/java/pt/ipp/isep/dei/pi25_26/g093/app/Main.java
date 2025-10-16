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

public class Main {


    public static void main(String[] args) throws FileNotFoundException {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.run();
        MainMenuUI mainMenuUI = new MainMenuUI();
        mainMenuUI.showMainMenu();
    }
}
