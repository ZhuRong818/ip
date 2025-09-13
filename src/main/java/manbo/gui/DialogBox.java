package manbo.gui;

import java.io.IOException;
import java.util.Collections;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * A dialog box that contains a text bubble and an avatar image.
 * Used for both user and bot messages.
 */
public class DialogBox extends HBox {
    @FXML private Label dialog;
    @FXML private ImageView displayPicture;

    private DialogBox(String text, Image img) {
        try {
            FXMLLoader loader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.setText(text);
        displayPicture.setImage(img);

        // make avatar circular
        double r = 25; // radius
        Circle clip = new Circle(r, r, r);
        displayPicture.setFitWidth(2 * r);
        displayPicture.setFitHeight(2 * r);
        displayPicture.setClip(clip);
    }

    /** Flips the dialog box so that the avatar is on the left (for bot). */
    private void flip() {
        ObservableList<Node> tmp = FXCollections.observableArrayList(getChildren());
        Collections.reverse(tmp);
        getChildren().setAll(tmp);
        setAlignment(Pos.TOP_LEFT);
    }

    /** Apply consistent styling to the dialog bubble. */
    private void styleBubble(Color bg, Color textColor, boolean bold) {
        dialog.setBackground(new Background(new BackgroundFill(bg, new CornerRadii(12), Insets.EMPTY)));
        dialog.setPadding(new Insets(8));
        dialog.setTextFill(textColor);
        if (bold) dialog.setStyle("-fx-font-weight: bold;");
    }

    /** Factory method for user messages. */
    public static DialogBox getUserDialog(String text, Image img) {
        DialogBox db = new DialogBox(text, img);
        db.styleBubble(Color.web("#d0ebff"), Color.web("#003366"), false);
        db.setAlignment(Pos.TOP_RIGHT);
        return db;
    }

    /** Factory method for bot messages. */
    public static DialogBox getManboDialog(String text, Image img) {
        DialogBox db = new DialogBox(text, img);
        db.flip();
        db.styleBubble(Color.web("#eeeeee"), Color.BLACK, false);
        return db;
    }

    /** Factory method for error messages (red bubble). */
    public static DialogBox getErrorDialog(String text, Image img) {
        DialogBox db = new DialogBox(text, img);
        db.flip();
        db.styleBubble(Color.web("#ffcccc"), Color.web("#a10000"), true);
        return db;
    }

    public DialogBox markAsError() {
        // style the whole bubble
        this.setStyle("-fx-background-color: #ffe6e6; -fx-background-radius: 14; -fx-padding: 8;");
        // style just the text
        if (dialog != null) {
            dialog.setStyle("-fx-text-fill: #a10000; -fx-font-weight: bold;");
        }
        return this;
    }


}
