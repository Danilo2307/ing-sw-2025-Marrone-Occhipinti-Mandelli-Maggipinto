package it.polimi.ingsw.psp23;

public class LifeSupport extends Component{
    //danilo
    private Color c;

    LifeSupport(int up, int down, int left, int right, int x,int y,Color c) {
        super(up,down,left,right,x,y);
        this.c = c;
    }

    public getColor(){
        return c;
    }

}
