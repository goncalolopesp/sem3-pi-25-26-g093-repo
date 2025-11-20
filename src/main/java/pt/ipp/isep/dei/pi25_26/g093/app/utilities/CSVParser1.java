package pt.ipp.isep.dei.pi25_26.g093.app.utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class CSVParser1 {

    private final List<Map<String, String>> csvData = new ArrayList<>();

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

    public List<Map<String, String>> getCSVData() {
        return csvData;
    }
}
