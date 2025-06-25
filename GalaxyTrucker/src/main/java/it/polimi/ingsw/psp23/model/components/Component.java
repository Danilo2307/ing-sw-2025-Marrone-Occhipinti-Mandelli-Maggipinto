package it.polimi.ingsw.psp23.model.components;
import it.polimi.ingsw.psp23.exceptions.ComponentStateException;
import it.polimi.ingsw.psp23.model.enumeration.ComponentLocation;
import it.polimi.ingsw.psp23.model.enumeration.Side;

import java.io.Serializable;

/**
 * Represents a generic component in a system with spatial, state, and orientation-based properties.
 *
 * This class serves as a base for all specific component types that extend its functionality.
 * It defines the general behavior and properties shared by all components,
 * including state management, spatial positioning, and orientation.
 *
 * The class is designed as a sealed class, restricting the number of direct subclasses
 * to a predetermined set. These subclasses represent specific component types.
 */
public sealed class Component implements Serializable permits AlienAddOns, BatteryHub, Cannon, Container, Engine, HousingUnit, Shield, StructuralComponent{

    private ComponentLocation state;
    private Side up;
    private Side down;
    private Side left;
    private Side right;
    private int x;
    private int y;
    private final int id;
    private int rotate;

    // lo chiamo sempre tramite super(...) quando istanzio le sottoclassi
    public Component(Side up, Side down, Side left, Side right, int id) {
        state = ComponentLocation.PILE;  // inizialmente stanno tutti nel mucchio a faccia in giù
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
        this.x = -1;   // assumeranno valori positivi solo se e quando Component passerà in status ON_TRUCK
        this.y = -1;
        this.id = id;
        this.rotate = 0;
    }

    public int getRotate() {
        return this.rotate;
    }

    /**
     * Moves the component to the player's hand if it is in a valid state.
     *
     * This method transitions the component's state to IN_HAND if it is currently
     * in one of the following valid states:
     * - PILE: The component is in the pile.
     * - FACE_UP: The component is discarded and face up.
     * - RESERVED: The component is reserved and not in active play.
     *
     * If the component is not in one of these valid states, a ComponentStateException
     * is thrown to indicate an invalid operation.
     *
     * @throws ComponentStateException if the component is not in a valid state for
     * moving to the player's hand.
     */
    // player prende in mano il component
    public void moveToHand() {
        if (state == ComponentLocation.PILE || state == ComponentLocation.FACE_UP || state == ComponentLocation.RESERVED)
            this.state = ComponentLocation.IN_HAND;
        else
            throw new ComponentStateException("Puoi prendere in mano il component solo se è nel mucchio o scoperto. Metodo moveToHand in Component");
    }

    /**
     * Places the component on the truck if it is in a valid state or is the starting cabin.
     *
     * This method transitions the component's state to ON_TRUCK if it meets one of the following
     * conditions:
     * - The component's current state is IN_HAND.
     * - The component is identified as the starting cabin by the isStartingCabin method.
     *
     * If the component does not meet these conditions, a ComponentStateException is thrown to
     * indicate that the operation is not allowed in its current state.
     *
     * @throws ComponentStateException if the component is not in a valid state to be placed on the truck.
     */
    public void placeOnTruck() {
        if (state == ComponentLocation.IN_HAND || this.isStartingCabin())
            this.state = ComponentLocation.ON_TRUCK;
        else
            throw new ComponentStateException("Puoi saldare il component sulla nave solo se lo hai in mano. Metodo placeOnTruck in Component");
    }

    /**
     * Discards the component to the face-up state if it is currently in the player's hand.
     *
     * This method transitions the component's state from IN_HAND to FACE_UP.
     * It ensures that only components currently held by the player can be discarded.
     *
     * If the component is not in the IN_HAND state at the time of invocation,
     * a ComponentStateException is thrown to indicate that the operation is invalid.
     *
     * @throws ComponentStateException if the component is not in the IN_HAND state.
     */
    public void discardFaceUp() {
        if (state == ComponentLocation.IN_HAND)
            this.state = ComponentLocation.FACE_UP;
        else
            throw new ComponentStateException("Puoi scartare il component solo se lo hai in mano. Metodo discardFaceUp in Component");
    }

    /**
     * Reserves the component if it is currently in the player's hand.
     *
     * This method transitions the component's state from IN_HAND to RESERVED, marking
     * it as reserved and no longer available for active play. It validates that the
     * current state of the component is IN_HAND before allowing the state change.
     *
     * If the component is not in the IN_HAND state, a ComponentStateException is thrown
     * to indicate that the operation is invalid.
     *
     * @throws ComponentStateException if the component is not in the IN_HAND state.
     */
    public void reserve() {
        if (state == ComponentLocation.IN_HAND)
            this.state = ComponentLocation.RESERVED;
        else
            throw new ComponentStateException("Puoi prenotare il component solo se lo hai in mano. Metodo reserve in Component");
    }

    /**
     * Rotates the component 90 degrees clockwise.
     *
     * This method updates the state of the component by shifting its side references
     * (up, down, left, right) to simulate a 90-degree clockwise rotation. The rotation
     * is cumulative, and the player can apply this method multiple times to achieve
     * greater degrees of rotation (e.g., 180, 270, etc.).
     *
     * The following changes occur during the rotation:
     * - The current `up` side becomes `right`.
     * - The current `right` side becomes `down`.
     * - The current `down` side becomes `left`.
     * - The current `left` side becomes `up`.
     *
     * Additionally, the `rotate` field is incremented by 90 to reflect the new rotation state.
     */
    public void rotate() {
        Side support = this.up;
        this.up = this.left;
        this.left = this.down;
        this.down = this.right;
        this.right = support;
        rotate = rotate + 90;
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

    /**
     * Determines whether the component represents the starting cabin.
     *
     * This method checks if the current component is designated
     * as the initial cabin required for the setup or core functionality
     * of the system. It provides a boolean value indicating this status.
     *
     * @return true if the component is the starting cabin, false otherwise.
     */
    public boolean isStartingCabin() {
        return false;
    }

    public String toSymbol() {
        return null;
    }

    public String getInfo() {
        return null;
    }

    public int getId() {
        return id;
    }
}
