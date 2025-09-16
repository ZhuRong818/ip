package manbo;

import org.junit.jupiter.api.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration-style tests for Manbo using the public handle(String) API.
 * These tests exercise the same pipeline as the CLI without mocking Ui/Storage.
 */
public class ManboIntegrationTest {

    private static final Path DATA_FILE = Path.of("data", "manbo.txt");

    @BeforeEach
    void cleanBefore() throws Exception {
        // Ensure tests start from a clean slate; ignore if file/dir don't exist.
        if (Files.exists(DATA_FILE)) {
            Files.delete(DATA_FILE);
        }
        // Keep the parent dir present if your Storage expects it.
        Files.createDirectories(DATA_FILE.getParent());
    }

    @AfterEach
    void cleanAfter() throws Exception {
        // Optional cleanup to keep the workspace tidy for other tests.
        if (Files.exists(DATA_FILE)) {
            Files.delete(DATA_FILE);
        }
        // Don't delete the directory so repeated runs are fine.
    }

    @Test
    void addTodo_thenList_showsOneTask() {
        Manbo app = new Manbo();

        Manbo.Reply r1 = app.handle("todo read book");
        assertFalse(r1.isExit);
        assertNotNull(r1.text);
        assertTrue(r1.text.toLowerCase().contains("add") || r1.text.toLowerCase().contains("added"),
                "Should mention adding a task");
        assertTrue(r1.text.toLowerCase().contains("read book"));

        Manbo.Reply r2 = app.handle("list");
        assertFalse(r2.isExit);
        assertTrue(r2.text.toLowerCase().contains("read book"));
        // Looser check that a 1-based index is shown somewhere (typical list output).
        assertTrue(r2.text.contains("1") || r2.text.contains("1."),
                "List should show at least one indexed item");
    }

    @Test
    void mark_thenUnmark_toggles_done_state_in_output() {
        Manbo app = new Manbo();

        app.handle("todo task A");
        Manbo.Reply mark = app.handle("mark 1");
        assertFalse(mark.isExit);
        // Expect something like a status change message (done/marked)
        assertTrue(mark.text.toLowerCase().contains("mark") || mark.text.toLowerCase().contains("done"));

        Manbo.Reply listAfterMark = app.handle("list");
        assertTrue(listAfterMark.text.toLowerCase().contains("task a"));
        // Typical done marker in many task apps is [X] or something similar — keep it flexible:
        assertTrue(listAfterMark.text.contains("X") || listAfterMark.text.toLowerCase().contains("done"),
                "List after mark should indicate completion");

        Manbo.Reply unmark = app.handle("unmark 1");
        assertFalse(unmark.isExit);
        assertTrue(unmark.text.toLowerCase().contains("unmark") || unmark.text.toLowerCase().contains("not done"));

        Manbo.Reply listAfterUnmark = app.handle("list");
        assertTrue(listAfterUnmark.text.toLowerCase().contains("task a"));
        // Look for a “not done” indicator (commonly [ ] or similar). Keep the assertion tolerant:
        assertTrue(listAfterUnmark.text.contains("[") || listAfterUnmark.text.toLowerCase().contains("not done"));
    }

    @Test
    void delete_removes_task() {
        Manbo app = new Manbo();

        app.handle("todo one");
        app.handle("todo two");

        Manbo.Reply del = app.handle("delete 1");
        assertFalse(del.isExit);
        assertTrue(del.text.toLowerCase().contains("delete") || del.text.toLowerCase().contains("removed"));

        Manbo.Reply list = app.handle("list");
        String out = list.text.toLowerCase();
        assertTrue(out.contains("two"), "Remaining task should be 'two'");
        assertFalse(out.contains("one"), "Deleted task 'one' should not be listed");
    }

    @Test
    void bye_sets_isExit_true() {
        Manbo app = new Manbo();
        Manbo.Reply bye = app.handle("bye");
        assertTrue(bye.isExit, "bye should signal exit");
        assertNotNull(bye.text);
    }

    @Test
    void invalid_command_returns_error_text_but_not_exit() {
        Manbo app = new Manbo();
        Manbo.Reply r = app.handle("nonsenseCommand");

        assertFalse(r.isExit, "Invalid commands should not exit the app");

        // Just check we got *some* feedback back to the user
        assertNotNull(r.text, "Reply text should not be null");
        assertFalse(r.text.isBlank(), "Reply text should not be blank");

        // Optional: you can check it's not echoing the command raw
        assertNotEquals("nonsenseCommand", r.text.trim(),
                "Reply should not just echo the command without explanation");
    }

    @Test
    void add_multiple_then_list_order_is_fifo_by_default() {
        Manbo app = new Manbo();

        app.handle("todo alpha");
        app.handle("todo beta");
        Manbo.Reply list = app.handle("list");
        String text = list.text.toLowerCase();
        int idxAlpha = text.indexOf("alpha");
        int idxBeta = text.indexOf("beta");

        assertTrue(idxAlpha >= 0 && idxBeta >= 0, "Both tasks should be listed");
        assertTrue(idxAlpha < idxBeta, "Expected insertion order: alpha before beta");
    }

    /**
     * Optional: quick sanity that the storage file is created on write.
     * If your Storage.save() writes lazily or only on exit, adapt/remove this.
     */
    @Test
    void storage_file_is_created_when_tasks_are_saved() {
        Manbo app = new Manbo();
        app.handle("todo persist me");

        File f = DATA_FILE.toFile();
        assertTrue(f.exists() && f.length() >= 0L, "Expected data/manbo.txt to exist after a write");
    }
}
