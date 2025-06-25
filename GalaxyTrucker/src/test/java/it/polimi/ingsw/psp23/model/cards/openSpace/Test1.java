package it.polimi.ingsw.psp23.model.cards.openSpace;

import it.polimi.ingsw.psp23.exceptions.CardException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.Game.Item;
import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.model.cards.OpenSpace;
import it.polimi.ingsw.psp23.model.cards.Smugglers;
import it.polimi.ingsw.psp23.model.cards.visitor.HelpVisitor;
import it.polimi.ingsw.psp23.model.cards.visitor.InitPlayVisitor;
import it.polimi.ingsw.psp23.model.cards.visitor.ReadyVisitor;
import it.polimi.ingsw.psp23.model.components.*;
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

import static org.junit.jupiter.api.Assertions.*;

public class Test1 {
    //TEST FUNZIONALITA CORRETTA
    Game game;
    Player p1, p2, p3;
    OpenSpace card;

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
        UsersConnected.getInstance().addClient("Gigi", 0);

        game.addPlayer("Albi");
        game.addPlayer("Fede");
        game.addPlayer("Gigi");

        p1 = game.getPlayerFromNickname("Albi");
        p2 = game.getPlayerFromNickname("Fede");
        p3 = game.getPlayerFromNickname("Gigi");


