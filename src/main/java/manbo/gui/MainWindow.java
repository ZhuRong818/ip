package manbo.gui;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;


/**
 * The main chat window for the Manbo app.
 * Handles user input, displays both user and bot dialogs.
 */
public class MainWindow extends AnchorPane {
    @FXML private ScrollPane scrollPane;
    @FXML private VBox dialogContainer;
    @FXML private TextField userInput;
    @FXML private Button sendButton;

    private ManboAdapter backend;

    // Swap images if needed
    private final Image userImage = new Image(this.getClass().getResourceAsStream("/images/ManboUser1.png"));
    private final Image manboImage = new Image(this.getClass().getResourceAsStream("/images/ManboBot.png"));
    private final Image thinkingImage = new Image(this.getClass().getResourceAsStream("/images/ManboThinking.gif"));


    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Inject backend adapter. */
    public void setBackend(ManboAdapter b) {
        this.backend = b;
        dialogContainer.getChildren().add(
                DialogBox.getManboDialog("曼波，曼波～ I'm Manbo \nType something!", manboImage)
        );
    }

    /** Handles input when Enter is pressed or Send button clicked. */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        if (input == null || input.trim().isEmpty()) {
            return;
        }

        String response;
        Image botAvatar = manboImage; // default avatar

        try {
            response = backend.getResponse(input);
        } catch (manbo.exceptions.UnrecognisedInputException e) {
            response = e.getMessage();
            botAvatar = thinkingImage; // swap avatar to GIF
        } catch (Exception e) {
            response = "Unexpected error: " + e.getMessage();
        }

        DialogBox user = DialogBox.getUserDialog(input, userImage);
        DialogBox bot  = DialogBox.getManboDialog(response, botAvatar);

        // Heuristic error styling
        if (looksLikeError(response)) {
            bot.markAsError();
        }

        dialogContainer.getChildren().addAll(user, bot);
        userInput.clear();
    }



    private boolean looksLikeError(String s) {
        if (s == null) return false;
        String t = s.toLowerCase();
        return t.startsWith("error:")
                || t.contains("invalid")
                || t.contains("cannot be")
                || t.contains("provide a valid")
                || t.contains("not a valid")
                || t.contains("missing")
                || t.contains("empty");
    }
}
