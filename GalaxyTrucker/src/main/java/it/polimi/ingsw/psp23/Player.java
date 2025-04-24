package it.polimi.ingsw.psp23;

import it.polimi.ingsw.psp23.exceptions.*;
import it.polimi.ingsw.psp23.model.Game.*;
import it.polimi.ingsw.psp23.model.components.*;


public class Player {
    private final String nickname;
    private int position ;
    private final Board truck;
    private int money;
    private Component currentTileInHand;
    private boolean inGame;
    private boolean isConnected;
    private Game game;

    public Player(String nickname) {
        this.nickname = nickname;
        this.position = 0;
        this.truck = new Board();
        this.money = 0;
        this.currentTileInHand = null;
        this.inGame = true;
        this.isConnected = true;

    }

    /**
     * Setta il game a cui il player sta partecipando
     * @game è il riferimento alla sessione di gioco a cui il player partecipa
     */
    public void setGame(Game game){
        this.game = game;
    }

    public boolean isInGame() {
        return inGame;
    }

    public Board getTruck() {
        return truck;
    }

    // per le perdite, passerò un intero negativo
    public void updateMoney(int moneyVariation) {
        this.money += moneyVariation;
    }

    public void setPosition(int offset) {
        position = position + offset;
    }

    public int getPosition() {
        return position;
    }

    // player abbandona il volo, ma parteciperà al calcolo del punteggio finale
    public void leaveFlight() {
        this.inGame = false;
        // il metodo sort() presente in Game provvederà a spostare il Player da players a playersNotOnFlight
    }

    public String getNickname() {
        return nickname;
    }

    public int getMoney() {
        return money;
    }

    public boolean isConnected(){
        return isConnected;
    }

    public void setDisconnected(){
        this.isConnected = false;
    }

    public void setConnected(){
        this.isConnected = true;
    }

    /** La logica effettiva rimane nel Game (dove risiedono le risorse condivise),
     * ma questi metodi agiscono come facciata per mantenere un design orientato agli oggetti:
     * è il Player che compie l'azione, e sa cosa chiedere al Game.
     */
    public Component chooseTileFromHeap() {
        // eventuale eccezione verrà propagata e gestita dal controller
        if (currentTileInHand != null)
            throw new InvalidComponentActionException("Per pescare non devi avere niente già in mano!");
        currentTileInHand = game.getTileFromHeap();
        return currentTileInHand;
    }

    public Component chooseCardUncovered(int position) {
        // eventuale eccezione verrà propagata e gestita dal controller
        if (currentTileInHand != null)
            throw new InvalidComponentActionException("Per pescare non devi avere niente già in mano!");
        currentTileInHand = game.getTileUncovered(position);
        return currentTileInHand;
    }

    public void discardComponent(){
        if (currentTileInHand == null)
            throw new InvalidComponentActionException("Non puoi rilasciare nulla perchè non hai niente in mano!");
        game.releaseTile(currentTileInHand);
        currentTileInHand = null;
    }

    public void rotateTileInHand() {
        if (currentTileInHand == null)
            throw new InvalidComponentActionException("Puoi ruotare solo se hai qualcosa in mano!");
        currentTileInHand.rotate();
    }

    // utile solo per evitare di fare player.getBoard().addComponent....
    public void addTile(int x, int y) {
        if (currentTileInHand == null)
            throw new InvalidComponentActionException("Non hai in mano nulla --> non puoi saldare nulla sulla nave!");
        getTruck().addComponent(currentTileInHand, x, y);
        currentTileInHand = null;
    }

}
