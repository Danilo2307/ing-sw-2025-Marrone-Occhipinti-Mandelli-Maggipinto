package it.polimi.ingsw.psp23.model.components;

import it.polimi.ingsw.psp23.exceptions.ContainerException;
import it.polimi.ingsw.psp23.model.Game.Item;
import it.polimi.ingsw.psp23.model.enumeration.Color;
import it.polimi.ingsw.psp23.model.enumeration.Side;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ContainerTest {

    @Test
    public void testLoadItem() {
        // Creo un container rosso: può contenere solo merci rosse o verdi
        Container c = new Container(Side.EMPTY, Side.EMPTY, Side.EMPTY, Side.EMPTY, 2, Color.Red, new ArrayList<>(),1);

        // Caso 1: carico merce verde -> deve essere accettata
        Item itemGreen = new Item(Color.Green);
        c.loadItem(itemGreen);
        assertEquals(1, c.getItems().size());
        assertEquals(itemGreen, c.getItems().get(0));

        // Caso 2: provo a caricare merce blu -> deve lanciare eccezione (colore non ammesso)
        Item itemBlue = new Item(Color.Blue);
        assertThrows(ContainerException.class, () -> c.loadItem(itemBlue));

        // Caso 3: carico merce rossa -> deve essere accettata
        Item itemRed = new Item(Color.Red);
        c.loadItem(itemRed);
        assertEquals(2, c.getItems().size());
        assertEquals(itemRed, c.getItems().get(1));

        // Caso 4: container pieno -> ogni ulteriore caricamento deve fallire
        Item itemExtra = new Item(Color.Red);
        assertThrows(ContainerException.class, () -> c.loadItem(itemExtra));
    }

    @Test
    public void testLoseItem() {
        Container c = new Container(Side.EMPTY, Side.EMPTY, Side.EMPTY, Side.EMPTY, 3, Color.Blue, new ArrayList<>(),1);

        // caso 1: container vuoto -> non posso rimuovere nulla
        Item itemGreen = new Item(Color.Green);
        assertThrows(ContainerException.class, () -> c.loseItem(itemGreen));

        // caso 2: controllo rimozione effettiva
        Item itemBlue = new Item(Color.Blue);
        Item itemYellow = new Item(Color.Yellow);
        c.loadItem(itemGreen);
        c.loadItem(itemBlue);
        c.loadItem(itemYellow);
        c.loseItem(itemBlue);
        assertFalse(c.getItems().contains(itemBlue));
        assertEquals(2, c.getItems().size());

        // caso 3: controllo rimozione: itemYellow2 non è presente ma ho fatto override di equals
        // quindi dovrebbe rimuovere il primo giallo che incontra (cioè itemYellow)
        Item itemYellow2 = new Item(Color.Yellow);
        c.loseItem(itemYellow2);
        assertFalse(c.getItems().contains(itemYellow));
        assertEquals(1, c.getItems().size());

        // caso 4: c contiene solo itemGreen -> se si cerca di rimuovere uno yellow devo lanciare eccezione
        assertThrows(ContainerException.class, () -> c.loseItem(itemYellow2));
    }
}
