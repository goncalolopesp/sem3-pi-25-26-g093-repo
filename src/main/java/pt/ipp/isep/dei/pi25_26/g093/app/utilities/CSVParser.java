package pt.ipp.isep.dei.pi25_26.g093.app.utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Class that Parses most CSV files into a List of Maps. (Non-usable with CSV files that include commas in the values)
 */
public class CSVParser {

    private List<Map<String, String>> csvData;

    /**
     * Constructor that parses the CSV file located in the given directory.
     * @param csvDirectory the directory of the CSV file.
     * @throws FileNotFoundException if the file is not found.
     */
    public CSVParser(String csvDirectory) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(csvDirectory));
        String headerData = scanner.nextLine();
        List<String> headers = new ArrayList<>();
        Map<String, String> csvDataMap = new HashMap<>();
        csvData = new ArrayList<>();
        for (String header : headerData.split("\\s*[,;]\\s*")){
            headers.add(header);
        }
        while (scanner.hasNext()){
            String csvLine = scanner.nextLine();
            int i = 0;
            for (String data : csvLine.split("\\s*[,;]\\s*")){
                csvDataMap.put(headers.get(i), data);
                i++;
            }
            csvData.add(csvDataMap);
            csvDataMap = new HashMap<>();
        }
    }

    /**
     * Returns the parsed CSV data as a List of Maps, with the key being the header and the value being the value.
     * @return the CSV data.
     */
    public List<Map<String, String>> getCSVData(){
        return csvData;
    }
}
