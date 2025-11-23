package pt.ipp.isep.dei.pi25_26.g093.app.utilities;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class CSVParser1Test {

    private File createCsvWithQuotes() throws Exception {
        File temp = File.createTempFile("csvparser1", ".csv");
        PrintWriter pw = new PrintWriter(temp);
        pw.println("id,name,comment");
        pw.println("1,\"Ana Maria\",\"Olá, tudo bem?\"");
        pw.println("2,Beto,Sem comentario");
        pw.flush();
        pw.close();
        temp.deleteOnExit();
        return temp;
    }

    @Test
    public void testParseCsvWithQuotedValues() throws Exception {
        File f = createCsvWithQuotes();
        CSVParser1 parser = new CSVParser1(f.getAbsolutePath());

        List<Map<String, String>> data = parser.getCSVData();
        assertEquals(2, data.size());
        Map<String, String> row1 = data.get(0);

        assertEquals("1", row1.get("id"));
        assertEquals("Ana Maria", row1.get("name"));
        assertEquals("Olá, tudo bem?", row1.get("comment"));
    }

    @Test(expected = FileNotFoundException.class)
    public void testThrowsWhenFileNotFound() throws Exception {
        new CSVParser1("non_existing_file.csv");
    }
}
