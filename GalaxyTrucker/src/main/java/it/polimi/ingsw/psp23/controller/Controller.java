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
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.network.socket.Server;
import it.polimi.ingsw.psp23.protocol.response.IllegalTruck;
import it.polimi.ingsw.psp23.protocol.response.StateChanged;
import it.polimi.ingsw.psp23.protocol.response.StringResponse;
import it.polimi.ingsw.psp23.protocol.response.UpdateFromCard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Controller {
    // private CardHandler cardHandler;
    private static Controller instance = null;
    private Timer timer;
    private boolean isFirstBuildingPhaseEnded; // variabile che serve all'handle timeout per capire se la clessidra deve ancora essere girata
    private int currentPosition;
    GameFlow gameFlow = new GameFlow();
    List<Player> crewPositioned = new ArrayList<>();


    public Controller() {
        // cardHandler = new CardHandler();
        timer = new Timer();
        isFirstBuildingPhaseEnded = false;
        currentPosition = 0;
        Game.getInstance().setEventListener(this::onGameEvent);
        Game.getInstance().setEventListener2(this::onGameEvent);
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
        Game game = Game.getInstance();
        for (Player player : game.getPlayers()) {
            player.getTruck().addComponent(new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true), 2, 3);
        }//questo for inizializza la cabina centrale dei player con la prima housing unit

        game.setGameStatus(GameStatus.Building);
        Server.getInstance().notifyAllObservers(new BroadcastMessage(new StateChanged(GameStatus.Building)));

        if (game.getLevel() != 0) {
            startTimer();
        }
    }

    public void startTimer() {
        if (Game.getInstance().getLevel() == 0) {
            throw new LevelException("Non esiste la clessidra nel volo di prova!");
        }

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
            Game game = Game.getInstance();
            game.checkReservedTiles();
            game.setGameStatus(GameStatus.CheckBoards);
            Server.getInstance().notifyAllObservers(new BroadcastMessage(new StateChanged(GameStatus.CheckBoards)));
        }

        for (Player player : Game.getInstance().getPlayers()) {
            if (!player.getTruck().check()) {
                Server.getInstance().sendMessage(player.getNickname(), new DirectMessage((new IllegalTruck())));
                return;
            }
        }

        for (Player player : Game.getInstance().getPlayers()) {
            player.getTruck().updateAllowedAliens();
        }


        startSetCrew();

    }

    public void startSetCrew() {
        if (Game.getInstance().getGameStatus() == GameStatus.CheckBoards) {
            Game.getInstance().setGameStatus(GameStatus.SetCrew);
            Server.getInstance().notifyAllObservers(new BroadcastMessage(new StateChanged(GameStatus.SetCrew)));
        }else{
            throw new RuntimeException("Not in the correct state to set crew");
        }
    }

    public void crewPositioned(String username) {
        if(!crewPositioned.contains(username)) {
            crewPositioned.add(Game.getInstance().getPlayerFromNickname(username));
        }else{
            throw new InvalidActionException("You have already declared crew ");
        }
        if(crewPositioned.size() == Game.getInstance().getPlayers().size()){
            crewPositioned.clear();
            startFlight();
        }
    }

    public void removeComponent(String username,int i, int j){
        Game.getInstance().getPlayerFromNickname(username).getTruck().delete(i,j);
    }



    public void addComponent(String username, Component c, int x, int y) {
        Game.getInstance().getPlayerFromNickname(username).getTruck().addComponent(c, x, y);
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

    public void playerFinishedBuilding(String username) {
        Game game = Game.getInstance();
        // game.setCurrentPlayer(game.getPlayerFromNickname(username));
        game.getPlayerFromNickname(username).setPosition(game.getFirstPositions()[currentPosition]);
        currentPosition++;
        if(currentPosition == game.getPlayers().size()) {
            startCheckBoard();
        }
    }

    public void startFlight(){
        Game game = Game.getInstance();
        game.setGameStatus(GameStatus.Playing);
        Server.getInstance().notifyAllObservers(new BroadcastMessage(new StateChanged(GameStatus.Playing)));
        game.nextCard();
    }


    public ArrayList<Card> getVisibleDeck1(String username){
        ArrayList<Card> deck;
        deck = Game.getInstance().getVisibleDeck1(Game.getInstance().getPlayerFromNickname(username));
        if(deck != null){
            return deck;
        }else{
            throw new DeckAlreadyTakenException("The deck is already taken by another player");
        }
    }

    public ArrayList<Card> getVisibleDeck2(String username){
        ArrayList<Card> deck;
        deck = Game.getInstance().getVisibleDeck2(Game.getInstance().getPlayerFromNickname(username));
        if(deck != null){
            return deck;
        }else{
            throw new DeckAlreadyTakenException("The deck is already taken by another player");
        }
    }

    public ArrayList<Card> getVisibleDeck3(String username){
        ArrayList<Card> deck;
        deck = Game.getInstance().getVisibleDeck3(Game.getInstance().getPlayerFromNickname(username));
        if(deck != null){
            return deck;
        }else{
            throw new DeckAlreadyTakenException("The deck is already taken by another player");
        }
    }

    public void releaseDeck1(String username){
        Game.getInstance().releaseVisibleDeck1(Game.getInstance().getPlayerFromNickname(username));
    }

    public void releaseDeck2(String username){
        Game.getInstance().releaseVisibleDeck2(Game.getInstance().getPlayerFromNickname(username));
    }

    public void releaseDeck3(String username){
        Game.getInstance().releaseVisibleDeck3(Game.getInstance().getPlayerFromNickname(username));
    }

    public ArrayList<Player> calculateFinalRanking(){
        Game.getInstance().sortPlayersByPosition();
        Game.getInstance().calculateFinalScores();
        Game.getInstance().getPlayers().sort(Comparator.comparingInt(Player::getMoney).reversed());
        return Game.getInstance().getPlayers();
    }



    /*//arriva un input dalla view
    public void handleInput(Object input) {
        //currentCard.play(Game.getInstance(),input);
    }*/


    // Questo onGameEvent inoltra gli eventi rivolti a tutti i client
    public void onGameEvent(Event event) { //metodo triggerato dall'evento generico di play nel model
        Game.getInstance().setGameStatus(event.getNewStatus());
        //qui serve tutta la gestione della chiamata alla view poichè giunti a questo punto
        //avremo l'evento pronto con le informazioni della carta e lo stato già aggiornato dal model con una ripetizione
        // del suo cambiamento all'interno dell'evento (si può anche togliere in futuro)
        // quindi da qui chiamiamo la view e dopo la view chiamerà un altro metodo per mandarci l'input da mandare a play

        // Invia i dati alla View (come JSON, socket, ecc.)

        //network.sendToAllClients("game_event", event);

        // TODO: capire quale evento effettivo bisogna inviare
        Message message = new BroadcastMessage(new UpdateFromCard(event.describe()));
        Server.getInstance().notifyAllObservers(message);

    }

    // Questo onGameEvent inoltra gli eventi rivolti ad un solo client
    public void onGameEvent(Event event, String playerUsername) {
        Game.getInstance().setGameStatus(event.getNewStatus());

        Message message = new DirectMessage(new UpdateFromCard(event.describe()));
        Server.getInstance().sendMessage(playerUsername, message);

    }


    public void buyShip(String username) {
        BuyShipVisitor buyShip = new BuyShipVisitor();
        Game.getInstance().getCurrentCard().call(buyShip, username);

    }

    public void reduceCrew(String username, int i, int j, int num) {
        ReduceCrewVisitorNum reduceCrew = new ReduceCrewVisitorNum();
        Game.getInstance().getCurrentCard().call(reduceCrew, username, i, j, num);
    }

    public void pass(String username) {
        PassVisitor pass = new PassVisitor();
        Game.getInstance().getCurrentCard().call(pass, username);
    }

    public String help(){
        HelpVisitor help = new HelpVisitor();
        return Game.getInstance().getCurrentCard().call(help);
    }

    public void activeCannon(String username, int i , int j, int iBattery, int jBattery){
        ActiveCannonVisitor activeCannon = new ActiveCannonVisitor();
        Game.getInstance().getPlayerFromNickname(username).getTruck().reduceBatteries(iBattery,jBattery,1);
        Game.getInstance().getCurrentCard().call(activeCannon, username, i, j);
    }

    public void activeEngine(String username, int i, int j, int iBattery, int jBattery){
        ActiveEngineVisitor activeEngine = new ActiveEngineVisitor();
        Game.getInstance().getPlayerFromNickname(username).getTruck().reduceBatteries(iBattery,jBattery,1);
        Game.getInstance().getCurrentCard().call(activeEngine, username, i, j);
    }

    public void activeShield(String username, int i, int j, int iBattery, int jBattery){
        ActiveShieldVisitor activeShield = new ActiveShieldVisitor();
        Game.getInstance().getPlayerFromNickname(username).getTruck().reduceBatteries(iBattery,jBattery,1);
        Game.getInstance().getCurrentCard().call(activeShield, username, i, j);
    }

    public void dockStation(String username){
        DockStationVisitor dockStation = new DockStationVisitor();
        Game.getInstance().getCurrentCard().call(dockStation, username);
    }

    public void loadGoods(String username, int i, int j){
        LoadGoodsVisitor loadGoods = new LoadGoodsVisitor();
        Game.getInstance().getCurrentCard().call(loadGoods, username, i, j);
    }

    public void removePreciousItems(String username, int i, int j, int num){
        RemovePreciousItemVisitor removePreciousItem = new RemovePreciousItemVisitor();
        Game.getInstance().getCurrentCard().call(removePreciousItem, username, i, j, num);
    }

    public void ready(String username){
        ReadyVisitor ready = new ReadyVisitor();
        Game.getInstance().getCurrentCard().call(ready, username);
    }

    public void landOnPlanet(String username, int i){
        LandOnPlanetVisitor landOnPlanet = new LandOnPlanetVisitor();
        Game.getInstance().getCurrentCard().call(landOnPlanet, username, i);
    }

    public void removeItem(String username, int i, int j, int index){
        if(!Game.getInstance().getGameStatus().equals(GameStatus.Playing))
            throw new RuntimeException("Not a possible instruction in this game state");
        Game.getInstance().getPlayerFromNickname(username).getTruck().removeGood(i, j, index);
    }







}












