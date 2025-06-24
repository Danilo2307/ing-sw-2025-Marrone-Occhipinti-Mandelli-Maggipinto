package it.polimi.ingsw.psp23.controller;

import it.polimi.ingsw.psp23.model.Game.Board;
import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.exceptions.*;
import it.polimi.ingsw.psp23.model.Events.Event;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.cards.visitor.*;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.model.components.HousingUnit;
import it.polimi.ingsw.psp23.model.enumeration.Color;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.model.enumeration.Side;
import it.polimi.ingsw.psp23.network.UsersConnected;
import it.polimi.ingsw.psp23.network.messages.BroadcastMessage;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.network.socket.Server;
import it.polimi.ingsw.psp23.protocol.response.*;

import java.util.ArrayList;
import java.util.List;

public class Controller {
    // private CardHandler cardHandler;
    // private static Controller instance = null;
    private Timer timer;
    private boolean isFirstBuildingPhaseEnded; // variabile che serve all'handle timeout per capire se la clessidra deve ancora essere girata
    private int currentPosition;
    List<Player> crewPositioned = new ArrayList<>();
    int gameId;


    public Controller(int gameId) {
        // cardHandler = new CardHandler();
        timer = new Timer();
        isFirstBuildingPhaseEnded = false;
        currentPosition = 0;
        this.gameId = gameId;
        Server.getInstance().getGame(gameId).setEventListener(this::onGameEvent);
        Server.getInstance().getGame(gameId).setEventListener2(this::onGameEventString);
    }

    /*public static synchronized Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }*/

    public void addPlayerToGame(String nickname) throws PlayerExistsException, GameFullException {
        Game game = Server.getInstance().getGame(gameId);

        if(game.getGameStatus() == GameStatus.Setup) {
            if (game.getPlayers().size() <= game.getNumRequestedPlayers() || game.getNumRequestedPlayers() == -1)
                game.addPlayer(nickname);
        }
    }

    private void setPlayersColors(Player player, int count) {
        switch (count) {
            case 0 -> player.setColor(Color.Blue);
            case 1 -> player.setColor(Color.Green);
            case 2 -> player.setColor(Color.Red);
            case 3 -> player.setColor(Color.Yellow);
        }
    }

    public void startBuildingPhase() {
        Game game = Server.getInstance().getGame(gameId);
        int count = 0;
        for (Player player : game.getPlayers()) {
            HousingUnit centralCabin = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true, count+900);
            player.getTruck().addComponent(centralCabin,2,3);
            setPlayersColors(player, count);
            Server.getInstance().sendMessage(player.getNickname(), new DirectMessage(new TileResponse(centralCabin)));
            count++;
        }//questo for inizializza la cabina centrale dei player con la prima housing unit

        Server.getInstance().notifyAllObservers(new BroadcastMessage(new StringList(UsersConnected.getInstance().getClients(gameId))), gameId);
        game.setGameStatus(GameStatus.Building);
        // game.getPlayers().forEach(player -> {
           // inizializzazioneNave.getInstance().popolaNave(player);});
        Server.getInstance().notifyAllObservers(new BroadcastMessage(new StateChanged(GameStatus.Building)), gameId);


