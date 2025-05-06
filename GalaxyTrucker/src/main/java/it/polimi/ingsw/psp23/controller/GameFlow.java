package it.polimi.ingsw.psp23.controller;

import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.cards.Card;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

import java.io.Serializable;

public class GameFlow implements Serializable {

    private GameStatus currentState;
    private int turn;
    private Card currentCard;
    private Player currentPlayer;

    public GameFlow() {
        currentState = GameStatus.Building;
        turn = 0;
        currentCard = null;
        currentPlayer = null;
    }

//    public void nextCard(){
//        if(!Game.getInstance().getGameStatus().equals(GameStatus.Playing)){
//            throw new CardException("Cannot pick next card at this time");
//        }
//        Game.getInstance().sortPlayersByPosition();
//        currentCard = Game.getInstance().getNextCard();
//        if(currentCard == null || Game.getInstance().getPlayers().size() <= 1){
//            Game.getInstance().setGameStatus(GameStatus.End);
//            Game.getInstance().calculateFinalScores();
//        }else{
//            turn = 0;
//            currentPlayer = leaderPlayer();
//            Game.getInstance().setCurrentPlayer(currentPlayer);
//            Visitor visitor = new InitPlayVisitor();
//            currentCard.call(visitor);
//
//        }
//    }

//    public Player nextTurn(){
//        turn++;
//        if(turn >= Game.getInstance().getPlayers().size()){
//            Game.getInstance().sortPlayersByPosition();
//            nextCard();
//            currentPlayer = leaderPlayer();
//        }
//        else{
//            currentPlayer = Game.getInstance().getNextPlayer();
//        }
//        return currentPlayer;
//    }

    public Player currentPlayer(){
        return Game.getInstance().getCurrentPlayer();
    }

    public Player leaderPlayer(){
        return Game.getInstance().getPlayers().getFirst();
    }


    public GameStatus getCurrentState() {
        return currentState;
    }

    public void setCurrentState(GameStatus currentState) {
        this.currentState = currentState;
    }
}

