package it.polimi.ingsw.psp23.model.components;

import it.polimi.ingsw.psp23.exceptions.BatteryOperationException;
import it.polimi.ingsw.psp23.model.enumeration.Side;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BatteryHubTest {

    @Test
    public void testReduceBatteries() {
        BatteryHub b = new BatteryHub(Side.EMPTY, Side.EMPTY, Side.EMPTY, Side.EMPTY, 3,1);

        // caso 1: verifico effettiva riduzione da 3 a 1
        b.removeBatteries(2);
        assertEquals(b.getNumBatteries(), 1);

        // caso 2: provo a rimuovere 2 batterie da un batteryHub che ne contiene solo 1 -> lancio eccezione
        assertThrows(IllegalArgumentException.class, () -> b.removeBatteries(2));

        // caso 3: numero di batterie da rimuovere negativo -> lancio eccezione
        assertThrows(IllegalArgumentException.class, () -> b.removeBatteries(-1));
    }
}
