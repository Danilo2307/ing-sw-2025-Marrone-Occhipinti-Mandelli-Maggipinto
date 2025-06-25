package it.polimi.ingsw.psp23.model.Game;

import it.polimi.ingsw.psp23.exceptions.LevelException;
import it.polimi.ingsw.psp23.exceptions.NoTileException;
import it.polimi.ingsw.psp23.exceptions.PlayerExistsException;
import it.polimi.ingsw.psp23.exceptions.PlayerNotExistsException;
import it.polimi.ingsw.psp23.model.components.Cannon;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.model.components.HousingUnit;
import it.polimi.ingsw.psp23.model.enumeration.ComponentLocation;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.model.enumeration.Side;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    @Test
    public void testAddPlayer() {
        Game game = new Game(2, 1);

        game.addPlayer("fede");
        assertEquals(1,game.getPlayers().size());
        assertEquals("fede",game.getPlayers().get(0).getNickname());

        game.addPlayer("gianlu");
        assertEquals(2,game.getPlayers().size());
        assertEquals("gianlu", game.getPlayers().get(1).getNickname());
    }

    @Test
    public void testGetTileFromHeap() {
        Game game = new Game(2, 1);
        int prevSize = game.getHeap().size();
        Component c = game.getTileFromHeap();
        assertEquals(c.getState(), ComponentLocation.IN_HAND);
        assertEquals(prevSize - 1, game.getHeap().size());
    }

    @Test
    public void releaseTile() {
        Game game = new Game(2, 1);
        Component cannon = new Cannon(Side.EMPTY, Side.EMPTY, Side.EMPTY, Side.GUN, false, 47);
        cannon.moveToHand();
        game.releaseTile(cannon);
        assertEquals(cannon.getState(), ComponentLocation.FACE_UP);
        assertEquals(1, game.getUncovered().size());
        assertTrue(game.getUncovered().contains(cannon));
    }

    @Test
    public void testGetUncoveredTile() {
        Game game = new Game(2, 1);
        Component cannon = new Cannon(Side.EMPTY, Side.EMPTY, Side.EMPTY, Side.GUN, false, 47);
        cannon.moveToHand();
        game.releaseTile(cannon);
        int prevSize = game.getUncovered().size();

        game.getTileUncovered(0,0);
        assertEquals(prevSize - 1, game.getUncovered().size());
        assertFalse(game.getUncovered().contains(cannon));
        assertSame(cannon.getState(), ComponentLocation.IN_HAND);
        assertEquals(1, game.getLastUncoveredVersion());
    }

    @Test
    public void testReservedTiles2() {
        Game game = new Game(2, 1);
        game.addPlayer("fede");
        Player player = game.getPlayers().get(0);

        Component c = player.chooseTileFromHeap();
        player.reserve();
        assertNull(player.getCurrentTileInHand());
        game.checkReservedTiles();
        assertEquals(c.getState(), ComponentLocation.RESERVED);
        assertEquals(1, player.getTruck().getGarbage());
    }

    @Test
    public void testReservedTilesTrial() {
        Game game = new Game(0, 1);
        game.addPlayer("fede");
        Player player = game.getPlayers().get(0);

        assertThrows(LevelException.class, () -> player.reserve());
    }

    @Test
    public void testGetPlayerFromNickname() {
        Game game = new Game(2, 1);
        game.addPlayer("fede");
        Player p = game.getPlayers().get(0);

        assertSame(p, game.getPlayerFromNickname("fede"));
    }

    @Test
    public void testPlayerNotExistsEx() {
        Game game = new Game(2, 1);
        game.addPlayer("fede");
        Player p = game.getPlayers().get(0);

        assertThrows(PlayerNotExistsException.class, () -> game.getPlayerFromNickname("gianlu"));
    }

    @Test
    public void testDecks_Trial_Throws() {
        Game trial = new Game(0, 99);
        trial.addPlayer("x");
        Player tx = trial.getPlayers().get(0);
        trial.setGameStatus(GameStatus.Building);
        assertThrows(LevelException.class, () -> trial.getVisibleDeck1(tx));
        assertThrows(LevelException.class, () -> trial.getVisibleDeck2(tx));
        assertThrows(LevelException.class, () -> trial.getVisibleDeck3(tx));
        assertThrows(LevelException.class, () -> trial.releaseVisibleDeck1(tx));
        assertThrows(LevelException.class, () -> trial.releaseVisibleDeck2(tx));
        assertThrows(LevelException.class, () -> trial.releaseVisibleDeck3(tx));

    }

    @Test
    public void testDeck_WrongOwner() {
        Game game = new Game(2, 1);
        game.addPlayer("fede");
        game.addPlayer("gianlu");
        Player p1 = game.getPlayerFromNickname("fede");
        Player p2 = game.getPlayerFromNickname("gianlu");
        HousingUnit h1 = new HousingUnit(Side.EMPTY, Side.EMPTY, Side.EMPTY, Side.EMPTY, true, 1);
        HousingUnit h2 = new HousingUnit(Side.EMPTY, Side.EMPTY, Side.EMPTY, Side.EMPTY, true, 2);
        p1.getTruck().addComponent(h1, 2, 3);
        p2.getTruck().addComponent(h2, 2, 3);
        game.setGameStatus(GameStatus.Building);

        // aggiungo una tile alla nave se no non posso prenderlo
        p1.chooseTileFromHeap();
        p1.addTile(2,4);
        game.getVisibleDeck1(p1);
        assertEquals(p1.getNickname(), game.getDeck1Owner());

        // deck già preso
        p2.chooseTileFromHeap();
        p2.addTile(2,4);
        assertNull(game.getVisibleDeck1(p2));
    }

    @Test
    public void testDeck1() {
        Game game = new Game(2, 1);
        game.addPlayer("fede");
        game.addPlayer("gianlu");
        Player p1 = game.getPlayerFromNickname("fede");
        Player p2 = game.getPlayerFromNickname("gianlu");
        HousingUnit h1 = new HousingUnit(Side.EMPTY, Side.EMPTY, Side.EMPTY, Side.EMPTY, true, 1);
        HousingUnit h2 = new HousingUnit(Side.EMPTY, Side.EMPTY, Side.EMPTY, Side.EMPTY, true, 2);
        p1.getTruck().addComponent(h1, 2, 3);
        p2.getTruck().addComponent(h2, 2, 3);
        game.setGameStatus(GameStatus.Building);

        // test rilascio e ripresa
        p1.chooseTileFromHeap();
        p1.addTile(2,4);
        game.getVisibleDeck2(p1);
        game.releaseVisibleDeck2(p1);
        assertNull(game.getDeck2Owner());
        p2.chooseTileFromHeap();
        p2.addTile(2,4);
        game.getVisibleDeck2(p2);
        assertEquals(p2.getNickname(), game.getDeck2Owner());

        // rilascio 2 e prendo 3 e rilascio 3
        game.releaseVisibleDeck2(p2);
        assertNull(game.getDeck2Owner());
        game.getVisibleDeck3(p2);
        game.releaseVisibleDeck3(p2);
        assertNull(game.getDeck3Owner());

        assertEquals(3, game.getVisibleDeck1(p1).size());
    }



}
