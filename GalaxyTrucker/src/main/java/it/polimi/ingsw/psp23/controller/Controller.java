package it.polimi.ingsw.psp23.controller;

import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.exceptions.*;
import it.polimi.ingsw.psp23.model.Events.Event;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.cards.*;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.model.components.HousingUnit;
import it.polimi.ingsw.psp23.model.enumeration.Color;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.model.enumeration.Side;
import it.polimi.ingsw.psp23.network.messages.BroadcastMessage;
import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.network.socket.Server;
import it.polimi.ingsw.psp23.protocol.response.StateChanged;
import it.polimi.ingsw.psp23.protocol.response.StringResponse;

import java.util.ArrayList;
import java.util.Comparator;

public class Controller {
    // private CardHandler cardHandler;
    private static Controller instance = null;
    private Timer timer;
    private boolean isFirstBuildingPhaseEnded; // variabile che serve all'handle timeout per capire se la clessidra deve ancora essere girata
    private int currentPosition;
    private Card currentCard;
    GameFlow gameFlow = new GameFlow();


    public Controller() {
        // cardHandler = new CardHandler();
        timer = new Timer();
        isFirstBuildingPhaseEnded = false;
        currentPosition = 1;
        Game.getInstance().setEventListener(this::onGameEvent);
    }

    public static synchronized Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    public void addPlayerToGame(String nickname) throws PlayerExistsException, GameFullException {
        Game game = Game.getInstance();

        if(game.getGameStatus() == GameStatus.Setup) {
            if (game.getPlayers().size() <= game.getNumRequestedPlayers() || game.getNumRequestedPlayers() == -1)
                game.addPlayer(nickname);
        }
    }

    public void startBuildingPhase() {

        for (Player player : Game.getInstance().getPlayers()) {
            player.getTruck().addComponent(new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true), 2, 3);
        }//questo for inizializza la cabina centrale dei player con la prima housing unit

        Game.getInstance().setGameStatus(GameStatus.Building);
        Server.getInstance().notifyAllObservers(new BroadcastMessage(new StateChanged(GameStatus.Building)));

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
        } else { //TODO: in questo else controllo se i player hanno finito tutti, in caso contrario assegno posizioni in ordine di entrata e in case 4 dello switch chiamo startcheckboards
            try {
                startCheckBoard();
            }catch (IllegalStateException e) {
                handleTimeout();
            }

        }

    }



    public void startCheckBoard() throws IllegalTruckException {

        if (Game.getInstance().getGameStatus() == GameStatus.Building) {
            timer.shutdown();
            Game.getInstance().setGameStatus(GameStatus.CheckBoards);
        }

        for (Player player : Game.getInstance().getPlayers()) {
            if (!player.getTruck().check()) {
                System.out.println("Player " + player.getNickname() + " has an illegal truck\n");
                throw new IllegalTruckException(player.getNickname() + " has an illegal truck");
            }
        }

        startFlight();

    }

    public void removeComponent(String nickname,int i, int j){
        Game.getInstance().getPlayerFromNickname(nickname).getTruck().delete(i,j);
    }



    public void addComponent(String nickname, Component c, int x, int y) {
        Game.getInstance().getPlayerFromNickname(nickname).getTruck().addComponent(c, x, y);
    }

    public Component getTileFromHeap() {
        return Game.getInstance().getTileFromHeap();
    }

    /*public Component getTileUncovered(int position) {
        return Game.getInstance().getTileUncovered(position);
    }*/

    public void releaseTile(Component c) {
        Game.getInstance().releaseTile(c);
    }

    public void rotate(Component c) {
        c.rotate();
    }

