package pt.ipp.isep.dei.pi25_26.g093.app.utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class CSVParser {

    private List<Map<String, String>> csvData;

    public CSVParser(String csvDirectory) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(csvDirectory));
        String headerData = scanner.nextLine();
        List<String> headers = new ArrayList<>();
        Map<String, String> csvDataMap = new HashMap<>();
        csvData = new ArrayList<>();
        for (String header : headerData.split(",")){
            headers.add(header);
        }
        while (scanner.hasNext()){
            String csvLine = scanner.nextLine();
            int i = 0;
            for (String data : csvLine.split(",")){
                csvDataMap.put(headers.get(i), data);
                i++;
            }
            csvData.add(csvDataMap);
            csvDataMap = new HashMap<>();
        }
    }

    public List<Map<String, String>> getCSVData(){
        return csvData;
    }
}
