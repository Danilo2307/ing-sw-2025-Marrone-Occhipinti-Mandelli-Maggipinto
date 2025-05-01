package it.polimi.ingsw.psp23.controller;

import it.polimi.ingsw.psp23.Player;
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

    public Card nextCard(){
        Game.getInstance().sortPlayersByPosition();
        if(Game.getInstance().getNextCard() != null || !Game.getInstance().getPlayers().isEmpty()){
            currentCard = Game.getInstance().getNextCard();
            turn = 0;
            return currentCard;
        }
        else {
            Game.getInstance().setGameStatus(GameStatus.End);
            Game.getInstance().calculateFinalScores();
        }
        return null;
    }

    public Player nextTurn(){
        turn++;
        if(turn > Game.getInstance().getPlayers().size()){
            Game.getInstance().sortPlayersByPosition();
            nextCard();
            currentPlayer = leaderPlayer();
        }
        else{
            currentPlayer = Game.getInstance().getNextPlayer();
        }
        return currentPlayer;
    }

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

