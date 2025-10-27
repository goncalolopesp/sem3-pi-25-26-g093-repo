package pt.ipp.isep.dei.pi25_26.g093.app.repositories;

import org.junit.Test;

import static org.junit.Assert.*;

public class RepositoriesTest {

    @Test
    public void testSingletonInstance() {
        Repositories r1 = Repositories.getInstance();
        Repositories r2 = Repositories.getInstance();
        assertSame(r1, r2);
    }

    @Test
    public void testGetRepositoriesNotNull() {
        Repositories repo = Repositories.getInstance();
        assertNotNull(repo.getWagonRepository());
        assertNotNull(repo.getWarehouseRepository());
    }
}
