package manbo.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class MainWindow extends AnchorPane {
    @FXML private ScrollPane scrollPane;
    @FXML private VBox dialogContainer;
    @FXML private TextField userInput;
    @FXML private Button sendButton;

    private ManboAdapter backend;

    private final Image userImage = new Image(this.getClass().getResourceAsStream("/images/ManboBot.png"));
    private final Image manboImage = new Image(this.getClass().getResourceAsStream("/images/ManboUser1.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Inject backend (adapter). */
    public void setBackend(ManboAdapter b) {
        this.backend = b;
        // Optional greeting
        dialogContainer.getChildren().add(DialogBox.getManboDialog("曼波，曼波～ I'm Manbo.\nType something!", manboImage));
    }

    /** Handles Enter key and Send button. */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = backend.getResponse(input);

        // In MainWindow.handleUserInput()
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getManboDialog(response, manboImage)
        );
        userInput.clear();
    }

}
