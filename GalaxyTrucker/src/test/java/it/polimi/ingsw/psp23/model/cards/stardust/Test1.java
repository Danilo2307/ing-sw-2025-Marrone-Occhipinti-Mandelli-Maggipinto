package it.polimi.ingsw.psp23.model.cards.stardust;

import it.polimi.ingsw.psp23.exceptions.CardException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.Game.Item;
import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.model.cards.AbandonedStation;
import it.polimi.ingsw.psp23.model.cards.Stardust;
import it.polimi.ingsw.psp23.model.components.Container;
import it.polimi.ingsw.psp23.model.components.HousingUnit;
import it.polimi.ingsw.psp23.model.enumeration.Color;
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
    Game game;
    Player p1, p2;
    Stardust card;

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

        HousingUnit h1 = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true,1);
        HousingUnit h2 = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true,1);
        HousingUnit h3 = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, false,1);
        HousingUnit h4 = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, false,1);
        HousingUnit h5 = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, false,1);
        HousingUnit h6 = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, false,1);
        Container c1 = new Container(Side.EMPTY, Side.EMPTY, Side.EMPTY, Side.EMPTY, 6, Color.Blue, new ArrayList<>(),1);
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
        c1.moveToHand();
        // la cabina centrale non richiede adiacenza
        p1.getTruck().addComponent(h1, 2, 3);
        p2.getTruck().addComponent(h2, 2, 3);
        p1.getTruck().addComponent(h3, 1, 3);
        p1.getTruck().addComponent(h4, 1, 4);
        p2.getTruck().addComponent(h5, 1, 3);
        p2.getTruck().addComponent(h6, 1, 4);
        p2.getTruck().addComponent(c1, 1, 5);

        p1.setPosition(12);
        p2.setPosition(10);

        card = new Stardust(2,1);

    }

    @Test
    void testStardust() throws CardException, InvocationTargetException, IllegalAccessException {
        String resultToStringStardust = card.toString();
        String expected = "è uscita la carta Stardust\n" +
                "si perdono tanti giorni quanti sono i connettori esposti";
        assertEquals(expected, resultToStringStardust);
        String resultHelpStardust = card.help("Fede");
        assertEquals("No commands available for Stardust; effect is automatic.", resultHelpStardust);

        // INIT
        card.initPlay("Fede");
        assertEquals(4, p1.getPosition());
        assertEquals(3, p2.getPosition());
        GameStatus after = game.getGameStatus();
        System.out.println("GameStatus: " + after);
    }
}
