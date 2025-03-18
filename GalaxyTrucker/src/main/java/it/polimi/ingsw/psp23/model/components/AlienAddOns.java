package it.polimi.ingsw.psp23.model.components;
import it.polimi.ingsw.psp23.model.enumeration.Color;
import it.polimi.ingsw.psp23.model.enumeration.Side;

public class AlienAddOns extends Component {
    //danilo
    private Color c;

    AlienAddOns(Side up, Side down, Side left, Side right, Color c) {
        super(up,down,left,right);
        this.c = c;
    }

    public Color getColor(){ return c; }

}
