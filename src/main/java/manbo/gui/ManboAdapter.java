// manbo/gui/ManboAdapter.java
package manbo.gui;

import manbo.Manbo;
import manbo.exceptions.UnrecognisedInputException;

public class ManboAdapter {
    private final Manbo core = new Manbo();

    /**
     * Returns the reply text for input.
     * If the reply looks like an unknown-command message, rethrow as UnrecognisedInputException
     * so the GUI can swap the avatar to the thinking GIF.
     */
    public String getResponse(String input) throws UnrecognisedInputException {
        Manbo.Reply r = core.handle(input);   // no changes outside gui
        String text = r.text;

        // Match the standard message your UnrecognisedInputException uses
        // (keep this prefix in sync with your exception message)
        if (text != null && text.startsWith("I don't understand your input")) {
            // Let the GUI handle this specific case
            throw new UnrecognisedInputException(input);
        }
        return text;
    }

    // If you use this in GUI:
    public boolean isExit(String input) {
        return core.handle(input).isExit;
    }
}
