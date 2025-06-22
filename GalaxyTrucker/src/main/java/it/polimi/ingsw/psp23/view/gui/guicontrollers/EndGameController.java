package it.polimi.ingsw.psp23.view.gui.guicontrollers;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.AbstractMap;
import java.util.List;

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

    public void printInfo(List<AbstractMap.SimpleEntry<String,Integer>> ranking, Stage stage){
        this.stage = stage;
        switch (ranking.size()){
            case 1: {
                text1.setVisible(true);
                player1.setText(ranking.get(0).getKey());
                cosmicCredits1.setText(ranking.get(0).getValue().toString());
            }
            case 2: {
                text1.setVisible(true);
                text2.setVisible(true);
                player1.setText(ranking.get(0).getKey());
                cosmicCredits1.setText(ranking.get(0).getValue().toString());
                player2.setText(ranking.get(1).getKey());
                cosmicCredits2.setText(ranking.get(1).getValue().toString());
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
            }

        }
    }

    @FXML void closeStage() {
        stage.close();
    }
}