        //CABINE CENTRALI
        HousingUnit ha1 = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true,1);
        ha1.moveToHand();
        HousingUnit hf1 = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true,1);
        hf1.moveToHand();
        HousingUnit hg1 = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true,1);
        hg1.moveToHand();
        p1.getTruck().addComponent(ha1, 2, 3);
        p2.getTruck().addComponent(hf1, 2, 3);
        p3.getTruck().addComponent(hg1, 2, 3);

        //BOARD ALBI
        Container ca1 = new Container(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, 3, Color.Blue, new ArrayList<>(),1);
        ca1.moveToHand();
        p1.getTruck().addComponent(ca1, 3, 3);
        BatteryHub ba1 = new BatteryHub(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, 2,1);
        ba1.moveToHand();
        p1.getTruck().addComponent(ba1, 2, 2);
        Cannon caa1 = new Cannon(Side.GUN, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, false,1);
        caa1.moveToHand();
        p1.getTruck().addComponent(caa1, 1, 3);
        Cannon caa2 = new Cannon(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.GUN, true,1);
        caa2.moveToHand();
        p1.getTruck().addComponent(caa2, 2, 4);
        Cannon caa3 = new Cannon(Side.GUN, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, false,1);
        caa3.moveToHand();
        p1.getTruck().addComponent(caa3, 1, 2);
        Cannon caa4 = new Cannon(Side.GUN, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true,1);
        caa4.moveToHand();
        p1.getTruck().addComponent(caa4, 1, 4);
        Container ca2 = new Container(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, 1, Color.Red, new ArrayList<>(),1);
        ca2.moveToHand();
        p1.getTruck().addComponent(ca2, 3, 4);
        Engine ea1 = new Engine(Side.UNIVERSAL_CONNECTOR, Side.ENGINE, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, false,1);
        ea1.moveToHand();
        p1.getTruck().addComponent(ea1, 3, 2);
        Engine ea2 = new Engine(Side.UNIVERSAL_CONNECTOR, Side.ENGINE, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true,1);
        ea2.moveToHand();
        p1.getTruck().addComponent(ea2, 3, 5);
        Engine ea3 = new Engine(Side.UNIVERSAL_CONNECTOR, Side.ENGINE, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true,1);
        ea3.moveToHand();
        p1.getTruck().addComponent(ea3, 3, 1);


        Item ia1 = new Item(Color.Yellow);
        ca1.loadItem(ia1);
        Item ia2 = new Item(Color.Yellow);
        ca1.loadItem(ia2);
        Item ia3 = new Item(Color.Blue);
        ca1.loadItem(ia3);
        Item ia4 = new Item(Color.Red);
        ca2.loadItem(ia4);

        //BOARD FEDE
        Cannon caf1 = new Cannon(Side.GUN, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true,1);
        caf1.moveToHand();
        p2.getTruck().addComponent(caf1, 1, 3);
        BatteryHub bf1 = new BatteryHub(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, 3,1);
        bf1.moveToHand();
        p2.getTruck().addComponent(bf1, 2, 4);
        Container cf1 = new Container(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, 2, Color.Blue, new ArrayList<>(),1);
        cf1.moveToHand();
        p2.getTruck().addComponent(cf1, 2, 2);
        Engine ef1 = new Engine(Side.UNIVERSAL_CONNECTOR, Side.ENGINE, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, false,1);
        ef1.moveToHand();
        p2.getTruck().addComponent(ef1, 3, 3);
        Cannon caf2 = new Cannon(Side.GUN, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true,1);
        caf2.moveToHand();
        p2.getTruck().addComponent(caf2, 1, 4);
        Cannon caf3 = new Cannon(Side.UNIVERSAL_CONNECTOR, Side.GUN, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, false,1);
        caf3.moveToHand();
        p2.getTruck().addComponent(caf3, 3, 4);
        BatteryHub bf2 = new BatteryHub(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, 2,1);
        bf2.moveToHand();
        p2.getTruck().addComponent(bf2, 3, 2);
        Engine ef2 = new Engine(Side.UNIVERSAL_CONNECTOR, Side.ENGINE, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true,1);
        ef2.moveToHand();
        p2.getTruck().addComponent(ef2, 3, 5);
        Engine ef3 = new Engine(Side.UNIVERSAL_CONNECTOR, Side.ENGINE, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true,1);
        ef3.moveToHand();
        p2.getTruck().addComponent(ef3, 3, 1);

        //BOARD GIGI
        HousingUnit hg2 = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, false,1);
        hg2.moveToHand();
        p3.getTruck().addComponent(hg2, 3, 3);
        BatteryHub bg1 = new BatteryHub(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, 3,1);
        bg1.moveToHand();
        p3.getTruck().addComponent(bg1, 2, 2);
        Container cg1 = new Container(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, 2, Color.Red, new ArrayList<>(),1);
        cg1.moveToHand();
        p3.getTruck().addComponent(cg1, 2, 4);
        Cannon cag1 = new Cannon(Side.GUN, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true,1);
        cag1.moveToHand();
        p3.getTruck().addComponent(cag1, 1, 3);
        Cannon cag2 = new Cannon(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.GUN, Side.UNIVERSAL_CONNECTOR, false,1);
        cag2.moveToHand();
        p3.getTruck().addComponent(cag2, 1, 2);
        Cannon cag3 = new Cannon(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.GUN, false,1);
        cag3.moveToHand();
        p3.getTruck().addComponent(cag3, 1, 4);
        Engine eg1 = new Engine(Side.UNIVERSAL_CONNECTOR, Side.ENGINE, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, false,1);
        eg1.moveToHand();
        p3.getTruck().addComponent(eg1, 3, 2);
        Engine eg2 = new Engine(Side.UNIVERSAL_CONNECTOR, Side.ENGINE, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true,1);
        eg2.moveToHand();
        p3.getTruck().addComponent(eg2, 3, 4);
        Engine eg3 = new Engine(Side.UNIVERSAL_CONNECTOR, Side.ENGINE, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true,1);
        eg3.moveToHand();
        p3.getTruck().addComponent(eg3, 3, 1);

        Item ig1 = new Item(Color.Red);
        cg1.loadItem(ig1);

        //SET POS INIZIALI
        p1.setPosition(12);
        p2.setPosition(10);
        p3.setPosition(8);
        game.sortPlayersByPosition();

        card = new OpenSpace(1,1);
    }

    @Test
    void testOpenSpace() throws CardException, InvocationTargetException, IllegalAccessException {
        HelpVisitor helpvisitor = new HelpVisitor();
        String resultHelpEngineAlwaysAvailable = helpvisitor.visitForOpenSpace(card, "Fede");
        assertEquals("Available commands: ATTIVA MOTORE, PRONTO\n", resultHelpEngineAlwaysAvailable);
        String resultToStringOpenSpace = card.toString();
        String expected = "È uscita la carta open space\n" +
                "più motori si attivano, più si va avanti!";
        assertEquals(expected, resultToStringOpenSpace);

        // INIT
        InitPlayVisitor playvisitor9 = new InitPlayVisitor();
        playvisitor9.visitForOpenSpace(card, "Fede");
        assertEquals(GameStatus.INIT_OPENSPACE, game.getGameStatus());

        // Albi attiva un motore
        card.activeEngine("Albi", 3, 5);
//        assertEquals(4, p1.getTruck().calculateCannonStrength());
//        assertEquals(1, p1.getTruck().calculateEngineStrength());
        assertThrows(CardException.class, () -> card.activeEngine("Fede", 1, 5));
        assertThrows(CardException.class, () -> card.ready("Fede"));
        ReadyVisitor readyvisitor = new ReadyVisitor();
        readyvisitor.visitForOpenSpace(card, "Albi");
        assertEquals(p2.getNickname(), game.getCurrentPlayer().getNickname());

        // Fede attiva due motori
        card.activeEngine("Fede", 3, 5);
        card.activeEngine("Fede", 3, 1);
//        assertEquals(4.5, p2.getTruck().calculateCannonStrength());
        card.ready("Fede");

        GameStatus before = game.getGameStatus();

        // Gigi non attiva niente
//        assertEquals(4.5, p2.getTruck().calculateCannonStrength());
        card.ready("Gigi");

        assertEquals(15, p1.getPosition());
        assertEquals(16, p2.getPosition());
        assertEquals(9, p3.getPosition());

        GameStatus after = game.getGameStatus();
        System.out.println("GameStatus: " + before + " → " + after);

        //PASSA ALLA CARTA SUCCESSIVA
        assertNotEquals(before, after);

    }
}
