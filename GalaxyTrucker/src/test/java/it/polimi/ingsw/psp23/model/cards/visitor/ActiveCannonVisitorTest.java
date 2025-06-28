package it.polimi.ingsw.psp23.model.cards.visitor;

import it.polimi.ingsw.psp23.exceptions.CardException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.helpers.CannonShot;
import it.polimi.ingsw.psp23.model.helpers.Item;
import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.model.cards.*;
import it.polimi.ingsw.psp23.model.components.*;
import it.polimi.ingsw.psp23.model.enumeration.*;
import it.polimi.ingsw.psp23.model.helpers.Meteor;
import it.polimi.ingsw.psp23.network.UsersConnected;
import it.polimi.ingsw.psp23.network.rmi.ClientRMIHandler;
import it.polimi.ingsw.psp23.network.rmi.ClientRMIHandlerInterface;
import it.polimi.ingsw.psp23.network.rmi.ClientRegistry;
import it.polimi.ingsw.psp23.network.rmi.ClientRegistryInterface;
import it.polimi.ingsw.psp23.network.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class ActiveCannonVisitorTest {

    Game game;
    Player p1;
    private ActiveCannonVisitor visitor;
    private Smugglers smugglers;
    private CombatZone combatZone;
    private MeteorSwarm meteorSwarm;
    private Pirates pirates;
    private Slavers slavers;

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
        Engine ea1 = new Engine(Side.UNIVERSAL_CONNECTOR, Side.ENGINE, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, false,1);
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
        smugglers = new Smugglers(1, 4, 2, 1, List.of(new Item(Color.Yellow), new Item(Color.Green), new Item(Color.Blue)),1);
        combatZone = new CombatZone(1,3,0,2, List.of(Challenge.Members, Challenge.EngineStrength, Challenge.CannonStrength), List.of(new CannonShot(false, Direction.DOWN), new CannonShot(true, Direction.DOWN)),1);
        meteorSwarm = new MeteorSwarm(1, List.of(new Meteor(false, Direction.UP), new Meteor(false, Direction.UP), new Meteor(false, Direction.LEFT), new Meteor(false, Direction.RIGHT), new Meteor(false, Direction.DOWN)),1);
        pirates = new Pirates(1, 4, 1, 4, List.of(new CannonShot(false, Direction.UP), new CannonShot(true, Direction.UP), new CannonShot(false, Direction.UP)),1);
        slavers = new Slavers(1, 4, 3, 5,1,1);
    }

    @Test
    public void testVisitForSmugglersCallsActiveCannon() {
        String username = "Albi";
        int i = 1;
        int j = 4;

        visitor = new ActiveCannonVisitor();

        CardException ex = assertThrows(
                CardException.class,
                () -> visitor.visitForSmugglers(smugglers, username, i, j)
        );

        assertEquals("Cannot activate cannon now: phase is Setup", ex.getMessage());

        game.setGameStatus(GameStatus.INIT_SMUGGLERS);
        visitor.visitForSmugglers(smugglers, username, i, j);

        assertEquals(true, p1.getTruck().getCannons().get(p1.getTruck().getCannons().indexOf(p1.getTruck().getShip()[1][4])).isActive() );

    }

    @Test
    public void testVisitForCombatZone() {
        String username = "Albi";
        int i = 1;
        int j = 4;

        visitor = new ActiveCannonVisitor();

        CardException ex = assertThrows(
                CardException.class,
                () -> visitor.visitForCombatZone(combatZone, username, i, j)
        );

        assertEquals("Cannot activate cannon now: phase is Setup", ex.getMessage());

        game.setGameStatus(GameStatus.FIRST_COMBATZONE);
        visitor.visitForCombatZone(combatZone, username, i, j);

        assertEquals(true, p1.getTruck().getCannons().get(p1.getTruck().getCannons().indexOf(p1.getTruck().getShip()[1][4])).isActive() );

    }

    @Test
    public void testVisitForMeteorSwarm() {
        String username = "Albi";
        int i = 1;
        int j = 4;

        visitor = new ActiveCannonVisitor();

        CardException ex = assertThrows(
                CardException.class,
                () -> visitor.visitForMeteorSwarm(meteorSwarm, username, i, j)
        );

        assertEquals("Cannot activate cannon now: phase is Setup", ex.getMessage());

        game.setGameStatus(GameStatus.INIT_METEORSWARM);
        visitor.visitForMeteorSwarm(meteorSwarm, username, i, j);

        assertEquals(true, p1.getTruck().getCannons().get(p1.getTruck().getCannons().indexOf(p1.getTruck().getShip()[1][4])).isActive() );

    }

    @Test
    public void testVisitForPirates() {
        String username = "Albi";
        int i = 1;
        int j = 4;

        visitor = new ActiveCannonVisitor();

        CardException ex = assertThrows(
                CardException.class,
                () -> visitor.visitForPirates(pirates, username, i, j)
        );

        assertEquals("Cannot activate cannon now: phase is Setup", ex.getMessage());

        game.setGameStatus(GameStatus.INIT_PIRATES);
        visitor.visitForPirates(pirates, username, i, j);

        assertEquals(true, p1.getTruck().getCannons().get(p1.getTruck().getCannons().indexOf(p1.getTruck().getShip()[1][4])).isActive() );

    }

    @Test
    public void testVisitForSlavers() {
        String username = "Albi";
        int i = 1;
        int j = 4;

        visitor = new ActiveCannonVisitor();

        CardException ex = assertThrows(
                CardException.class,
                () -> visitor.visitForSlavers(slavers, username, i, j)
        );

        assertEquals("Cannot activate cannon now: phase is Setup", ex.getMessage());

        game.setGameStatus(GameStatus.INIT_SLAVERS);
        visitor.visitForSlavers(slavers, username, i, j);

        assertEquals(true, p1.getTruck().getCannons().get(p1.getTruck().getCannons().indexOf(p1.getTruck().getShip()[1][4])).isActive() );

    }

    @Test
    void testReturnNullStubs() {
        ActiveCannonVisitor visitor = new ActiveCannonVisitor();
        visitor.visitForAbandonedStation(null, "Fede", 0, 0);
        visitor.visitForPlanets(null, "Fede", 0, 0);
        visitor.visitForOpenSpace(null, "Fede", 0, 0);
    }

}
