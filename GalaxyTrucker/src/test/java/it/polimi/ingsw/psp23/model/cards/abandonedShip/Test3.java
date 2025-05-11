package it.polimi.ingsw.psp23.model.cards.abandonedShip;

import it.polimi.ingsw.psp23.exceptions.CardException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.model.cards.AbandonedShip;
import it.polimi.ingsw.psp23.model.components.AlienAddOns;
import it.polimi.ingsw.psp23.model.components.HousingUnit;
import it.polimi.ingsw.psp23.model.enumeration.Color;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.model.enumeration.Side;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class Test3 {
    //TEST CON GLI ALIENI
    Game game;
    Player p1, p2;
    AbandonedShip card;

    @BeforeEach
    void setUp() {
        this.game = Game.getInstance();

        game.addPlayer("Albi");
        game.addPlayer("Fede");

        p1 = game.getPlayerFromNickname("Albi");
        p2 = game.getPlayerFromNickname("Fede");

        HousingUnit h1 = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true);
        HousingUnit h2 = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true);
        HousingUnit h3 = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, false);
        AlienAddOns a1 = new AlienAddOns(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Color.Purple);
        AlienAddOns a2 = new AlienAddOns(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Color.Brown);
        HousingUnit h4 = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, false);
        HousingUnit h5 = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, false);
        HousingUnit h6 = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, false);
        h5.setAstronaut();
        h6.setAstronaut();
        h1.moveToHand();
        h2.moveToHand();
        h3.moveToHand();
        h4.moveToHand();
        h5.moveToHand();
        h6.moveToHand();
        a1.moveToHand();
        a2.moveToHand();
        // la cabina centrale non richiede adiacenza
        p1.getTruck().addComponent(h1, 2, 3);
        p2.getTruck().addComponent(h2, 2, 3);
        p1.getTruck().addComponent(h3, 1, 3);
        p1.getTruck().addComponent(a1, 1, 4);
        p1.getTruck().addComponent(h4, 2, 2);
        p1.getTruck().addComponent(a2, 1, 2);
        p2.getTruck().addComponent(h5, 1, 3);
        p2.getTruck().addComponent(h6, 1, 4);
        p1.getTruck().updateAllowedAliens();
        h3.setAlien(Color.Purple);
        h4.setAlien(Color.Brown);

        p1.setPosition(12);
        p2.setPosition(10);
        game.sortPlayersByPosition();

        card = new AbandonedShip(1,3,4,3);
    }

    @Test
    void testBuyAndReduce() throws CardException, InvocationTargetException, IllegalAccessException {
        // INIT
        card.initPlay();
        assertEquals(GameStatus.INIT_ABANDONEDSHIP, game.getGameStatus());

        // Albi COMPRA
        card.buyShip("Albi");
        assertEquals(p1.getNickname(), game.getCurrentPlayer().getNickname());

        // Fede compra la nave
        // card.buyShip("Fede");
        // Dopo l’ultimo atterraggio, si applica la penalty e finisce la fase
        assertEquals(GameStatus.END_ABANDONEDSHIP, game.getGameStatus());

        // Verifica che i marker sulla board siano arretrati di 4 spazi
        assertEquals(8, p1.getPosition());
        assertEquals(10, p2.getPosition());

        //Riduzione crew Fede
        GameStatus before = game.getGameStatus();
        card.reduceCrew("Albi", 2, 3, 2);
        card.reduceCrew("Albi", 2, 2, 1);

        GameStatus after = game.getGameStatus();
        System.out.println("GameStatus: " + before + " → " + after);

        assertNotEquals(before, after);
    }
}
