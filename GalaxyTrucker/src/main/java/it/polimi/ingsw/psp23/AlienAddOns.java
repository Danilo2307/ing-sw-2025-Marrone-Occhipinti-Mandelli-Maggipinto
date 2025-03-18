package it.polimi.ingsw.psp23;

public class AlienAddOns extends Component{
    //danilo
    private Color c;

    AlienAddOns(Side up, Side down, Side left, Side right, Color c) {
        super(up,down,left,right);
        this.c = c;
    }

    public Color getColor(){ return c; }

}