        if (game.getLevel() != 0) {
            startTimer();
        }
    }

    public void startTimer() {
        if (Server.getInstance().getGame(gameId).getLevel() == 0) {
            throw new LevelException("Non esiste la clessidra nel volo di prova!");
        }

        //la clessidra dura un minuto e mezzo
        timer.startCountdown(90, this::handleTimeout);
    }



    public void handleTimeout() {
        if (!isFirstBuildingPhaseEnded) {
            isFirstBuildingPhaseEnded = true;
            BroadcastMessage bm = new BroadcastMessage(new TimeExpired());
            Server.getInstance().notifyAllObservers(bm, gameId);
        } else { //TODO: in questo else controllo se i player hanno finito tutti, in caso contrario assegno posizioni in ordine di entrata e in case 4 dello switch chiamo startcheckboards
            try {
                startCheckBoard();
            }catch (IllegalStateException e) {
                handleTimeout();
            }

        }

    }



    public void startCheckBoard() throws IllegalTruckException {

        if (Server.getInstance().getGame(gameId).getGameStatus() == GameStatus.Building) {
            timer.shutdown();
            Game game = Server.getInstance().getGame(gameId);
            game.checkReservedTiles();
            game.setGameStatus(GameStatus.CheckBoards);
            Server.getInstance().notifyAllObservers(new BroadcastMessage(new StateChanged(GameStatus.CheckBoards)), gameId);
        }

        for (Player player : Server.getInstance().getGame(gameId).getPlayers()) {
            if (!player.getTruck().check()) {
                Server.getInstance().sendMessage(player.getNickname(), new DirectMessage((new IllegalTruck())));
                return;
            }
        }

        for (Player player : Server.getInstance().getGame(gameId).getPlayers()) {
            player.getTruck().updateAllowedAliens();
        }


        startSetCrew();

    }

    public void startSetCrew() {
        if (Server.getInstance().getGame(gameId).getGameStatus() == GameStatus.CheckBoards) {
            Server.getInstance().getGame(gameId).setGameStatus(GameStatus.SetCrew);
            Server.getInstance().notifyAllObservers(new BroadcastMessage(new StateChanged(GameStatus.SetCrew)), gameId);
        }else{
            throw new RuntimeException("Not in the correct state to set crew");
        }
    }

    public void crewPositioned(String username) {
        if(!crewPositioned.contains(username)) {
            crewPositioned.add(Server.getInstance().getGame(gameId).getPlayerFromNickname(username));
        }else{
            throw new InvalidActionException("You have already declared crew ");
        }
        if(crewPositioned.size() == Server.getInstance().getGame(gameId).getPlayers().size()){
            crewPositioned.clear();
            startFlight();
        }
    }

    public void removeComponent(String username,int i, int j){
        Server.getInstance().getGame(gameId).getPlayerFromNickname(username).getTruck().delete(i,j);
    }



    public void addComponent(String username, Component c, int x, int y) {
        Server.getInstance().getGame(gameId).getPlayerFromNickname(username).getTruck().addComponent(c, x, y);
    }

    public Component getTileFromHeap() {
        return Server.getInstance().getGame(gameId).getTileFromHeap();
    }

    /*public Component getTileUncovered(int position) {
        return Game.getInstance().getTileUncovered(position);
    }*/

    public void releaseTile(Component c) {
        Server.getInstance().getGame(gameId).releaseTile(c);
    }

    public void rotate(Component c) {
        c.rotate();
    }

