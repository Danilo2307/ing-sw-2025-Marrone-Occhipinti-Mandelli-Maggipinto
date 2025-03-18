package it.polimi.ingsw.psp23;

import java.util.List;

public class Pirates extends Card{
    // Danilo
    private int prize;
    private int days;
    private int firepower;
    List <CannonShot> cannonShot;

    Pirates(int level, int prize, int days, int firepower, List <CannonShot> cannonShot) {
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
        return cannonShot;
    }

    @Override
    public void play(List<PLayer> players){
        Boolean takePrize = false;
        int size = players.size();
        int i = 0;
        int impactLine;
        for(i=0;i<size;i++) {
            if (players.get(i).getTruck().getCannonStrength() > firepower) {
                //qui il giocatore deve scegliere se prendere il premio e perdere i giorni
                //oppure se sconfiggerli e rimanere dov'è
                //decisione del player se sconfiggerli e prendere il premio o soltanto resistere all'attacco

                if(!takePrize) {
                    takePrize = false; //inizializzo a caso, questa sarà la decisione del player
                    if(takePrize) {
                        players.get(i).updateMoney(prize);
                        players.get(i).updatePosition(-days);
                    }
                    takePrize = true; //qui faccio in modo che nessuno potrà poi riscuotere il premio
                }

            }else if(players.get(i).getTruck().getCannonStrength() < firepower){
                for(Cannonshot c : cannonShot) {
                    impactLine = DiceUtility.roll2to12();
                    players.get(i).getTruck().handleCannonShot(c, impactLine);

                }
            }else{

            }
        }
    }
}
