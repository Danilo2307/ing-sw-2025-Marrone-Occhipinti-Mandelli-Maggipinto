package it.polimi.ingsw.psp23.view.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

// è un helper per cambiare le scene, ad esempio, quando viene cambiato stato da BuildingPhase a Flight
// ha un riferimento allo stage principale, esponde un metodo switchScene(String fxmlPath, Object controller)
// chiama FxmlViewLoader (fxmlPath, controller)
public class StageManager {
    private Stage stage;

    public StageManager(Stage stage) {
        this.stage = stage;
    }

    public void toBuildingPhase() {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/build-view.fxml")
        );
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root, 400, 300);
            stage.setScene(scene);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

}
