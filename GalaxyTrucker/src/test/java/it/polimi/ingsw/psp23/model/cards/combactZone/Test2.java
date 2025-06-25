package it.polimi.ingsw.psp23.model.cards.combactZone;

import it.polimi.ingsw.psp23.exceptions.CardException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.Game.Item;
import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.model.cards.CannonShot;
import it.polimi.ingsw.psp23.model.cards.CombatZone;
import it.polimi.ingsw.psp23.model.cards.visitor.ActiveShieldVisitor;
import it.polimi.ingsw.psp23.model.cards.visitor.ReduceCrewVisitorNum;
import it.polimi.ingsw.psp23.model.cards.visitor.RemoveBatteriesVisitor;
import it.polimi.ingsw.psp23.model.cards.visitor.RemovePreciousItemVisitor;
import it.polimi.ingsw.psp23.model.components.*;
import it.polimi.ingsw.psp23.model.enumeration.*;
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

public class Test2 {
    //TEST SU CARTA LIVELLO 2
    Game game;
    Player p1, p2, p3;
    CombatZone card;

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
        Shield sa1 = new Shield(Side.SHIELD, Side.SHIELD, Side.SHIELD, Side.SHIELD,1);
        sa1.moveToHand();
        p1.getTruck().addComponent(sa1, 1, 5);

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
        hg2.setAstronaut();

        Item ig1 = new Item(Color.Red);
        cg1.loadItem(ig1);

        //SET POS INIZIALI
        p1.setPosition(12);
        p2.setPosition(10);
        p3.setPosition(8);
        game.sortPlayersByPosition();

        card = new CombatZone(2,4,3,0, List.of(Challenge.CannonStrength, Challenge.EngineStrength, Challenge.Members), List.of(new CannonShot(false, Direction.UP), new CannonShot(false, Direction.LEFT), new CannonShot(false, Direction.RIGHT), new CannonShot(true, Direction.DOWN)),1);
    }

    @Test
    void testCombatZone() throws CardException, InvocationTargetException, IllegalAccessException {
        String expected = "è uscita la carta Combat Zone:\n" +
                " La prima sfida riguarda la potenza di fuoco (penalità: 4 giorni)\n" +
                "la seconda sfida riguarda la potenza motrice (penalità: 3 merci importanti)\n" +
                "la terza sfida riguarda i membri dell'equipaggio (penalità colpi di cannone: " +
                card.getCannonShot().toString() + "\n";
        String resultComplex = card.toString();

        assertEquals(expected, resultComplex);
        // INIT
        card.initPlay("Fede");
        //SI PARTE CON LA PRIMA SFIDA
        assertEquals(GameStatus.FIRST_COMBATZONE, game.getGameStatus());
        String resultFirst = card.help("Fede");
        assertEquals("Available commands: ATTIVACANNONE, READY\n", resultFirst);

        //ALBI ATTIVA UNO DEI SUOI CANNONI
        card.activeCannon("Albi", 1,4);
        card.ready("Albi");

        //FEDE NE ATTIVA 1 E PERDE
        card.activeCannon("Fede", 1,4);
        card.ready("Fede");

        //GIGI ATTIVA 1
        card.activeCannon("Gigi", 1,3);
        card.ready("Gigi");

        //FEDE HA PERSO LA PRIMA SFIDA
        assertEquals(5, p2.getPosition());
        //ALBI DIVENTA IL LEADER, POI GIGI E ALBI
        assertEquals("Albi", game.getCurrentPlayer().getNickname());
        //SI PASSA ALLA SECONDA SFIDA
        assertEquals(GameStatus.SECOND_COMBATZONE, game.getGameStatus());

        //SECONDA SFIDA
        //ALBI ATTIVA I SUOI MOTORI
        card.activeEngine("Albi", 3,1);
        card.activeEngine("Albi", 3,5);
        card.ready("Albi");

        //GIGI NON NE ATTIVA E PERDE
        card.ready("Gigi");

        //FEDE NE ATTIVA SOLO UNO
        card.activeEngine("Fede", 3,1);
        card.ready("Fede");

        //GIGI DEVE SCONTARE LA PENALITA'
        RemovePreciousItemVisitor itemvisitor = new RemovePreciousItemVisitor();
        itemvisitor.visitForCombatZone(card, "Gigi", 2, 4, 1);
        RemoveBatteriesVisitor batteriesvisitor = new RemoveBatteriesVisitor();
        batteriesvisitor.visitForCombatZone(card, "Gigi", 2, 2, 2);
        assertEquals(0, p3.getTruck().calculateGoods());
        assertEquals(1, p3.getTruck().calculateBatteriesAvailable());

        //SI PASSA ALLA TERZA CON ALBU IN TESTA
        assertEquals("Albi", game.getCurrentPlayer().getNickname());
        assertEquals(GameStatus.ENDTHIRD_COMBATZONE, game.getGameStatus());

        //ALBI PERDE LA SFIDA E AFFRONTA IL PRIMO CANNONE CON LO SCUDO
        ActiveShieldVisitor visitor = new ActiveShieldVisitor();
        visitor.visitForCombatZone(card, "Albi", 1, 5);
        card.ready("Albi");
        card.ready("Albi");
        GameStatus before = game.getGameStatus();
        card.ready("Albi");

        //PASSA ALLA CARTA SUCCESSIVA
        GameStatus after = game.getGameStatus();
        System.out.println("GameStatus: " + before + " → " + after);
        assertNotEquals(before, after);

    }
}
