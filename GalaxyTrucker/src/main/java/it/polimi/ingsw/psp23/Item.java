package it.polimi.ingsw.psp23;

import it.polimi.ingsw.psp23.model.enumeration.Color;

public class Item {
    private Color itemcolor;

    Item(Color itemcolor) {
        this.itemcolor = itemcolor;
    }

    public Color getItemColor() {
        return itemcolor;
    }
}
