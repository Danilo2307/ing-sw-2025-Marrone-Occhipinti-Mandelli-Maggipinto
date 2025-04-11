package it.polimi.ingsw.psp23.controller;

import it.polimi.ingsw.psp23.Player;
import it.polimi.ingsw.psp23.exceptions.*;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.model.components.HousingUnit;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.model.enumeration.Side;

public class Controller{
    private Game game; //inizializzo il model
    private CardHandler cardHandler;
    private Timer timer;
    private boolean isFirstBuildingPhaseEnded; // variabile che serve all'handle timeout per capire se il timer deve ancora essere girato
    private int currentPosition;

    public Controller(int gameId) {
        game = new Game(gameId);
        cardHandler = new CardHandler();
        timer = new Timer();
        isFirstBuildingPhaseEnded = false;
        currentPosition = 1;
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



    public void handleTimeout(){
        if(!isFirstBuildingPhaseEnded) {
            isFirstBuildingPhaseEnded = true;
            System.out.println("First building phase ended\n");
        }else{
            startCheckBoard();
        }


    }

    public void startCheckBoard() throws IllegalTruckException{

        if(game.getGameStatus() == GameStatus.Building) {
            timer.shutdown();
            game.setGameStatus(GameStatus.CheckBoards);
        }

        for (Player player : game.getPlayers()) {
            if(!player.getTruck().check()){
                System.out.println("Player " + player.getNickname() + " has an illegal truck\n");
                throw new IllegalTruckException(player.getNickname()+" has an illegal truck");
            }
        }

    }

    public void addComponent(String nickname, Component c, int x, int y) {
        game.getPlayerFromNickname(nickname).getTruck().addComponent(c,x,y);
    }

    public void playerFinishedBuilding(String nickname){
        switch(currentPosition){
            case 1:
                game.setCurrentPlayer(game.getPlayerFromNickname(nickname));
                game.getPlayerFromNickname(nickname).setPosition(8);












}
