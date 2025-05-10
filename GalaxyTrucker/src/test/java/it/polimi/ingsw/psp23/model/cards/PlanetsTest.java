package it.polimi.ingsw.psp23.model.cards;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.psp23.exceptions.CardException;
import it.polimi.ingsw.psp23.model.components.Container;
import it.polimi.ingsw.psp23.model.components.HousingUnit;
import it.polimi.ingsw.psp23.model.enumeration.Color;
import it.polimi.ingsw.psp23.model.enumeration.Direction;
import it.polimi.ingsw.psp23.model.enumeration.Side;
import org.junit.jupiter.api.*;

import it.polimi.ingsw.psp23.model.Game.*;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.model.Game.Item;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;
import java.lang.reflect.Field;

class PlanetsTest {

    Game game;
    Player p1, p2;
    Board b1, b2;
    Planets card;
    MeteorSwarm nextCard;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        this.game = Game.getInstance();

        game.addPlayer("Albi");
        game.addPlayer("Fede");

        p1 = game.getPlayerFromNickname("Albi");
        p2 = game.getPlayerFromNickname("Fede");

        HousingUnit h1 = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true);
        HousingUnit h2 = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true);
        Container c1 = new Container(Side.EMPTY, Side.EMPTY, Side.EMPTY, Side.EMPTY, 6, Color.Red, new ArrayList<>());
        Container c2 = new Container(Side.EMPTY, Side.EMPTY, Side.EMPTY, Side.EMPTY, 6, Color.Blue, new ArrayList<>());
        Container c3 = new Container(Side.EMPTY, Side.EMPTY, Side.EMPTY, Side.EMPTY, 6, Color.Red, new ArrayList<>());
        Container c4 = new Container(Side.EMPTY, Side.EMPTY, Side.EMPTY, Side.EMPTY, 6, Color.Blue, new ArrayList<>());
        h1.moveToHand();
        h2.moveToHand();
        c1.moveToHand();
        c2.moveToHand();
        c3.moveToHand();
        c4.moveToHand();
        // la cabina centrale non richiede adiacenza
        p1.getTruck().addComponent(h1, 2, 3);
        p2.getTruck().addComponent(h2, 2, 3);
        p1.getTruck().addComponent(c1, 1, 3);
        p1.getTruck().addComponent(c2, 1, 4);
        p2.getTruck().addComponent(c3, 1, 3);
        p2.getTruck().addComponent(c4, 1, 4);

        p1.setPosition(12);
        p2.setPosition(10);
        game.sortPlayersByPosition();

        card = new Planets(2,4, List.of(List.of(new Item(Color.Red), new Item(Color.Red), new Item(Color.Red), new Item(Color.Yellow)), List.of(new Item(Color.Red),new Item(Color.Red), new Item(Color.Green), new Item(Color.Green)), List.of(new Item(Color.Red), new Item(Color.Blue), new Item(Color.Blue), new Item(Color.Blue), new Item(Color.Blue))));
        nextCard = new MeteorSwarm(2, List.of(new Meteor(false, Direction.UP), new Meteor(false, Direction.UP), new Meteor(true, Direction.LEFT), new Meteor(false, Direction.LEFT), new Meteor(false, Direction.LEFT)));
    }

    @Test
    void testLandingAndLoading() throws CardException, InvocationTargetException, IllegalAccessException {
        // INIT
        card.initPlay();
        assertEquals(GameStatus.INIT_PLANETS, game.getGameStatus());

        // Albi atterra sul pianeta 1
        card.landOnPlanet("Albi", 1);
        assertEquals("Albi", card.getPlanetsOccupied()[0]);
        assertEquals(p2.getNickname(), game.getCurrentPlayer().getNickname());
        System.out.println("Giocatori in Game: " + Game.getInstance().getPlayers()
                .stream().map(Player::getNickname).collect(Collectors.toList()));
        // Fede atterra sul pianeta 2
        card.landOnPlanet("Fede", 2);
        assertEquals("Fede", card.getPlanetsOccupied()[1]);
        // Dopo l’ultimo atterraggio, si applica la penalty e finisce la fase
        assertEquals(GameStatus.END_PLANETS, game.getGameStatus());

        // Verifica che i marker sulla board siano arretrati di 4 spazi
        assertEquals(8, p1.getPosition());
        assertEquals(6, p2.getPosition());

        // Ora prepariamo le board per il loading:
        // dobbiamo avere dei Container in posizioni note (i,j).

        // Albi carica item
        card.loadGoods("Albi", 7, 6);
        card.loadGoods("Albi", 7, 6);
        card.loadGoods("Albi", 7, 6);
        card.loadGoods("Albi", 7, 8);

        // Fede carica item
        card.loadGoods("Fede", 7, 6);
        card.loadGoods("Fede", 7, 6);
        card.loadGoods("Fede", 7, 8);
        GameStatus before = game.getGameStatus();
        card.loadGoods("Fede", 7, 8);

        GameStatus after = game.getGameStatus();
        System.out.println("GameStatus: " + before + " → " + after);

        assertNotEquals(before, after);

    }
}
