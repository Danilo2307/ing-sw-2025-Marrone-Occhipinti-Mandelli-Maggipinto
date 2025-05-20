package it.polimi.ingsw.psp23.model.cards.abandonedStation;

import it.polimi.ingsw.psp23.exceptions.CardException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.Game.Item;
import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.model.cards.AbandonedShip;
import it.polimi.ingsw.psp23.model.cards.AbandonedStation;
import it.polimi.ingsw.psp23.model.components.Container;
import it.polimi.ingsw.psp23.model.components.HousingUnit;
import it.polimi.ingsw.psp23.model.enumeration.Color;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.model.enumeration.Side;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class Test1 {
    //TEST COMPLETO (MOLTE FUNZIONALITA' GIA' TESTATE IN ABANDONEDSHIP)
    Game game;
    Player p1, p2;
    AbandonedStation card;

    @BeforeEach
    void setUp() {
        this.game = Game.getInstance(2);

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
        // INIT
        card.initPlay();
        card.help();
        assertEquals(GameStatus.INIT_ABANDONEDSTATION, game.getGameStatus());

        // Albi passa
        card.pass("Albi");
        assertEquals(p2.getNickname(), game.getCurrentPlayer().getNickname());

        // Fede attracca
//        card.pass("Fede");
        card.dockStation("Fede");
        assertEquals(GameStatus.END_ABANDONEDSTATION, game.getGameStatus());

        assertEquals(12, p1.getPosition());
        assertEquals(9, p2.getPosition());

        //Caricamento
        GameStatus before = game.getGameStatus();
        card.loadGoods("Fede", 1, 5);
        card.pass("Fede");

        GameStatus after = game.getGameStatus();
        System.out.println("GameStatus: " + before + " → " + after);

        assertNotEquals(before, after);

    }
}
