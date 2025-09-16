package manbo;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import manbo.command.Command;
import manbo.exceptions.ManboException;
import manbo.parser.Parser;
import manbo.storage.Storage;
import manbo.task.Deadline;
import manbo.task.Event;
import manbo.task.Task;
import manbo.task.Todo;
import manbo.ui.Ui;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ManboTest {

    /** A UI that captures messages in memory (no real I/O). */
    static class TestUi extends Ui {
        StringBuilder buf = new StringBuilder();

        @Override public void info(String msg) { buf.append("[INFO] ").append(msg).append("\n"); }
        @Override public void showError(String msg) { buf.append("[ERR] ").append(msg).append("\n"); }
        @Override public void showList(List<Task> tasks) {
            buf.append("[LIST size=").append(tasks.size()).append("]\n");
            for (int i = 0; i < tasks.size(); i++) {
                buf.append(i + 1).append(". ").append(tasks.get(i).toString()).append("\n");
            }
        }
        @Override
        public String out() {
            return buf.toString();
        }
    }

    /** A Storage that writes to a temp file and ignores errors (keeps tests fast). */
    static class NoopStorage extends Storage {
        public NoopStorage() {
            super(tempFilePath());
        }
        private static String tempFilePath() {
            try {
                Path p = Files.createTempFile("manbo-test-", ".txt");
                p.toFile().deleteOnExit();
                return p.toString();
            } catch (IOException e) {
                // Fallback to current dir if temp fails
                return "manbo-test.txt";
            }
        }
        // If you want zero disk writes during tests, you can also override save() to no-op:
        // @Override public void save(List<Task> tasks) { /* no-op for tests */ }
    }

    @Test
    void parseTodo_andExecute_addsTask() throws ManboException {
        List<Task> tasks = new ArrayList<>();
        TestUi ui = new TestUi();
        Storage storage = new NoopStorage();

        Command c = Parser.parse("todo read book");
        assertNotNull(c);

        c.execute(tasks, ui, storage);

        assertEquals(1, tasks.size());
        assertTrue(tasks.get(0) instanceof Todo);
        assertTrue(ui.out().contains("added this task"));
    }



    @Test
    void mark_then_unmark_flow() throws ManboException {
        List<Task> tasks = new ArrayList<>();
        TestUi ui = new TestUi();
        Storage storage = new NoopStorage();

        // seed one task
        Parser.parse("todo task A").execute(tasks, ui, storage);
        assertFalse(tasks.get(0).ifDone());

        Parser.parse("mark 1").execute(tasks, ui, storage);
        assertTrue(tasks.get(0).ifDone());

        Parser.parse("unmark 1").execute(tasks, ui, storage);
        assertFalse(tasks.get(0).ifDone());
    }

    @Test
    void delete_removes_task_by_index() throws ManboException {
        List<Task> tasks = new ArrayList<>();
        TestUi ui = new TestUi();
        Storage storage = new NoopStorage();

        Parser.parse("todo one").execute(tasks, ui, storage);
        Parser.parse("todo two").execute(tasks, ui, storage);
        assertEquals(2, tasks.size());

        Parser.parse("delete 1").execute(tasks, ui, storage);
        assertEquals(1, tasks.size());
        assertTrue(tasks.get(0) instanceof Todo);
        assertTrue(tasks.get(0).toString().toLowerCase().contains("two"));
    }

    @Test
    void bye_sets_isExit_true_on_command() throws ManboException {
        Command c = Parser.parse("bye");
        assertTrue(c.isExit());
    }

    @Test
    void list_outputs_current_tasks() throws ManboException {
        List<Task> tasks = new ArrayList<>();
        TestUi ui = new TestUi();
        Storage storage = new NoopStorage();

        Parser.parse("todo read").execute(tasks, ui, storage);
        Parser.parse("list").execute(tasks, ui, storage);

        String out = ui.out();
        assertTrue(out.contains("[LIST size=1]"));
        assertTrue(out.toLowerCase().contains("read"));
    }

    @Test
    void invalid_command_throws_ManboException() {
        assertThrows(ManboException.class, () -> Parser.parse("nonsenseCommand"));
    }
}
