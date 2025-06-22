package it.polimi.ingsw.psp23.model.Game;

import it.polimi.ingsw.psp23.model.enumeration.Color;

/**
 * Represents an item characterized by a specific color.
 * The color of the item is immutable and defined during object construction.
 */
public class Item {
    private final Color color;

    public Item(Color itemcolor) {
        this.color = itemcolor;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        return color.toString();
    }

    // necessario perchè in Container uso remove; rimuove il primo Item equals all'item in ingresso
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // stesso riferimento

        // cast diretto; assumo che 'o' sia sempre un Item (infatti goods è list<Item>
        Item other = (Item) o;
        return color == other.getColor();
    }
}
