package it.polimi.ingsw.psp23.view.gui.guicontrollers;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.AbstractMap;
import java.util.List;

/**
 * The EndGameController class handles the display and functionality
 * of the end game screen. It manages the presentation of player rankings
 */
public class EndGameController {
    @FXML private Label text1;
    @FXML private Label text2;
    @FXML private Label text3;
    @FXML private Label text4;
    @FXML private Label cosmicCredits1;
    @FXML private Label cosmicCredits2;
    @FXML private Label cosmicCredits3;
    @FXML private Label cosmicCredits4;
    @FXML private Label player1;
    @FXML private Label player2;
    @FXML private Label player3;
    @FXML private Label player4;
    Stage stage;

    /**
     * Displays the ranking information of players and their respective cosmic credits
     * on the end game screen. Updates the visibility of elements based on the number
     * of players passed in the ranking list.
     *
     * @param ranking a list of player rankings where each entry consists of a
     *                player's name as the key and their cosmic credits as the value
     * @param stage   the {@code Stage} object representing the current window to associate with this view
     */
    public void printInfo(List<AbstractMap.SimpleEntry<String,Integer>> ranking, Stage stage){
        this.stage = stage;
        switch (ranking.size()){
            case 1: {
                text1.setVisible(true);
                player1.setText(ranking.get(0).getKey());
                cosmicCredits1.setText(ranking.get(0).getValue().toString());
                break;
            }
            case 2: {
                text1.setVisible(true);
                text2.setVisible(true);
                player1.setText(ranking.get(0).getKey());
                cosmicCredits1.setText(ranking.get(0).getValue().toString());
                player2.setText(ranking.get(1).getKey());
                cosmicCredits2.setText(ranking.get(1).getValue().toString());
                break;
            }
            case 3: {
                text1.setVisible(true);
                text2.setVisible(true);
                text3.setVisible(true);
                player1.setText(ranking.get(0).getKey());
                cosmicCredits1.setText(ranking.get(0).getValue().toString());
                player2.setText(ranking.get(1).getKey());
                cosmicCredits2.setText(ranking.get(1).getValue().toString());
                player3.setText(ranking.get(2).getKey());
                cosmicCredits3.setText(ranking.get(2).getValue().toString());
                break;
            }
            case 4: {
                text1.setVisible(true);
                text2.setVisible(true);
                text3.setVisible(true);
                text4.setVisible(true);
                player1.setText(ranking.get(0).getKey());
                cosmicCredits1.setText(ranking.get(0).getValue().toString());
                player2.setText(ranking.get(1).getKey());
                cosmicCredits2.setText(ranking.get(1).getValue().toString());
                player3.setText(ranking.get(2).getKey());
                cosmicCredits3.setText(ranking.get(2).getValue().toString());
                player4.setText(ranking.get(3).getKey());
                cosmicCredits4.setText(ranking.get(3).getValue().toString());
                break;
            }

        }
    }

    @FXML void closeStage() {
        stage.close();
    }
}
