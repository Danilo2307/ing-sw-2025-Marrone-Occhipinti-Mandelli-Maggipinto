package it.polimi.ingsw.psp23.controller;

import it.polimi.ingsw.psp23.Player;
import it.polimi.ingsw.psp23.exceptions.*;
import it.polimi.ingsw.psp23.model.Events.Event;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.cards.Card;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.model.components.HousingUnit;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.model.enumeration.Side;
import it.polimi.ingsw.psp23.Player;

import java.util.ArrayList;
import java.util.Comparator;

public class Controller {
    private Game game; //inizializzo il model
    private CardHandler cardHandler;
    private Timer timer;
    private boolean isFirstBuildingPhaseEnded; // variabile che serve all'handle timeout per capire se la clessidra deve ancora essere girata
    private int currentPosition;
    private Card currentCard;

    public Controller(int gameId) {
        game = new Game(gameId);
        cardHandler = new CardHandler();
        timer = new Timer();
        isFirstBuildingPhaseEnded = false;
        currentPosition = 1;
        game.setEventListener(this::onGameEvent);
    }

    public void addPlayerToGame(String nickname) throws PlayerExistsException, GameFullException {
        if(game.getGameStatus() == GameStatus.Setup) {
            if (game.getPlayers().size() < 4)
                game.addPlayer(nickname);
            else
                throw new GameFullException("The game is full");
        }
    }

    public void startBuildingPhase() {

        for (Player player : game.getPlayers()) {
            player.getTruck().addComponent(new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true), 2, 3);
        }//questo for inizializza la cabina centrale dei player con la prima housing unit

        game.setGameStatus(GameStatus.Building);

        startTimer();
    }

    public void startTimer() {
        //la clessidra dura un minuto e mezzo
        timer.startCountdown(90, this::handleTimeout);
    }



    public void handleTimeout() {
        if (!isFirstBuildingPhaseEnded) {
            isFirstBuildingPhaseEnded = true;
            System.out.println("First building phase ended\n");
        } else {
            startCheckBoard();
        }

    }



    public void startCheckBoard() throws IllegalTruckException {

        if (game.getGameStatus() == GameStatus.Building) {
            timer.shutdown();
            game.setGameStatus(GameStatus.CheckBoards);
        }

        for (Player player : game.getPlayers()) {
            if (!player.getTruck().check()) {
                System.out.println("Player " + player.getNickname() + " has an illegal truck\n");
                throw new IllegalTruckException(player.getNickname() + " has an illegal truck");
            }
        }

    }

    public void removeComponent(String nickname,int i, int j){
        game.getPlayerFromNickname(nickname).getTruck().delete(i,j);
    }



    public void addComponent(String nickname, Component c, int x, int y) {
        game.getPlayerFromNickname(nickname).getTruck().addComponent(c, x, y);
    }

    public Component getTileFromHeap() {
        return game.getTileFromHeap();
    }

    public Component getTileUncovered(int position) {
        return game.getTileUncovered(position);
    }

    public void releaseTile(Component c) {
        game.releaseTile(c);
    }



    public void playerFinishedBuilding(String nickname) {
        switch (currentPosition) {
            case 1: {
                game.setCurrentPlayer(game.getPlayerFromNickname(nickname));
                game.getPlayerFromNickname(nickname).setPosition(8);
                break;
            }
            case 2: {
                game.setCurrentPlayer(game.getPlayerFromNickname(nickname));
                game.getPlayerFromNickname(nickname).setPosition(5);
                break;
            }
            case 3: {
                game.setCurrentPlayer(game.getPlayerFromNickname(nickname));
                game.getPlayerFromNickname(nickname).setPosition(3);
                break;
            }
            case 4: {
                game.setCurrentPlayer(game.getPlayerFromNickname(nickname));
                game.getPlayerFromNickname(nickname).setPosition(2);
                break;
            }
        }
        currentPosition++;
    }

    public void startFlight(){
        game.sortPlayersByPosition();
        game.setGameStatus(GameStatus.Playing);
    }


    public ArrayList<Card> getVisibleDeck1(String nickname){
        ArrayList<Card> deck;
        deck = game.getVisibleDeck1(game.getPlayerFromNickname(nickname));
        if(deck != null){
            return deck;
        }else{
            throw new DeckAlreadyTakenException("The deck is already taken by another player");
        }
    }

    public ArrayList<Card> getVisibleDeck2(String nickname){
        ArrayList<Card> deck;
        deck = game.getVisibleDeck2(game.getPlayerFromNickname(nickname));
        if(deck != null){
            return deck;
        }else{
            throw new DeckAlreadyTakenException("The deck is already taken by another player");
        }
    }

    public ArrayList<Card> getVisibleDeck3(String nickname){
        ArrayList<Card> deck;
        deck = game.getVisibleDeck3(game.getPlayerFromNickname(nickname));
        if(deck != null){
            return deck;
        }else{
            throw new DeckAlreadyTakenException("The deck is already taken by another player");
        }
    }

    public void releaseDeck1(String nickname){
        game.releaseVisibleDeck1(game.getPlayerFromNickname(nickname));
    }

    public void releaseDeck2(String nickname){
        game.releaseVisibleDeck2(game.getPlayerFromNickname(nickname));
    }

    public void releaseDeck3(String nickname){
        game.releaseVisibleDeck3(game.getPlayerFromNickname(nickname));
    }

    public ArrayList<Player> calculateFinalRanking(){
        game.sortPlayersByPosition();
        game.calculateFinalScores();
        game.getPlayers().sort(Comparator.comparingInt(Player::getMoney).reversed());
        return game.getPlayers();
    }

    public void gameOver(){
        game.setGameStatus(GameStatus.End);
    }

    public void nextCard(){
        currentCard = game.getNextCard();
        if(currentCard == null){
            gameOver();
        }else{
            // qui ci andrà l'handleCard method, ovvero quel metodo presente nel model per ogni carta
            // che creerà l'evento, cambierà stato e metterà nell'evento le informazioni della carta.
            // Dopo la chiamata a questo evento di inizializzazione ci sarà la chiamata dell'effettivo metodo Play
            // al quale saranno già passati gli input richiesti nella precedente chiamata grazie all'evento
            // (nota bene che nella chiamata al primo metodo, l'handleCard vuole l'istanza del model in  ingresso ovvero game
            // perchè deve poter chiamare la funzione fireEvent dichiarata nel model per compattezza);
        }
    }

    //arriva un input dalla view
    public void handleInput(Object input) {
        currentCard.play(game,input);
    }

    public void onGameEvent(Event event) { //metodo triggerato dall'evento generico di play nel model
        game.setGameStatus(event.getNewStatus());
        //qui serve tutta la gestione della chiamata alla view poichè giunti a questo punto
        //avremo l'evento pronto con le informazioni della carta e lo stato già aggiornato dal model con una ripetizione
        // del suo cambiamento all'interno dell'evento (si può anche togliere in futuro)
        // quindi da qui chiamiamo la view e dopo la view chiamerà un altro metodo per mandarci l'input da mandare a play

        // Invia i dati alla View (come JSON, socket, ecc.)
        network.sendToAllClients("game_event", event);
    }



}












