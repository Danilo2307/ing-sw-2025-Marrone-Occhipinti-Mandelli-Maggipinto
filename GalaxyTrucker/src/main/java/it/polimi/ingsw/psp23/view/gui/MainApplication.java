package it.polimi.ingsw.psp23.view.gui;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class MainApplication extends Application {
        public void start(Stage stage) {
            Button btn = new Button("INIZIA IL GIOCO");
            btn.setOnAction(e -> System.out.println("Hai avviato il gioco"));

            StackPane root = new StackPane(btn);
            Scene scene = new Scene(root, 400, 300);

            stage.setTitle("Galaxy Trucker");
            stage.setScene(scene);
            stage.show();
        }

        public static void main(String[] args) {
            launch();
        }

}
