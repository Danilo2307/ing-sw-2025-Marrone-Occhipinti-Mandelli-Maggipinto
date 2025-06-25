package it.polimi.ingsw.psp23.model.components;

import it.polimi.ingsw.psp23.exceptions.InvalidActionException;
import it.polimi.ingsw.psp23.exceptions.InvalidComponentActionException;
import it.polimi.ingsw.psp23.model.enumeration.Color;
import it.polimi.ingsw.psp23.model.enumeration.Side;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HousingUnitTest {

    @Test
    public void testStartingCabinInitialization() {
        HousingUnit cabin = new HousingUnit(Side.EMPTY, Side.EMPTY, Side.EMPTY, Side.EMPTY, true,1);
        // cabina iniziale deve avere 2 astronauti
        assertTrue(cabin.isStartingCabin());
        assertEquals(2, cabin.getNumAstronaut());
        assertNull(cabin.getAlien());
    }

    @Test
    public void testStandardCabinInitialization() {
        HousingUnit cabin = new HousingUnit(Side.EMPTY, Side.EMPTY, Side.EMPTY, Side.EMPTY, false,1);
        // cabina normale deve iniziare vuota
        assertFalse(cabin.isStartingCabin());
        assertEquals(0, cabin.getNumAstronaut());
        assertNull(cabin.getAlien());
    }

    @Test
    public void testReduceHumans() {
        HousingUnit cabin = new HousingUnit(Side.EMPTY, Side.EMPTY, Side.EMPTY, Side.EMPTY, false,1);
        // aggiungo e rimuovo astronauti
        cabin.setAstronaut();
        cabin.reduceOccupants(1);
        assertEquals(1, cabin.getNumAstronaut());

        // provo a rimuovere 2 astronauti avendone solo 1 -> lancio eccezione
        assertThrows(IllegalArgumentException.class, () -> cabin.reduceOccupants(2));
    }

    @Test
    public void testReduceAlien() {
        HousingUnit cabin = new HousingUnit(Side.EMPTY, Side.EMPTY, Side.EMPTY, Side.EMPTY, false,1);
        cabin.addConnectedAddon(Color.Brown);
        // aggiungo alieno
        cabin.setAlien(Color.Brown);
        // avendo l'alieno nella cabin, posso rimuovere solo 1 occupant -> lancio eccezione
        assertThrows(IllegalArgumentException.class, () -> cabin.reduceOccupants(2));
        // check rimozione effettiva
        cabin.reduceOccupants(1);
        assertNull(cabin.getAlien());
    }

    @Test
    public void testExceptionSetAlien() {
        HousingUnit h = new HousingUnit(Side.EMPTY, Side.EMPTY, Side.EMPTY, Side.EMPTY, false,1);
        h.setAstronaut();
        assertThrows(InvalidComponentActionException.class, () -> h.setAlien(Color.Brown));
    }

    @Test
    public void testExceptionSetAstronaut() {
        HousingUnit h = new HousingUnit(Side.EMPTY, Side.EMPTY, Side.EMPTY, Side.EMPTY, false,1);
        h.addConnectedAddon(Color.Brown);
        h.setAlien(Color.Brown);
        assertThrows(InvalidComponentActionException.class, () -> h.setAstronaut());
    }



}
