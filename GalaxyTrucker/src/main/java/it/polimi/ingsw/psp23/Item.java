package it.polimi.ingsw.psp23;

import it.polimi.ingsw.psp23.model.enumeration.Color;

public class Item {
    private Color itemcolor;

    public Item(Color itemcolor) {
        this.itemcolor = itemcolor;
    }

    public Color getItemColor() {
        return itemcolor;
    }

    @Override
    public String toString() {
        return itemcolor.toString();
    }
}
