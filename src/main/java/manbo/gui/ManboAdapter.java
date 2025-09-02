package manbo.gui;

/** Temporary adapter. Replace echo logic with real Manbo parsing soon. */
public class ManboAdapter {
    public String getResponse(String input) {
        // TODO: later call your Parser/TaskList/Storage pipeline and return its message
        if ("bye".equalsIgnoreCase(input.trim())) {
            return "Bye. Hope to see you again soon!";
        }
        return "Manbo heard: " + input;
    }
}
