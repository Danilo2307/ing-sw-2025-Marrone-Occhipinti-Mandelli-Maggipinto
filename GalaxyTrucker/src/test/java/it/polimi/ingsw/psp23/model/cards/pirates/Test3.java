package it.polimi.ingsw.psp23.model.cards.pirates;

import it.polimi.ingsw.psp23.exceptions.CardException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.Game.Item;
import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.model.cards.CannonShot;
import it.polimi.ingsw.psp23.model.cards.Pirates;
import it.polimi.ingsw.psp23.model.components.*;
import it.polimi.ingsw.psp23.model.enumeration.Color;
import it.polimi.ingsw.psp23.model.enumeration.Direction;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.model.enumeration.Side;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class Test3 {
    //CARTA LIVELLO 2
    Game game;
    Player p1, p2, p3;
    Pirates card;

    @BeforeEach
    void setUp() {
        this.game = Game.getInstance(2);

        game.addPlayer("Albi");
        game.addPlayer("Fede");
        game.addPlayer("Gigi");

        p1 = game.getPlayerFromNickname("Albi");
        p2 = game.getPlayerFromNickname("Fede");
        p3 = game.getPlayerFromNickname("Gigi");


        //CABINE CENTRALI
        HousingUnit ha1 = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true);
        ha1.moveToHand();
        HousingUnit hf1 = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true);
        hf1.moveToHand();
        HousingUnit hg1 = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true);
        hg1.moveToHand();
        p1.getTruck().addComponent(ha1, 2, 3);
        p2.getTruck().addComponent(hf1, 2, 3);
        p3.getTruck().addComponent(hg1, 2, 3);

        //BOARD ALBI
        Container ca1 = new Container(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, 3, Color.Blue, new ArrayList<>());
        ca1.moveToHand();
        p1.getTruck().addComponent(ca1, 3, 3);
        BatteryHub ba1 = new BatteryHub(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, 2);
        ba1.moveToHand();
        p1.getTruck().addComponent(ba1, 2, 2);
        Cannon caa1 = new Cannon(Side.GUN, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, false);
        caa1.moveToHand();
        p1.getTruck().addComponent(caa1, 1, 3);
        Cannon caa2 = new Cannon(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.GUN, true);
        caa2.moveToHand();
        p1.getTruck().addComponent(caa2, 2, 4);
        Cannon caa3 = new Cannon(Side.GUN, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, false);
        caa3.moveToHand();
        p1.getTruck().addComponent(caa3, 1, 2);
        Cannon caa4 = new Cannon(Side.GUN, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true);
        caa4.moveToHand();
        p1.getTruck().addComponent(caa4, 1, 4);
        Container ca2 = new Container(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, 1, Color.Red, new ArrayList<>());
        ca2.moveToHand();
        p1.getTruck().addComponent(ca2, 3, 4);
        Engine ea1 = new Engine(Side.UNIVERSAL_CONNECTOR, Side.ENGINE, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, false);
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

        //BOARD FEDE
        Cannon caf1 = new Cannon(Side.GUN, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true);
        caf1.moveToHand();
        p2.getTruck().addComponent(caf1, 1, 3);
        BatteryHub bf1 = new BatteryHub(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, 3);
        bf1.moveToHand();
        p2.getTruck().addComponent(bf1, 2, 4);
        Container cf1 = new Container(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, 2, Color.Blue, new ArrayList<>());
        cf1.moveToHand();
        p2.getTruck().addComponent(cf1, 2, 2);
        Engine ef1 = new Engine(Side.UNIVERSAL_CONNECTOR, Side.ENGINE, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, false);
        ef1.moveToHand();
        p2.getTruck().addComponent(ef1, 3, 3);
        Cannon caf2 = new Cannon(Side.GUN, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true);
        caf2.moveToHand();
        p2.getTruck().addComponent(caf2, 1, 4);
        Cannon caf3 = new Cannon(Side.UNIVERSAL_CONNECTOR, Side.GUN, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, false);
        caf3.moveToHand();
        p2.getTruck().addComponent(caf3, 3, 4);
        BatteryHub bf2 = new BatteryHub(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, 2);
        bf2.moveToHand();
        p2.getTruck().addComponent(bf2, 3, 2);

        //BOARD GIGI
        HousingUnit hg2 = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, false);
        hg2.moveToHand();
        p3.getTruck().addComponent(hg2, 3, 3);
        BatteryHub bg1 = new BatteryHub(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, 3);
        bg1.moveToHand();
        p3.getTruck().addComponent(bg1, 2, 2);
        Container cg1 = new Container(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, 2, Color.Red, new ArrayList<>());
        cg1.moveToHand();
        p3.getTruck().addComponent(cg1, 2, 4);
        Cannon cag1 = new Cannon(Side.GUN, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true);
        cag1.moveToHand();
        p3.getTruck().addComponent(cag1, 1, 3);
        Cannon cag2 = new Cannon(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.GUN, Side.UNIVERSAL_CONNECTOR, false);
        cag2.moveToHand();
        p3.getTruck().addComponent(cag2, 1, 2);
        Cannon cag3 = new Cannon(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.GUN, false);
        cag3.moveToHand();
        p3.getTruck().addComponent(cag3, 1, 4);
        Engine eg1 = new Engine(Side.UNIVERSAL_CONNECTOR, Side.ENGINE, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, false);
        eg1.moveToHand();
        p3.getTruck().addComponent(eg1, 3, 2);
        Item ig1 = new Item(Color.Blue);
        cg1.loadItem(ig1);

        //SET POS INIZIALI
        p1.setPosition(12);
        p2.setPosition(10);
        p3.setPosition(8);
        game.sortPlayersByPosition();

        card = new Pirates(2, 5, 1, 4, List.of(new CannonShot(true, Direction.UP), new CannonShot(false, Direction.UP), new CannonShot(true, Direction.UP)));
    }

    @Test
    void testPirates() throws CardException, InvocationTargetException, IllegalAccessException {
        // INIT
        card.initPlay();
        assertEquals(GameStatus.INIT_PIRATES, game.getGameStatus());

        // Albi attiva un cannone doppio e raggiunge la potenza di fuoco minima
        card.activeCannon("Albi", 1, 4);
//        assertEquals(4, p1.getTruck().calculateCannonStrength());
//        assertEquals(1, p1.getTruck().calculateEngineStrength());
        card.ready("Albi");
        assertEquals(p2.getNickname(), game.getCurrentPlayer().getNickname());

        // Fede viene sconfitto
        assertEquals(0.5 , p2.getTruck().calculateCannonStrength());
        card.ready("Fede");
        assertEquals(GameStatus.INIT_PIRATES, game.getGameStatus());

        //Gigi attiva tutto ma perde
        card.activeCannon("Gigi", 1, 3);
        card.ready("Gigi");
        assertEquals(GameStatus.END_PIRATES, game.getGameStatus());

        assertEquals(12, p1.getPosition());
        assertEquals(10, p2.getPosition());
        assertEquals(8, p3.getPosition());

        card.ready("Gigi");
        assertEquals(GameStatus.END_PIRATES, game.getGameStatus());
        GameStatus before = game.getGameStatus();
        card.ready("Fede");

        GameStatus after = game.getGameStatus();
        System.out.println("GameStatus: " + before + " → " + after);

        //PASSA ALLA CARTA SUCCESSIVA
        assertNotEquals(before, after);

    }
}
