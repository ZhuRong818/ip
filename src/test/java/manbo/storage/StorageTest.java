package manbo.storage;

import manbo.task.Todo;
import manbo.task.Task;
import org.junit.jupiter.api.*;
import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the save/load behavior of Storage with a temp file.
 */
public class StorageTest {
    private static final String TEST_FILE = "data/test-storage.txt";
    private Storage storage;

    @BeforeEach
    void setUp() {
        // Use a separate test file
        storage = new Storage(TEST_FILE);

        // Clean up any leftover content before test
        File f = new File(TEST_FILE);
        if (f.exists()) {
            f.delete();
        }
    }

    @AfterEach
    void tearDown() {
        // Remove test file after test
        File f = new File(TEST_FILE);
        if (f.exists()) {
            f.delete();
        }
    }

    @Test
    void saveAndLoad_roundTrip_success() {
        Todo t1 = new Todo("read book");
        Todo t2 = new Todo("write tests");
        t2.markAsDone();

        storage.save(List.of(t1, t2));
        List<Task> loaded = storage.load();

        assertEquals(2, loaded.size(), "Should load back 2 tasks");
        assertEquals(t1.toSaveFormat(), loaded.get(0).toSaveFormat());
        assertEquals(t2.toSaveFormat(), loaded.get(1).toSaveFormat());
    }
}
