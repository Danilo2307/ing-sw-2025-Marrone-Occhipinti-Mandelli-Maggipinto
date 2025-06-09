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


    @FXML
    public void onBackToShipClicked(){
        GuiApplication.getInstance().backToShip();
    }


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
                System.out.println("Qui ci arrivo con almeno un colore " + entry.getValue() + " " + entry.getKey());
                String imagePath = "/it/polimi/ingsw/psp23/images/cards/" + 101 + ".jpg";
                javafx.scene.image.Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
                switch (color){
                    case Red ->  positions[entry.getValue() % 18].setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/psp23/images/utility/red.jpg"))));
                    case Blue ->  positions[entry.getValue() % 18].setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/psp23/images/utility/blue.jpg"))));
                    case Yellow -> positions[entry.getValue() % 18].setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/psp23/images/utility/yellow.jpg"))));
                    case Green -> positions[entry.getValue() % 18].setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/psp23/images/utility/green.jpg"))));
                }
            });

        }
    }


}