//TODO: va gestita la possibilità del player di attaccare ancora pezzi una volta che dichiara di aver terminato ovvero dovremmo mettere una lista temporanea per dire chi ha finito

    public void playerFinishedBuilding(String username) {
        Game game = Server.getInstance().getGame(gameId);
        // game.setCurrentPlayer(game.getPlayerFromNickname(username));
        game.getPlayerFromNickname(username).setPosition(game.getFirstPositions()[currentPosition]);
        currentPosition++;
        Server.getInstance().getGame(gameId).sortPlayersByPosition();
        int playerPlacement = Server.getInstance().getGame(gameId).getPlayers().stream().map(player -> player.getNickname()).toList().indexOf(username) + 1;
        Server.getInstance().sendMessage(username, new DirectMessage(new StringResponse("Pedina posizionata! Sei in posizione " + playerPlacement +"\n")));
        if(currentPosition == game.getPlayers().size()) {
            startCheckBoard();
        }
    }

    public void startFlight(){
        Game game = Server.getInstance().getGame(gameId);
        game.setGameStatus(GameStatus.Playing);
        Server.getInstance().notifyAllObservers(new BroadcastMessage(new StateChanged(GameStatus.Playing)), gameId);
        game.setGameStatus(GameStatus.WAITING_FOR_NEW_CARD);
        Server.getInstance().notifyAllObservers(new BroadcastMessage(new StringResponse("Il leader deve pescare la prima carta\n")), gameId);
    }

    // Questo onGameEvent inoltra gli eventi rivolti a tutti i client
    public void onGameEvent(Event event) { //metodo triggerato dall'evento generico di play nel model
        Game game = Server.getInstance().getGame(gameId);
        game.setGameStatus(event.getNewStatus());
        BroadcastMessage broadcastMessage = new BroadcastMessage(new StateChanged(game.getGameStatus()));
        Server.getInstance().notifyAllObservers(broadcastMessage, gameId);
        //qui serve tutta la gestione della chiamata alla view poichè giunti a questo punto
        //avremo l'evento pronto con le informazioni della carta e lo stato già aggiornato dal model con una ripetizione
        // del suo cambiamento all'interno dell'evento (si può anche togliere in futuro)
        // quindi da qui chiamiamo la view e dopo la view chiamerà un altro metodo per mandarci l'input da mandare a play

        // Invia i dati alla View (come JSON, socket, ecc.)

        //network.sendToAllClients("game_event", event);

        // TODO: capire quale evento effettivo bisogna inviare
        Message message = new BroadcastMessage(new UpdateFromCard(event.describe(gameId)));
        Server.getInstance().notifyAllObservers(message, gameId);

    }

    // Questo onGameEvent inoltra gli eventi rivolti ad un solo client
    public void onGameEventString(Event event, String playerUsername) {
        Server.getInstance().getGame(gameId).setGameStatus(event.getNewStatus());

        Message message = new DirectMessage(new UpdateFromCard(event.describe(gameId)));
        Server.getInstance().sendMessage(playerUsername, message);

    }


    public void buyShip(String username) {
        BuyShipVisitor buyShip = new BuyShipVisitor();
        Server.getInstance().getGame(gameId).getCurrentCard().call(buyShip, username);

    }

    public void reduceCrew(String username, int i, int j, int num) {
        ReduceCrewVisitorNum reduceCrew = new ReduceCrewVisitorNum();
        Server.getInstance().getGame(gameId).getCurrentCard().call(reduceCrew, username, i, j, num);
    }

    public void pass(String username) {
        PassVisitor pass = new PassVisitor();
        Server.getInstance().getGame(gameId).getCurrentCard().call(pass, username);
    }

    public String help(String username) {
        HelpVisitor help = new HelpVisitor();
        return Server.getInstance().getGame(gameId).getCurrentCard().call(help, username);
    }

    public void activeCannon(String username, int i , int j, int iBattery, int jBattery){
        Board truck = Server.getInstance().getGame(gameId).getCurrentPlayer().getTruck();
        Component[][] nave = truck.getShip();
        int cannonIndex = truck.getCannons().indexOf(nave[i][j]);
        if(truck.getCannons().get(cannonIndex).isActive()){
            throw new InvalidActionException("Cannon already activated");
        }
        else {
            ActiveCannonVisitor activeCannon = new ActiveCannonVisitor();
            Server.getInstance().getGame(gameId).getPlayerFromNickname(username).getTruck().reduceBatteries(iBattery, jBattery, 1);
            Server.getInstance().getGame(gameId).getCurrentCard().call(activeCannon, username, i, j);
        }
    }

    public void activeEngine(String username, int i, int j, int iBattery, int jBattery){
        ActiveEngineVisitor activeEngine = new ActiveEngineVisitor();
        Server.getInstance().getGame(gameId).getPlayerFromNickname(username).getTruck().reduceBatteries(iBattery,jBattery,1);
        Server.getInstance().getGame(gameId).getCurrentCard().call(activeEngine, username, i, j);
    }

    public void activeShield(String username, int i, int j, int iBattery, int jBattery){
        ActiveShieldVisitor activeShield = new ActiveShieldVisitor();
        Server.getInstance().getGame(gameId).getPlayerFromNickname(username).getTruck().reduceBatteries(iBattery,jBattery,1);
        Server.getInstance().getGame(gameId).getCurrentCard().call(activeShield, username, i, j);
    }

    public void dockStation(String username){
        DockStationVisitor dockStation = new DockStationVisitor();
        Server.getInstance().getGame(gameId).getCurrentCard().call(dockStation, username);
    }

    public void loadGoods(String username, int i, int j){
        LoadGoodsVisitor loadGoods = new LoadGoodsVisitor();
        Server.getInstance().getGame(gameId).getCurrentCard().call(loadGoods, username, i, j);
    }

    public void removePreciousItems(String username, int i, int j, int num){
        RemovePreciousItemVisitor removePreciousItem = new RemovePreciousItemVisitor();
        Server.getInstance().getGame(gameId).getCurrentCard().call(removePreciousItem, username, i, j, num);
    }

    public void ready(String username){
        ReadyVisitor ready = new ReadyVisitor();
        Server.getInstance().getGame(gameId).getCurrentCard().call(ready, username);
    }

    public void landOnPlanet(String username, int i){
        LandOnPlanetVisitor landOnPlanet = new LandOnPlanetVisitor();
        Server.getInstance().getGame(gameId).getCurrentCard().call(landOnPlanet, username, i);
    }

    public void removeItem(String username, int i, int j, int index){
        if(!Server.getInstance().getGame(gameId).getGameStatus().equals(GameStatus.Playing))
            throw new InvalidActionException("Not a possible instruction in this game state");
        Server.getInstance().getGame(gameId).getPlayerFromNickname(username).getTruck().removeGood(i, j, index);
    }







}












