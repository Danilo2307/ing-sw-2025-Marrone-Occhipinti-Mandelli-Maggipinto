package it.polimi.ingsw.psp23;

public class LifeSupport extends Component{
    //danilo
    private Color c;

    LifeSupport(int up, int down, int left, int right, int x,int y,Color c) {
        super(up);
        super(down);
        super(left);
        super(right);
        super(x);
        super(y);
        this.c = c;
    }

    public getColor(){
        return c;
    }

}
