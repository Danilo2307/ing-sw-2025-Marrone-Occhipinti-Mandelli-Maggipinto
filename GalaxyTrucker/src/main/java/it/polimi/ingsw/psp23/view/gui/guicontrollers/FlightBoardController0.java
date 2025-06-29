package it.polimi.ingsw.psp23.view.gui.guicontrollers;
import it.polimi.ingsw.psp23.network.Client;
import it.polimi.ingsw.psp23.view.gui.GuiApplication;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.Map;
import java.util.Objects;
import it.polimi.ingsw.psp23.model.enumeration.Color;


/**
 * The FlightBoardController0 class is responsible for controlling the flight board user interface in the application.
 * It provides functionalities to manage and update flight board elements and interactions based on the game state.
 */
public class FlightBoardController0 {
    @FXML private Button backToShip;
    @FXML private ImageView position0;
    @FXML private ImageView position1;
    @FXML private ImageView position2;
    @FXML private ImageView position3;
    @FXML private ImageView position4;
    @FXML private ImageView position5;
    @FXML private ImageView position6;
    @FXML private ImageView position7;
    @FXML private ImageView position8;
    @FXML private ImageView position9;
    @FXML private ImageView position10;
    @FXML private ImageView position11;
    @FXML private ImageView position12;
    @FXML private ImageView position13;
    @FXML private ImageView position14;
    @FXML private ImageView position15;
    @FXML private ImageView position16;
    @FXML private ImageView position17;
    private Client client;
    private ImageView[] positions;

    public void initialize() {
        positions = new ImageView[]{
                position0, position1, position2, position3, position4, position5,
                position6, position7, position8, position9, position10, position11,
                position12, position13, position14, position15, position16, position17
        };
    }

    public void setClient(Client client) {
        this.client = client;
    }


    /**
     * Handles the action event triggered by clicking the "Back to Ship" button in the GUI.
     * This method directs the user back to the ship screen of the application by invoking
     * the appropriate method in the GuiApplication instance.
     */
    @FXML
    public void onBackToShipClicked(){
        GuiApplication.getInstance().backToShip();
    }


    /**
     * Updates the graphical positions on the board with images associated with specific colors and their corresponding positions.
     * This method clears any existing images in the positions array and maps new images based on the given color-position mapping.
     *
     * @param colors a map containing color-to-position associations, where the key is the Color type of the player and
     *               the value is the position index at which the color's image should be displayed
     */
    public void setColors(Map<Color,Integer> colors){

        for(int i =0; i<positions.length; i++){
            int finalI = i;
            Platform.runLater(()->{
                positions[finalI].setImage(null);
            });

        }

        for(Map.Entry<Color,Integer> entry : colors.entrySet()){
            Platform.runLater(()->{
                Color color = entry.getKey();
                String imagePath = "/it/polimi/ingsw/psp23/images/cards/" + 101 + ".jpg";
                javafx.scene.image.Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
                int newPosition = entry.getValue();
                if (newPosition < 0) {
                    newPosition = 18 + newPosition;
                }
                switch (color){
                    case Red ->  positions[newPosition % 18].setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/psp23/images/utility/red.jpg"))));
                    case Blue ->  positions[newPosition % 18].setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/psp23/images/utility/blue.jpg"))));
                    case Yellow -> positions[newPosition % 18].setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/psp23/images/utility/yellow.jpg"))));
                    case Green -> positions[newPosition % 18].setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/psp23/images/utility/green.jpg"))));
                }
            });

        }
    }

}
