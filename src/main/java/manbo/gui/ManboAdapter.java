package manbo.gui;

import manbo.Manbo;

public class ManboAdapter {
    private final Manbo core = new Manbo();

    /** Returns the text reply produced by the CLI pipeline. */
    public String getResponse(String input) {
        Manbo.Reply r = core.handle(input);   // calls Parser/Command/Storage via Ui
        return r.text;
    }

    /** Optional: let the GUI know if the user asked to exit. */
    public boolean isExit(String input) {
        return core.handle(input).exit;
    }
}
