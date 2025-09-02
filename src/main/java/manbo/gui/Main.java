package manbo.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/** A GUI for Manbo using FXML. */
public class Main extends Application {

    private final ManboAdapter manbo = new ManboAdapter(); // see below

    @Override
    public void start(Stage stage) {
        System.out.println(Main.class.getResource("/view/MainWindow.fxml"));
        System.out.println("FXML: " + Main.class.getResource("/view/MainWindow.fxml"));
        System.out.println("User img: " + getClass().getResource("/images/ManboBot.png"));
        System.out.println("Duke img: " + getClass().getResource("/images/ManboUser1.png"));

        try {


            FXMLLoader fxml = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxml.load();
            Scene scene = new Scene(ap);
            stage.setTitle("Manbo");
            stage.setResizable(false);
            stage.setScene(scene);
            fxml.<MainWindow>getController().setBackend(manbo);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
