package it.polimi.ingsw.psp23.view.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // 1) Carica il file FXML (attenzione al path: risorsa sotto resources/)
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/hello-view.fxml")
        );
        Parent root = loader.load();

        // 2) Se serve, puoi ottenere il controller così:
        // HelloController controller = loader.getController();
        // controller.initData(...);

        Scene scene = new Scene(root, 400, 300);
        stage.setTitle("Galaxy Trucker");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
