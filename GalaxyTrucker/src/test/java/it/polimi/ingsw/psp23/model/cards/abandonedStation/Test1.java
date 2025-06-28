package it.polimi.ingsw.psp23.model.cards.abandonedStation;

import it.polimi.ingsw.psp23.exceptions.CardException;
import it.polimi.ingsw.psp23.exceptions.ItemException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.helpers.Item;
import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.model.cards.AbandonedStation;
import it.polimi.ingsw.psp23.model.cards.visitor.*;
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
import it.polimi.ingsw.psp23.network.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class Test1 {
    //TEST COMPLETO (MOLTE FUNZIONALITA' GIA' TESTATE IN ABANDONEDSHIP)
    Game game;
    Player p1, p2;
    AbandonedStation card;

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

        card = new AbandonedStation(1,1,5,List.of(new Item(Color.Yellow), new Item(Color.Green)),1);

    }

    @Test
    void testDockAndLoading() throws CardException, InvocationTargetException, IllegalAccessException {
        String expected = "è uscita la carta Abandoned Station\n" +
                "i membri richiesti sono 5\n" +
                "le merci disponibili sono [gialla, verde]\n" +
                "i giorni persi sarebbero 1\n";
        assertEquals(expected, card.toString());

        // INIT
        InitPlayVisitor playvisitor2 = new InitPlayVisitor();
        playvisitor2.visitForAbandonedStation(card, "Fede");
        HelpVisitor helpvisitor = new HelpVisitor();
        String result = helpvisitor.visitForAbandonedStation(card, "Fede");
        assertEquals(GameStatus.INIT_ABANDONEDSTATION, game.getGameStatus());

        assertEquals("Available commands: ATTRACCA, PASSA\n", result);

        // Albi passa
        assertThrows(CardException.class, () -> card.loadGoods("Gigi", 1, 5));
        assertThrows(CardException.class, () -> card.dockStation("Gigi"));
        assertThrows(CardException.class, () -> card.pass("Gigi"));
        card.pass("Albi");
        assertEquals(p2.getNickname(), game.getCurrentPlayer().getNickname());

        // Fede attracca
//        card.pass("Fede");
        DockStationVisitor visitor = new DockStationVisitor();
        visitor.visitForAbandonedStation(card, "Fede");
        assertEquals(GameStatus.END_ABANDONEDSTATION, game.getGameStatus());
        assertThrows(CardException.class, () -> card.loadGoods("Gigi", 1, 5));
        assertThrows(CardException.class, () -> card.dockStation("Gigi"));
        String resultEnd = card.help("Fede");
        assertEquals("Available commands: LOADGOODS, PERDI, PASSA\n", resultEnd);

        assertEquals(12, p1.getPosition());
        assertEquals(9, p2.getPosition());

        //Caricamento
        GameStatus before = game.getGameStatus();
        assertThrows(ItemException.class, () -> card.loadGoods("Fede", 10, 20));
        LoadGoodsVisitor loadvisitor = new LoadGoodsVisitor();
        loadvisitor.visitForAbandonedStation(card, "Fede", 1, 5);
        PassVisitor passvisitor = new PassVisitor();
        passvisitor.visitForAbandonedStation(card, "Fede");

        GameStatus after = game.getGameStatus();
        System.out.println("GameStatus: " + before + " → " + after);

        assertNotEquals(before, after);

    }
}
