package it.polimi.ingsw.psp23.model.cards;

import it.polimi.ingsw.psp23.Board;
import it.polimi.ingsw.psp23.Player;
import it.polimi.ingsw.psp23.Utility;
import it.polimi.ingsw.psp23.model.Events.Event;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

public class AbandonedShip extends Card {
    //Danilo

    private final int days;
    private final int cosmicCredits;
    private final int numMembers;
    private boolean isSold = false;

    public AbandonedShip(int level,int days, int cosmicCredits, int numMembers) {
        super(level);
        this.days = days;
        this.cosmicCredits = cosmicCredits;
        this.numMembers = numMembers;
    }

    public int getDays() {
        return days;
    }

    public int getCosmicCredits() {
        return cosmicCredits;
    }

    public int getNumMembers() {
        return numMembers;
    }

    @Override
    public Object call(Visitor visitor){
        return visitor.visitForAbandonedShip(this);
    }

    public void initPlay(){
        Game.getInstance().setGameStatus(GameStatus.BooleanRequest);
        Game.getInstance().fireEvent(new Event(Game.getInstance().getGameStatus(),days, cosmicCredits,numMembers));
    }

    public void play(InputObject input){

        Player player = Game.getInstance().getCurrentPlayer();

        if(isSold)
            throw new RuntimeException("Ship is already sold");
       if(input.getDecision()){
            if(player.getTruck().calculateCrew() > numMembers && inputValidity(input)) {
                isSold = true;

                    //verrà anche deciso da quali hub togliere i membri da eliminare

                    //qui posso o supporre che in input ci siano già le cabine da dove togliere i membri dell'equipaggio
                    //oppure devo inserire un altro stato per chiedere all'utente di rimuovere gli umani dalle cabine desiderate
                    player.updateMoney(cosmicCredits);
                    Utility.updatePosition(Game.getInstance().getPlayers(),Game.getInstance().getPlayers().indexOf(player),-days);

            }else{
                throw new RuntimeException("Not enough members");
            }

    }
    }

    public boolean inputValidity(InputObject input) {
        Board board = Game.getInstance().getCurrentPlayer().getTruck();
        boolean valid = true;
        int i = 0;
        int sum = 0;
        while(valid && i < input.getLista().size()){

            int coordX = input.getLista().get(i)[0];

            int coordY = input.getLista().get(i)[1];

            Component analizedComponent = board.getTile(coordX, coordY);

            if(!board.isValid(coordX, coordY) || !board.getHousingUnits().contains(analizedComponent)){
                valid = false;
            }
            sum += input.getLista().get(i)[3];
            i++;
        }

        if(sum != numMembers){
            valid = false;
        }




        return valid;
    }
}