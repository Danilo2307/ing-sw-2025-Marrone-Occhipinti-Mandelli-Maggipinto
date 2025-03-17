package it.polimi.ingsw.psp23;

public class Alien_add_ons extends Component{
    //danilo
    private Color c;

    Alien_add_ons(Side up, Side down, Side left, Side right, int x, int y, Color c) {
        super(up,down,left,right,x,y);
        this.c = c;
    }

    public Color getColor(){
        return c;
    }

}
