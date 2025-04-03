package it.polimi.ingsw.psp23.model.components;
import it.polimi.ingsw.psp23.exceptions.ComponentStateException;
import it.polimi.ingsw.psp23.model.enumeration.ComponentLocation;
import it.polimi.ingsw.psp23.model.enumeration.Side;
import it.polimi.ingsw.psp23.model.enumeration.ComponentType;

/* @author Federico */
public class Component {

    private final ComponentType type;
    private ComponentLocation state;
    private Side up;
    private Side down;
    private Side left;
    private Side right;
    private int x;
    private int y;

    // lo chiamo sempre tramite super(...) quando istanzio le sottoclassi
    public Component(ComponentType type, Side up, Side down, Side left, Side right) {
        this.type = type;
        state = ComponentLocation.PILE;  // inizialmente stanno tutti nel mucchio a faccia in giù
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
        this.x = -1;   // assumeranno valori positivi solo se e quando Component passerà in status ON_TRUCK
        this.y = -1;
    }

    // player prende in mano il component
    public void moveToHand() {
        if (state == ComponentLocation.PILE || state == ComponentLocation.FACE_UP)
            this.state = ComponentLocation.IN_HAND;
        else
            throw new ComponentStateException("Puoi prendere in mano il component solo se è nel mucchio o scoperto. Metodo moveToHand in Component");
    }

    /* @param x,y coordinate da piazzare on truck */
    public void placeOnTruck() {
        if (state == ComponentLocation.IN_HAND)
            this.state = ComponentLocation.ON_TRUCK;
        else
            throw new ComponentStateException("Puoi saldare il component sulla nave solo se lo hai in mano. Metodo placeOnTruck in Component");
    }

    public void discardFaceUp() {
        if (state == ComponentLocation.IN_HAND)
            this.state = ComponentLocation.FACE_UP;
        else
            throw new ComponentStateException("Puoi scartare il component solo se lo hai in mano. Metodo discardFaceUp in Component");
    }

    // rotazione di 90 gradi verso destra. Se player vuole ruotarlo di più si applica più volte il metodo
    public void rotate() {
        Side support = this.up;
        this.up = this.left;
        this.left = this.down;
        this.down = this.right;
        this.right = support;
    }

    public ComponentLocation getState() {
        return state;
    }

    // get coordinates component
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    // set coordinates component: necessario quando salderò il component sulla nave
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }

    public ComponentType getType() {
        return type;
    }

    public Side getUp() {
        return up;
    }
    public Side getDown() {
        return down;
    }
    public Side getLeft() {
        return left;
    }
    public Side getRight() {
        return right;
    }
}
