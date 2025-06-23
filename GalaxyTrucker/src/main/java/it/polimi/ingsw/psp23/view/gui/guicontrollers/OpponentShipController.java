package it.polimi.ingsw.psp23.view.gui.guicontrollers;

import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.network.Client;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.util.Objects;


public class OpponentShipController {

    @FXML private Label labelOwner;
    @FXML private Button returnBtn;
    @FXML private StackPane stackShip;
    @FXML private ImageView imageGrid;
    @FXML private GridPane gridShip;


    public void setLabel(String owner) {
        labelOwner.setText("Ecco la nave di " + owner);
    }


    public void renderShip(Component[][] ship, int level) {
        gridShip.getChildren().clear();

        /*imageGrid.fitWidthProperty().bind(stackShip.widthProperty());
        imageGrid.fitHeightProperty().bind(stackShip.heightProperty());
        gridShip.prefWidthProperty().bind(stackShip.widthProperty());
        gridShip.prefHeightProperty().bind(stackShip.heightProperty());*/

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
                    imageView.setFitHeight(90);
                    imageView.setPreserveRatio(true);
                    gridShip.add(imageView, col, row);
                }
            }
        }
    }
}

