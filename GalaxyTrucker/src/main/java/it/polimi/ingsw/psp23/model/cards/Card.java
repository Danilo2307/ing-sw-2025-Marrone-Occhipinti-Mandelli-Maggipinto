package it.polimi.ingsw.psp23.model.cards;
import it.polimi.ingsw.psp23.Player;

import java.util.List;

public class Card implements CardInterface {
    private int level;
    private boolean turned = false; // quando è false noi vediamo il retro della carta

    Card(int level) {
        this.level = level;
    }

    // we are using a getter because we want to know the level of the card later(for example when we have to create the small decks)
    public int getLevel() {
        return level;
    }

    public boolean isTurned() {
        return turned;
    }

    public void setTurned(boolean updatedFlag) {
        turned = updatedFlag;
    }

    // call sarebbe il metodo preso dall'implementazione di CardInterface, qui scrivo un metodo "inutile"
    // per poi fare override nelle varie sottoclassi
    @Override
    public Void call(Visitor visitor){
        return null;
    }

}