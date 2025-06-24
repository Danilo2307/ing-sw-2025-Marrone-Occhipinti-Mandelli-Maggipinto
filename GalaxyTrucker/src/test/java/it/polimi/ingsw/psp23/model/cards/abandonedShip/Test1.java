package it.polimi.ingsw.psp23.model.cards.abandonedShip;

import it.polimi.ingsw.psp23.exceptions.CardException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.Game.Item;
import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.model.cards.AbandonedShip;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class Test1 {
    // PRIMO PASSA E SECONDO COMPRA LA NAVE
    Game game;
    Player p1, p2;
    AbandonedShip card;

    @BeforeEach
    void setUp() {
        try {
            Registry rmiRegistry = LocateRegistry.createRegistry(1099);
            ClientRegistryInterface clientRegistry = new ClientRegistry();
            rmiRegistry.rebind("ClientRegistry", clientRegistry);
            ClientRMIHandlerInterface rmiServer = new ClientRMIHandler(clientRegistry);
            rmiRegistry.rebind("GameServer", rmiServer);
            Server.getInstance("localhost", 8000, rmiServer);
        }
        catch (Exception e) {
            System.out.println("\n\n\nerrore!!!\n\n\n");
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

        HousingUnit h1 = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true, 1);
        HousingUnit h2 = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true, 1);
        HousingUnit h3 = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, false, 1);
        HousingUnit h4 = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, false, 1);
        HousingUnit h5 = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, false,1);
        HousingUnit h6 = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, false,1);
        h3.setAstronaut();
        h4.setAstronaut();
        h5.setAstronaut();
        h6.setAstronaut();
        h1.moveToHand();
        h2.moveToHand();
        h3.moveToHand();
        h4.moveToHand();
        h5.moveToHand();
        h6.moveToHand();
        // la cabina centrale non richiede adiacenza
        p1.getTruck().addComponent(h1, 2, 3);
        p2.getTruck().addComponent(h2, 2, 3);
        p1.getTruck().addComponent(h3, 1, 3);
        p1.getTruck().addComponent(h4, 1, 4);
        p2.getTruck().addComponent(h5, 1, 3);
        p2.getTruck().addComponent(h6, 1, 4);

        p1.setPosition(12);
        p2.setPosition(10);
        game.sortPlayersByPosition();

        card = new AbandonedShip(1,1,4,3,1);
    }

    @Test
    void testBuyAndReduce() throws CardException, InvocationTargetException, IllegalAccessException {
        String expected = "è uscita la carta Abandoned Ship\n" +
                "l'equipaggio da perdere ammonta a 3\n" +
                "i crediti cosmici disponibili sono 4\n" +
                "i giorni persi sarebbero 1";
        assertEquals(expected, card.toString());
        // INIT
        card.initPlay("Fede");
        String result = card.help("Fede");
        assertEquals("Available commands: COMPRANAVE, PASSA\n", result);
        card.toString();
        card.help("Fede");
        assertEquals(GameStatus.INIT_ABANDONEDSHIP, game.getGameStatus());

        // Albi passa
        card.pass("Albi");
        assertEquals(p2.getNickname(), game.getCurrentPlayer().getNickname());

        // Fede compra la nave
        card.buyShip("Fede");
        // Dopo l’ultimo atterraggio, si applica la penalty e finisce la fase
        assertEquals(GameStatus.END_ABANDONEDSHIP, game.getGameStatus());

        String result1 = card.help("Fede");
        assertEquals("Available commands: REDUCECREW\n", result1);

        // Verifica che i marker sulla board siano arretrati di 4 spazi
        assertEquals(12, p1.getPosition());
        assertEquals(9, p2.getPosition());

        //Riduzione crew Fede
        GameStatus before = game.getGameStatus();
        card.reduceCrew("Fede", 1, 3, 2);
        card.reduceCrew("Fede", 1, 4, 1);

        GameStatus after = game.getGameStatus();
        System.out.println("GameStatus: " + before + " → " + after);

        assertNotEquals(before, after);

    }
}
