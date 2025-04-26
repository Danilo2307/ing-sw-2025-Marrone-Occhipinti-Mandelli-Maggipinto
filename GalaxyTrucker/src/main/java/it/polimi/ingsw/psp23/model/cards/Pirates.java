package it.polimi.ingsw.psp23.model.cards;

import it.polimi.ingsw.psp23.Board;
import it.polimi.ingsw.psp23.Player;
import it.polimi.ingsw.psp23.Utility;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.components.Cannon;

import java.util.ArrayList;
import java.util.List;

public class Pirates extends Card {
    // Danilo
    private final int prize;
    private final int days;
    private final int firepower;
    private final List <CannonShot> cannonShot;

    public Pirates(int level, int prize, int days, int firepower, List <CannonShot> cannonShot) {
        super(level);
        this.prize = prize;
        this.days = days;
        this.firepower = firepower;
        this.cannonShot = cannonShot;
    }

    public int getPrize() {
        return prize;
    }
    public int getDays() {
        return days;
    }
    public int getFirepower() {
        return firepower;
    }
    public List<CannonShot> getCannonShot() {
        return new ArrayList<>(cannonShot);
    }

    @Override
    public Object call(Visitor visitor){
        return visitor.visitForPirates(this);
    }

    public void play(InputObject input){

        // Devo prima attivare tutti i cannoni che mi sono passati nell'inputObject tramite il metodo activateCannon
        // così che poi il metodo calculateCannonStrength calcoli correttamente la potenza di fuoco
        Player currentPlayer = Game.getInstance().getCurrentPlayer();
        Board board = currentPlayer.getTruck();
        List<Cannon> cannoni = board.getCannons();
        double potenzaDiFuocoPlayer;

        for(Integer[] i : input.getCannons()){
            for(Cannon c : cannoni){
                if(c.getX() == i[0] && c.getY() == i[1]){
                    c.activeCannon();
                    break;
                }
            }
        }
        potenzaDiFuocoPlayer = board.calculateCannonStrength();

        // Adesso faccio il confronto tra la potenza di fuoco del player e quella dei pirates per vedere se vince il player
        if(potenzaDiFuocoPlayer > firepower){
            if(input.getDecision()){
                Utility.updatePosition(Game.getInstance().getPlayers(), Game.getInstance().getCurrentPlayerIndex(), -days);
                currentPlayer.updateMoney(prize);
            }
        }
        else if(potenzaDiFuocoPlayer == firepower){
            // Qui non bisogna fare niente perchè i pirati attaccano i player presenti dopo
        }
        else{
            // In questo caso dovremmo aspettare che tutti i giocatori abbiano affrontato i pirati o che questi ultimi
            // siano stati sconfitti per chiedere al current player di lanciare i dadi e poi fare arrivare le cannonate
            // a tutti gli altri player. Per fare ciò io metterei uno stato in cui si ci arriva o dopo aver sconfitto
            // i nemici o dopo avere perso e, quando tutti i player sono in quello stato allora si comincia a gestire
            // le cannonate
        }
    }

}