//TODO: va gestita la possibilità del player di attaccare ancora pezzi una volta che dichiara di aver terminato ovvero dovremmo mettere una lista temporanea per dire chi ha finito

    public void playerFinishedBuilding(String nickname) {
        switch (currentPosition) {
            case 1: {
                Game.getInstance().setCurrentPlayer(Game.getInstance().getPlayerFromNickname(nickname));
                Game.getInstance().getPlayerFromNickname(nickname).setPosition(8);
                break;
            }
            case 2: {
                Game.getInstance().setCurrentPlayer(Game.getInstance().getPlayerFromNickname(nickname));
                Game.getInstance().getPlayerFromNickname(nickname).setPosition(5);
                break;
            }
            case 3: {
                Game.getInstance().setCurrentPlayer(Game.getInstance().getPlayerFromNickname(nickname));
                Game.getInstance().getPlayerFromNickname(nickname).setPosition(3);
                break;
            }
            case 4: {
                Game.getInstance().setCurrentPlayer(Game.getInstance().getPlayerFromNickname(nickname));
                Game.getInstance().getPlayerFromNickname(nickname).setPosition(2);
                break;
            }
        }
        currentPosition++;
        if(currentPosition == Game.getInstance().getPlayers().size()) {
            startFlight();
        }
    }

    public void startFlight(){
        Game game = Game.getInstance();
        game.setGameStatus(GameStatus.Playing);
        game.nextCard();
    }


    public ArrayList<Card> getVisibleDeck1(String nickname){
        ArrayList<Card> deck;
        deck = Game.getInstance().getVisibleDeck1(Game.getInstance().getPlayerFromNickname(nickname));
        if(deck != null){
            return deck;
        }else{
            throw new DeckAlreadyTakenException("The deck is already taken by another player");
        }
    }

    public ArrayList<Card> getVisibleDeck2(String nickname){
        ArrayList<Card> deck;
        deck = Game.getInstance().getVisibleDeck2(Game.getInstance().getPlayerFromNickname(nickname));
        if(deck != null){
            return deck;
        }else{
            throw new DeckAlreadyTakenException("The deck is already taken by another player");
        }
    }

    public ArrayList<Card> getVisibleDeck3(String nickname){
        ArrayList<Card> deck;
        deck = Game.getInstance().getVisibleDeck3(Game.getInstance().getPlayerFromNickname(nickname));
        if(deck != null){
            return deck;
        }else{
            throw new DeckAlreadyTakenException("The deck is already taken by another player");
        }
    }

    public void releaseDeck1(String nickname){
        Game.getInstance().releaseVisibleDeck1(Game.getInstance().getPlayerFromNickname(nickname));
    }

    public void releaseDeck2(String nickname){
        Game.getInstance().releaseVisibleDeck2(Game.getInstance().getPlayerFromNickname(nickname));
    }

    public void releaseDeck3(String nickname){
        Game.getInstance().releaseVisibleDeck3(Game.getInstance().getPlayerFromNickname(nickname));
    }

    public ArrayList<Player> calculateFinalRanking(){
        Game.getInstance().sortPlayersByPosition();
        Game.getInstance().calculateFinalScores();
        Game.getInstance().getPlayers().sort(Comparator.comparingInt(Player::getMoney).reversed());
        return Game.getInstance().getPlayers();
    }



    //arriva un input dalla view
    public void handleInput(Object input) {
        //currentCard.play(Game.getInstance(),input);
    }

    public void onGameEvent(Event event) { //metodo triggerato dall'evento generico di play nel model
        Game.getInstance().setGameStatus(event.getNewStatus());
        //qui serve tutta la gestione della chiamata alla view poichè giunti a questo punto
        //avremo l'evento pronto con le informazioni della carta e lo stato già aggiornato dal model con una ripetizione
        // del suo cambiamento all'interno dell'evento (si può anche togliere in futuro)
        // quindi da qui chiamiamo la view e dopo la view chiamerà un altro metodo per mandarci l'input da mandare a play

        // Invia i dati alla View (come JSON, socket, ecc.)

        //network.sendToAllClients("game_event", event);

        // TODO: capire quale evento effettivo bisogna inviare
        Message message = new BroadcastMessage(new StringResponse("evento generico in play"));
        Server.getInstance().notifyAllObservers(message);
    }


    public void buyShip(String username) {
        BuyShipVisitor buyShip = new BuyShipVisitor();
        currentCard.call(buyShip, username);

    }

    public void reduceCrew(String username, int i, int j, int num) {
        ReduceCrewVisitorNum reduceCrew = new ReduceCrewVisitorNum();
        currentCard.call(reduceCrew, username, i, j, num);
    }

    public void pass(String username) {
        PassVisitor pass = new PassVisitor();
        currentCard.call(pass, username);
    }

    public String help(){
        HelpVisitor help = new HelpVisitor();
        return currentCard.call(help);
    }

    public void activeCannon(String username, int i , int j, int iBattery, int jBattery){
        ActiveCannonVisitor activeCannon = new ActiveCannonVisitor();
        Game.getInstance().getPlayerFromNickname(username).getTruck().reduceBatteries(iBattery,jBattery,1);
        currentCard.call(activeCannon, username, i, j);
    }

    public void activeEngine(String username, int i, int j, int iBattery, int jBattery){
        ActiveEngineVisitor activeEngine = new ActiveEngineVisitor();
        Game.getInstance().getPlayerFromNickname(username).getTruck().reduceBatteries(iBattery,jBattery,1);
        currentCard.call(activeEngine, username, i, j);
    }

    public void activeShield(String username, int i, int j, int iBattery, int jBattery){
        ActiveShieldVisitor activeShield = new ActiveShieldVisitor();
        Game.getInstance().getPlayerFromNickname(username).getTruck().reduceBatteries(iBattery,jBattery,1);
        currentCard.call(activeShield, username, i, j);
    }

    public void dockStation(String username){
        DockStationVisitor dockStation = new DockStationVisitor();
        currentCard.call(dockStation, username);
    }

    public void loadGoods(String username, int i, int j){
        LoadGoodsVisitor loadGoods = new LoadGoodsVisitor();
        currentCard.call(loadGoods, username, i, j);
    }

    public void removePreciousItems(String username, int i, int j, int num){
        RemovePreciousItemVisitor removePreciousItem = new RemovePreciousItemVisitor();
        currentCard.call(removePreciousItem, username, i, j, num);
    }

    public void ready(String username){
        ReadyVisitor ready = new ReadyVisitor();
        currentCard.call(ready, username);
    }

    public void landOnPlanet(String username, int i){
        LandOnPlanetVisitor landOnPlanet = new LandOnPlanetVisitor();
        currentCard.call(landOnPlanet, username, i);
    }

    public void removeItem(String username, int i, int j, Color color){
        if(!Game.getInstance().getGameStatus().equals(GameStatus.Playing))
            throw new RuntimeException("Not a possible instruction in this game state");
        Game.getInstance().getPlayerFromNickname(username).getTruck().removeGood(i, j, color);
    }







}












