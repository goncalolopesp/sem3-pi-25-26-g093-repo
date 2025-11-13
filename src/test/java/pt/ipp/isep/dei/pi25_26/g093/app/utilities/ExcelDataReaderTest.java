package pt.ipp.isep.dei.pi25_26.g093.app.utilities;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Unit tests for the ExcelDataReader class (USPL02) — JUnit 4 version.
 */
public class ExcelDataReaderTest {

    @Test(expected = NullPointerException.class)
    public void ensureConstructorThrowsExceptionForNullPath() throws IOException {
        new ExcelDataReader(null);
    }

    @Test
    public void ensureFileNotFoundThrowsException() {
        try {
            ExcelDataReader reader = new ExcelDataReader("nonexistent/file.xlsx");
            try {
                reader.readSheet("test");
                fail("Expected IOException due to missing file");
            } catch (IOException e) {
                // expected
            } finally {
                reader.close();
            }
        } catch (IOException e) {
            // também aceitável — o construtor pode lançar
        }
    }

    @Test
    public void ensureSheetNotFoundThrowsException() throws IOException {
        File tempFile = File.createTempFile("test", ".xlsx");
        tempFile.deleteOnExit();

        ExcelDataReader reader = new ExcelDataReader(tempFile.getAbsolutePath());
        try {
            reader.readSheet("nonexistent");
            fail("Expected IOException for missing sheet");
        } catch (IOException e) {
            // expected
        } finally {
            reader.close();
        }
    }

    @Test
    public void ensureCloseDoesNotThrowExceptionWhenCalledMultipleTimes() throws IOException {
        File tempFile = File.createTempFile("test", ".xlsx");
        tempFile.deleteOnExit();

        ExcelDataReader reader = new ExcelDataReader(tempFile.getAbsolutePath());
        reader.close();
        try {
            reader.close(); // segunda chamada
        } catch (Exception e) {
            fail("close() should not throw an exception when called multiple times");
        }
    }
}
