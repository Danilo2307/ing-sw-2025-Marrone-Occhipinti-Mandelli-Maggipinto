package it.polimi.ingsw.psp23.model.components;
import it.polimi.ingsw.psp23.exceptions.ComponentStateException;
import it.polimi.ingsw.psp23.model.enumeration.ComponentLocation;
import it.polimi.ingsw.psp23.model.enumeration.Side;

import java.io.Serializable;

/* @author Federico */
public sealed class Component implements Serializable permits AlienAddOns, BatteryHub, Cannon, Container, Engine, HousingUnit, Shield, StructuralComponent{

    private ComponentLocation state;
    private Side up;
    private Side down;
    private Side left;
    private Side right;
    private int x;
    private int y;

    // lo chiamo sempre tramite super(...) quando istanzio le sottoclassi
    public Component(Side up, Side down, Side left, Side right) {
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
        if (state == ComponentLocation.PILE || state == ComponentLocation.FACE_UP || state == ComponentLocation.RESERVED)
            this.state = ComponentLocation.IN_HAND;
        else
            throw new ComponentStateException("Puoi prendere in mano il component solo se è nel mucchio o scoperto. Metodo moveToHand in Component");
    }

    public void placeOnTruck() {
        if (state == ComponentLocation.IN_HAND || this.isStartingCabin())
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

    public void reserve() {
        if (state == ComponentLocation.IN_HAND)
            this.state = ComponentLocation.RESERVED;
        else
            throw new ComponentStateException("Puoi prenotare il component solo se lo hai in mano. Metodo reserve in Component");
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

    // necessario per gestire l'aggiunta della cabina centrale
    public boolean isStartingCabin() {
        return false;
    }
}
