package pt.ipp.isep.dei.pi25_26.g093.app.utilities;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class CSVParserTest {

    private File createSimpleCsv() throws Exception {
        File temp = File.createTempFile("csvparser", ".csv");
        PrintWriter pw = new PrintWriter(temp);
        pw.println("id,name");
        pw.println("1,Ana");
        pw.println("2,Beto");
        pw.flush();
        pw.close();
        temp.deleteOnExit();
        return temp;
    }

    @Test
    public void testParseSimpleCsv() throws Exception {
        File f = createSimpleCsv();
        CSVParser parser = new CSVParser(f.getAbsolutePath());

        List<Map<String, String>> data = parser.getCSVData();
        assertEquals(2, data.size());
        assertEquals("Ana", data.get(0).get("name"));
        assertEquals("2", data.get(1).get("id"));
    }

    @Test(expected = FileNotFoundException.class)
    public void testThrowsWhenFileNotFound() throws Exception {
        new CSVParser("non_existing_file.csv");
    }
}
