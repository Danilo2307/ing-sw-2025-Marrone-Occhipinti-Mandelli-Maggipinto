package it.polimi.ingsw.psp23.model.components;
import it.polimi.ingsw.psp23.model.enumeration.ComponentLocation;
import it.polimi.ingsw.psp23.model.enumeration.Side;

/* @author Federico */
public class Component {

    private String type;
    private ComponentLocation state;
    private Side up;
    private Side down;
    private Side left;
    private Side right;
    private int x;
    private int y;

    public Component(String type, Side up, Side down, Side left, Side right) {
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
        if (state == ComponentLocation.PILE || state == ComponentLocation.FACE_UP) {
            this.state = ComponentLocation.IN_HAND;
        }
        //else: PRINT MESSAGGIO D'ERRORE / ECCEZIONE ????
    }

    // rotazione di 90 gradi verso destra. Se player vuole ruotarlo di più si applica più volte il metodo
    public void rotate() {
        Side support = this.up;
        this.up = this.left;
        this.left = this.down;
        this.down = this.right;
        this.right = support;
    }

    /* @param x,y coordinate da piazzare on truck */
    public void placeOnTruck() {
        if (state == ComponentLocation.IN_HAND) {
            this.state = ComponentLocation.ON_TRUCK;
        }
    }

    public void discardFaceUp() {
        // 1^ condizione è logica. La 2^ è necessaria per quando ne prendo uno scoperto e decido di rimetterlo a posto senza saldarlo
        if (state == ComponentLocation.IN_HAND || state == ComponentLocation.FACE_UP) {
            this.state = ComponentLocation.FACE_UP;
        }
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

    public String getType() {
        return type;
    }
}
