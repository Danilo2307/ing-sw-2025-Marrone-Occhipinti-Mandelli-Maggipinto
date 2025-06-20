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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class Test2 {
    //TEST 2 PER IL PRIMO COMPRA, IL SECONDO TENTA, TEST ORDINE SBAGLIATO, REDUCE CHIAMATO DAL SECONDO,
    //RESTA IN ATTESA SE NON VENGONO TOLTE TUTTE LE MERCI, BLOCCA SE HAI INSUFFICENTE CREW
    Game game;
    Player p1, p2;
    AbandonedShip card;

    @BeforeEach
    void setUp() {
        this.game = new Game(2,1);;

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

        card = new AbandonedShip(1,3,4,3,1);
    }

    @Test
    void testBuyAndReduce() throws CardException, InvocationTargetException, IllegalAccessException {
        // INIT
        card.initPlay("Fede");
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
        card.reduceCrew("Albi", 1, 3, 2);
        card.reduceCrew("Albi", 1, 4, 1);

        GameStatus after = game.getGameStatus();
        System.out.println("GameStatus: " + before + " → " + after);

        assertNotEquals(before, after);
    }
}
