package pt.ipp.isep.dei.pi25_26.g093.app.utilities;

import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class CSVParserTest {

    private File createTempCSV(String content) throws Exception {
        File tempFile = File.createTempFile("testCSV", ".csv");
        try (FileWriter fw = new FileWriter(tempFile)) {
            fw.write(content);
        }
        tempFile.deleteOnExit();
        return tempFile;
    }

    @Test
    public void testParseCommaSeparatedCSV() throws Exception {
        String csv = "boxID,sku,quantity\n" +
                "10,5,100\n" +
                "20,3,200\n";

        File file = createTempCSV(csv);
        CSVParser parser = new CSVParser(file.getAbsolutePath());
        List<Map<String, String>> data = parser.getCSVData();

        assertEquals(2, data.size());

        assertEquals("10", data.get(0).get("boxID"));
        assertEquals("5", data.get(0).get("sku"));
        assertEquals("100", data.get(0).get("quantity"));

        assertEquals("20", data.get(1).get("boxID"));
        assertEquals("3", data.get(1).get("sku"));
        assertEquals("200", data.get(1).get("quantity"));
    }

    @Test
    public void testParseSemicolonSeparatedCSV() throws Exception {
        String csv = "name;category;volume\n" +
                "Soap;Cleaning;0.3\n" +
                "TV;Electronics;5.0\n";

        File file = createTempCSV(csv);
        CSVParser parser = new CSVParser(file.getAbsolutePath());
        List<Map<String, String>> data = parser.getCSVData();

        assertEquals(2, data.size());
        assertEquals("Soap", data.get(0).get("name"));
        assertEquals("Cleaning", data.get(0).get("category"));
        assertEquals("0.3", data.get(0).get("volume"));

        assertEquals("TV", data.get(1).get("name"));
        assertEquals("Electronics", data.get(1).get("category"));
        assertEquals("5.0", data.get(1).get("volume"));
    }

    @Test
    public void testEachLineCreatesNewMap() throws Exception {
        String csv = "id,value\n" +
                "1,A\n" +
                "2,B\n";

        File file = createTempCSV(csv);
        CSVParser parser = new CSVParser(file.getAbsolutePath());
        List<Map<String, String>> data = parser.getCSVData();

        assertNotSame(data.get(0), data.get(1));
    }
}
