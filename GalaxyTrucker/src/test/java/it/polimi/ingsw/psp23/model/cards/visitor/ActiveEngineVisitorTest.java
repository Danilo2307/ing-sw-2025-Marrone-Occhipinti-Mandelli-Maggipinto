package it.polimi.ingsw.psp23.model.cards.visitor;

import it.polimi.ingsw.psp23.exceptions.CardException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.Game.Item;
import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.model.cards.*;
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

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class ActiveEngineVisitorTest {

    Game game;
    Player p1;
    private ActiveEngineVisitor visitor;
    private CombatZone combatZone;
    private OpenSpace openSpace;

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

        game.addPlayer("Albi");

        p1 = game.getPlayerFromNickname("Albi");



        //CABINE CENTRALI
        HousingUnit ha1 = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true,1);
        ha1.moveToHand();
        HousingUnit hf1 = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true,1);
        hf1.moveToHand();
        HousingUnit hg1 = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true,1);
        hg1.moveToHand();
        p1.getTruck().addComponent(ha1, 2, 3);

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
        Engine ea1 = new Engine(Side.UNIVERSAL_CONNECTOR, Side.ENGINE, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true,1);
        ea1.moveToHand();
        p1.getTruck().addComponent(ea1, 3, 2);

        Item ia1 = new Item(Color.Yellow);
        ca1.loadItem(ia1);
        Item ia2 = new Item(Color.Yellow);
        ca1.loadItem(ia2);
        Item ia3 = new Item(Color.Blue);
        ca1.loadItem(ia3);
        Item ia4 = new Item(Color.Red);
        ca2.loadItem(ia4);


        //SET POS INIZIALI
        p1.setPosition(12);
        game.sortPlayersByPosition();

        game.setCurrentPlayer(p1);
        combatZone = new CombatZone(1,3,0,2, List.of(Challenge.Members, Challenge.EngineStrength, Challenge.CannonStrength), List.of(new CannonShot(false, Direction.DOWN), new CannonShot(true, Direction.DOWN)),1);
        openSpace = new OpenSpace(1,1);
    }


    @Test
    public void testVisitForCombatZone() {
        String username = "Albi";
        int i = 5;
        int j = 3;

        visitor = new ActiveEngineVisitor();

        CardException ex = assertThrows(
                CardException.class,
                () -> visitor.visitForCombatZone(combatZone, username, 5, 3)
        );

        assertEquals("Cannot activate engine now: phase is Setup", ex.getMessage());

        i = 3;
        j = 2;

        ex = assertThrows(
                CardException.class,
                () -> visitor.visitForCombatZone(combatZone, username, 3, 2)
        );

        assertEquals("Cannot activate engine now: phase is Setup", ex.getMessage());

        game.setGameStatus(GameStatus.SECOND_COMBATZONE);
        visitor.visitForCombatZone(combatZone, username, i, j);

        assertEquals(true, p1.getTruck().getEngines().get(p1.getTruck().getEngines().indexOf(p1.getTruck().getShip()[i][j])).isActive() );

    }

    @Test
    public void testVisitForOpenSpace() {
        String username = "Albi";
        int i = 3;
        int j = 2;

        visitor = new ActiveEngineVisitor();

        CardException ex = assertThrows(
                CardException.class,
                () -> visitor.visitForCombatZone(combatZone, username, 3, 2)
        );

        assertEquals("Cannot activate engine now: phase is Setup", ex.getMessage());

        game.setGameStatus(GameStatus.INIT_OPENSPACE);
        visitor.visitForOpenSpace(openSpace, username, i, j);

        assertEquals(true, p1.getTruck().getEngines().get(p1.getTruck().getEngines().indexOf(p1.getTruck().getShip()[i][j])).isActive() );

    }

}
