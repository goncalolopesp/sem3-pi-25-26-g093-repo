package pt.ipp.isep.dei.pi25_26.g093.app.utilities;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Utility to read Excel sheets into List<Map<String,String>>.
 * First row is treated as header. Uses DataFormatter to get
 * uniform string values independent of the cell's type.
 */
public class ExcelDataReader {

    private final String filePath;

    public ExcelDataReader(String filePath) {
        this.filePath = Objects.requireNonNull(filePath, "Excel file path is required");
    }

    /**
     * Reads a sheet by name and returns rows as maps: header -> value.
     */
    public List<Map<String, String>> readSheet(String sheetName) throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new IOException("Sheet not found: " + sheetName);
            }

            DataFormatter formatter = new DataFormatter();
            List<Map<String, String>> rows = new ArrayList<>();

            int firstRow = sheet.getFirstRowNum();
            int lastRow = sheet.getLastRowNum();
            if (firstRow > lastRow) return rows;

            // Read header
            Row headerRow = sheet.getRow(firstRow);
            if (headerRow == null) return rows;

            int lastCol = headerRow.getLastCellNum();
            List<String> headers = new ArrayList<>(lastCol);
            for (int c = 0; c < lastCol; c++) {
                Cell cell = headerRow.getCell(c, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                String h = (cell == null) ? "" : formatter.formatCellValue(cell).trim();
                headers.add(h);
            }

            // Read data rows
            for (int r = firstRow + 1; r <= lastRow; r++) {
                Row row = sheet.getRow(r);
                if (row == null || isRowEmpty(row)) continue;

                Map<String, String> map = new LinkedHashMap<>();
                for (int c = 0; c < headers.size(); c++) {
                    String key = headers.get(c);
                    if (key == null || key.isBlank()) continue; // ignora colunas sem header

                    Cell cell = row.getCell(c, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    String value = (cell == null) ? "" : formatter.formatCellValue(cell).trim();
                    map.put(key, value);
                }
                rows.add(map);
            }

            return rows;
        }
    }

    private static boolean isRowEmpty(Row row) {
        if (row == null) return true;
        DataFormatter f = new DataFormatter();
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            if (cell != null && !f.formatCellValue(cell).trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
