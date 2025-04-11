package it.polimi.ingsw.psp23.model.controller;

import it.polimi.ingsw.psp23.Player;
import it.polimi.ingsw.psp23.exceptions.GameFullException;
import it.polimi.ingsw.psp23.exceptions.PlayerExistsException;
import it.polimi.ingsw.psp23.exceptions.TimeoutException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.components.HousingUnit;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.model.enumeration.Side;

public class Controller{
    private Game game; //inizializzo il model
    private CardHandler cardHandler;
    private Timer timer;
    private boolean firstBuildingPhaseEnded; // variabile che serve all'handle timeout per capire se il timer deve ancora essere girato

    public Controller(int gameId) {
        game = new Game(gameId);
        cardHandler = new CardHandler();
        timer = new Timer();
        firstBuildingPhaseEnded = false;
    }

    public void addPlayerToGame(String nickname) throws PlayerExistsException, GameFullException {
            if (game.getPlayers().size() < 4)
                game.addPlayer(nickname);
            else
                throw new GameFullException("The game is full");
    }

    public void startBuildingPhase(){

        for (Player player : game.getPlayers()) {
            player.getTruck().addComponent(new HousingUnit(Side.UNIVERSAL_CONNECTOR,Side.UNIVERSAL_CONNECTOR,Side.UNIVERSAL_CONNECTOR,Side.UNIVERSAL_CONNECTOR,true),2,3);
        }//questo for inizializza la cabina centrale dei player con la prima housing unit

        game.setGameStatus(GameStatus.Building);

        startTimer();
    }

    public void startTimer(){
        //la clessidra dura un minuto e mezzo
        timer.startCountdown(90,this:: handleTimeout);
    }



    public void handleTimeout() throws TimeoutException{
        if(!firstBuildingPhaseEnded) {
            firstBuildingPhaseEnded = true;
            throw new TimeoutException("The first building phase ended");
        }else{
            timer.shutdown();
            game.setGameStatus(GameStatus.CheckBoards);
        }
    }


}
