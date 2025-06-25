package it.polimi.ingsw.psp23.model.cards.planets;

import it.polimi.ingsw.psp23.exceptions.CardException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.Game.Item;
import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.model.cards.Meteor;
import it.polimi.ingsw.psp23.model.cards.MeteorSwarm;
import it.polimi.ingsw.psp23.model.cards.Planets;
import it.polimi.ingsw.psp23.model.components.Container;
import it.polimi.ingsw.psp23.model.components.HousingUnit;
import it.polimi.ingsw.psp23.model.enumeration.Color;
import it.polimi.ingsw.psp23.model.enumeration.Direction;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.model.enumeration.Side;
import it.polimi.ingsw.psp23.network.UsersConnected;
import it.polimi.ingsw.psp23.network.rmi.ClientRMIHandler;
import it.polimi.ingsw.psp23.network.rmi.ClientRMIHandlerInterface;
import it.polimi.ingsw.psp23.network.rmi.ClientRegistry;
import it.polimi.ingsw.psp23.network.rmi.ClientRegistryInterface;
import it.polimi.ingsw.psp23.network.socket.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlanetsTest2 {
    //TEST DI PLANETS IN CUI L'UTENTE SBAGLIA: NON SEGUE L'ORDINE, VUOLE ATTERRARE SU UN PIANETA OCCUPATO
    Game game;
    Player p1, p2;
    Planets card;
    MeteorSwarm nextCard;

    @BeforeEach
    void setUp() throws Exception {
        
        try {
    Registry rmiRegistry = LocateRegistry.createRegistry(1099);
    ClientRegistryInterface clientRegistry = new ClientRegistry();
    rmiRegistry.rebind("ClientRegistry", clientRegistry);
    ClientRMIHandlerInterface rmiServer = new ClientRMIHandler(clientRegistry);
    rmiRegistry.rebind("GameServer", rmiServer);
    Server.getInstance("localhost", 8000, rmiServer);
} catch (Exception ignored) {
    // Silently ignore RMI registry errors in tests
}
        
        this.game = new Game(2,0);
        Server.getInstance().addGame(game);
        UsersConnected.getInstance().addGame();
        UsersConnected.getInstance().addClient("Albi", 0);
        UsersConnected.getInstance().addClient("Fede", 0);

        game.addPlayer("Albi");
        game.addPlayer("Fede");

        p1 = game.getPlayerFromNickname("Albi");
        p2 = game.getPlayerFromNickname("Fede");

        HousingUnit h1 = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true,1);
        HousingUnit h2 = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true,1);
        Container c1 = new Container(Side.EMPTY, Side.EMPTY, Side.EMPTY, Side.EMPTY, 6, Color.Red, new ArrayList<>(),1);
        Container c2 = new Container(Side.EMPTY, Side.EMPTY, Side.EMPTY, Side.EMPTY, 6, Color.Blue, new ArrayList<>(),1);
        Container c3 = new Container(Side.EMPTY, Side.EMPTY, Side.EMPTY, Side.EMPTY, 6, Color.Red, new ArrayList<>(),1);
        Container c4 = new Container(Side.EMPTY, Side.EMPTY, Side.EMPTY, Side.EMPTY, 6, Color.Blue, new ArrayList<>(),1);
        h1.moveToHand();
        h2.moveToHand();
        c1.moveToHand();
        c2.moveToHand();
        c3.moveToHand();
        c4.moveToHand();
        // la cabina centrale non richiede adiacenza
        p1.getTruck().addComponent(h1, 2, 3);
        p2.getTruck().addComponent(h2, 2, 3);
        p1.getTruck().addComponent(c1, 1, 3);
        p1.getTruck().addComponent(c2, 1, 4);
        p2.getTruck().addComponent(c3, 1, 3);
        p2.getTruck().addComponent(c4, 1, 4);

        p1.setPosition(12);
        p2.setPosition(10);
        game.sortPlayersByPosition();

        card = new Planets(2,4, List.of(List.of(new Item(Color.Red), new Item(Color.Red), new Item(Color.Red), new Item(Color.Yellow)), List.of(new Item(Color.Red),new Item(Color.Red), new Item(Color.Green), new Item(Color.Green)), List.of(new Item(Color.Red), new Item(Color.Blue), new Item(Color.Blue), new Item(Color.Blue), new Item(Color.Blue))),1);
        nextCard = new MeteorSwarm(2, List.of(new Meteor(false, Direction.UP), new Meteor(false, Direction.UP), new Meteor(true, Direction.LEFT), new Meteor(false, Direction.LEFT), new Meteor(false, Direction.LEFT)),1);
    }

    @Test
    void testLandingAndLoading() throws CardException {
        // INIT
        card.initPlay("Fede");
        assertEquals(GameStatus.INIT_PLANETS, game.getGameStatus());

        // Albi atterra sul pianeta 1
        card.landOnPlanet("Albi", 1);
        assertEquals("Albi", card.getPlanetsOccupied()[0]);
        assertEquals(p2.getNickname(), game.getCurrentPlayer().getNickname());

        // Fede atterra sul pianeta 2
        card.landOnPlanet("Fede", 2);
        assertEquals("Fede", card.getPlanetsOccupied()[1]);
        // Dopo l’ultimo atterraggio, si applica la penalty e finisce la fase
        assertEquals(GameStatus.END_PLANETS, game.getGameStatus());

        // Verifica che i marker sulla board siano arretrati di 4 spazi
        assertEquals(8, p1.getPosition());
        assertEquals(6, p2.getPosition());

        // Ora prepariamo le board per il loading:
        // dobbiamo avere dei Container in posizioni note (i,j).

        // Albi carica item
        card.loadGoods("Albi", 1, 3);
        card.loadGoods("Albi", 1, 3);
        card.loadGoods("Albi", 1, 3);
        card.loadGoods("Albi", 1, 4);
        assertThrows(CardException.class, () -> card.loadGoods("Albi", 1, 3));

        // Fede carica item
        card.loadGoods("Fede", 1, 3);
        card.loadGoods("Fede", 1, 3);
        card.loadGoods("Fede", 1, 4);
        GameStatus before = game.getGameStatus();
        card.loadGoods("Fede", 1, 4);

        GameStatus after = game.getGameStatus();
        System.out.println("GameStatus: " + before + " → " + after);

        assertNotEquals(before, after);

    }
}
