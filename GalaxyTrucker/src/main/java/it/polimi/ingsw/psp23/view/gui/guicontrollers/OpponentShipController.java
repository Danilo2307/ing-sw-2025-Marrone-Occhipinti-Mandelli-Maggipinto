package it.polimi.ingsw.psp23.view.gui.guicontrollers;

import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.network.Client;
import it.polimi.ingsw.psp23.view.gui.GuiApplication;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.util.Objects;


/**
 * Controller class for managing the opponent's ship view in a graphical user interface.
 */
public class OpponentShipController {

    @FXML private Label labelOwner;
    @FXML private Button returnBtn;
    @FXML private StackPane stackShip;
    @FXML private ImageView imageGrid;
    @FXML private GridPane gridShip;


    public void setLabel(String owner) {
        labelOwner.setText("Ecco la nave di " + owner);
    }

    /**
     * Renders the ship layout on the graphical grid based on the provided ship components and level.
     *
     * @param ship a 2D array of Component objects representing the ship layout, where each element
     *             corresponds to a part of the ship or null if no component is present at that position
     * @param level an integer indicating the level of detail or display mode for rendering the ship;
     *              0 for default level, other values for alternate views
     */
    public void renderShip(Component[][] ship, int level) {
        gridShip.getChildren().clear();

        if (level == 0) {
            imageGrid.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/psp23/images/cardboard/cardboard-1.jpg"))));
        }
        else {
            imageGrid.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/psp23/images/cardboard/cardboard-1b.jpg"))));
        }

        for (int row = 0; row < ship.length; row++) {
            for (int col = 0; col < ship[row].length; col++) {
                Component component = ship[row][col];

                if (component != null) {
                    String imagePath = "/it/polimi/ingsw/psp23/images/tiles/" + component.getId() + ".jpg";
                    ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath))));
                    imageView.setFitWidth(120);
                    imageView.setFitHeight(95);
                    imageView.setPreserveRatio(true);
                    imageView.setRotate(component.getRotate());
                    gridShip.add(imageView, col, row);
                }
            }
        }
    }

    /**
     * Navigates the application back to the ship view.
     */
    @FXML
    public void onReturnClicked() {
        GuiApplication.getInstance().backToShip();
    }
}

