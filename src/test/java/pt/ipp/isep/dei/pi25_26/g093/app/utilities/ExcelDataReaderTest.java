package pt.ipp.isep.dei.pi25_26.g093.app.utilities;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the ExcelDataReader class (USPL02).
 */
class ExcelDataReaderTest {

    @Test
    void ensureConstructorThrowsExceptionForNullPath() {
        assertThrows(NullPointerException.class, () -> new ExcelDataReader(null));
    }

    @Test
    void ensureFileNotFoundThrowsException() {
        assertThrows(IOException.class, () -> {
            ExcelDataReader reader = new ExcelDataReader("nonexistent/file.xlsx");
            try {
                reader.readSheet("test");
            } finally {
                reader.close();
            }
        });
    }

    @Test
    void ensureSheetNotFoundThrowsException() throws IOException {
        File tempFile = File.createTempFile("test", ".xlsx");
        tempFile.deleteOnExit();

        // Criar um ficheiro Excel vazio (ou usar biblioteca POI para criar um real)
        ExcelDataReader reader = new ExcelDataReader(tempFile.getAbsolutePath());
        assertThrows(IOException.class, () -> reader.readSheet("nonexistent"));
        reader.close();
    }

    @Test
    void ensureCloseDoesNotThrowExceptionWhenCalledMultipleTimes() throws IOException {
        File tempFile = File.createTempFile("test", ".xlsx");
        tempFile.deleteOnExit();

        ExcelDataReader reader = new ExcelDataReader(tempFile.getAbsolutePath());
        reader.close();
        assertDoesNotThrow(() -> reader.close());
    }
}

