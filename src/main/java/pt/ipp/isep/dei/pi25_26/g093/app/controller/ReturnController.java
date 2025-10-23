package pt.ipp.isep.dei.pi25_26.g093.app.controller;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.ReturnItem;
import pt.ipp.isep.dei.pi25_26.g093.app.utilities.CSVParser;
import pt.ipp.isep.dei.pi25_26.g093.app.service.ReturnService;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReturnController {

    public List<ReturnItem> loadReturnItems() throws FileNotFoundException {

        String path = "train_station_dataset/returns.csv";

        List<ReturnItem> returnItems = new ArrayList<>();

        try{

            CSVParser parser = new CSVParser(path);

            List<Map<String, String>> data = parser.getCSVData();

            for (Map<String, String> row : data) {

                ReturnItem itemCSV = ReturnItem.fromCSV(row);

                returnItems.add(itemCSV);
            }
        }catch(FileNotFoundException e){

            System.out.println("File not found");

        }

        return returnItems;
    }
}
