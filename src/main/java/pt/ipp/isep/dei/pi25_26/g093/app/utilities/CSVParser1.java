package pt.ipp.isep.dei.pi25_26.g093.app.utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Class that Parses most CSV files into a List of Maps. (Handles quoted values with commas)
 */
public class CSVParser1 {

    private final List<Map<String, String>> csvData = new ArrayList<>();

    /**
     * Constructor that parses the CSV file located in the given path.
     * @param csvPath the path of the CSV file.
     * @throws FileNotFoundException if the file is not found.
     */
    public CSVParser1(String csvPath) throws FileNotFoundException {

        Scanner scanner = new Scanner(new File(csvPath));

        String headerLine = scanner.nextLine();
        List<String> headers = parseCsvLine(headerLine);

        while (scanner.hasNext()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            List<String> values = parseCsvLine(line);

            Map<String, String> row = new HashMap<>();

            int limit = Math.min(headers.size(), values.size());
            for (int i = 0; i < limit; i++) {
                row.put(headers.get(i), values.get(i));
            }

            for (int i = limit; i < headers.size(); i++) {
                row.put(headers.get(i), "");
            }

            csvData.add(row);
        }

        scanner.close();
    }

    /**
     * Parses a single CSV line into tokens, handling quoted values.
     * @param line the CSV line.
     * @return the list of tokens.
     */
    private List<String> parseCsvLine(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                inQuotes = !inQuotes;
            }
            else if (c == ',' && !inQuotes) {
                tokens.add(current.toString().trim());
                current.setLength(0);
            }
            else {
                current.append(c);
            }
        }

        tokens.add(current.toString().trim());
        return tokens;
    }

    /**
     * Returns the parsed CSV data as a List of Maps, with the key being the header and the value being the value.
     * @return the CSV data.
     */
    public List<Map<String, String>> getCSVData() {
        return csvData;
    }
}
