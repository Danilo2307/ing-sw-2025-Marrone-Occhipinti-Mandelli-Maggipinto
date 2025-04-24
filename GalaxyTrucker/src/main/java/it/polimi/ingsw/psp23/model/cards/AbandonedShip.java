package it.polimi.ingsw.psp23.model.cards;

import it.polimi.ingsw.psp23.Player;
import it.polimi.ingsw.psp23.Utility;
import it.polimi.ingsw.psp23.model.Events.Event;
import it.polimi.ingsw.psp23.model.Game.Game;
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

    public void initPlay(Game game){
        game.setGameStatus(GameStatus.BooleanRequest);
        game.fireEvent(new Event(game.getGameStatus(),days, cosmicCredits,numMembers));
    }

    public void play(Game game, InputObject input){
        Player player = game.getCurrentPlayer();
        if(!player.isInGame())
            throw new RuntimeException("Player is not in a game");
        if(isSold)
            throw new RuntimeException("Ship is already sold");
       if(input.getDecision()){
            if(player.getTruck().calculateCrew() > numMembers) {
                isSold = true;

                    //verrà anche deciso da quali hub togliere i membri da eliminare

                    //qui posso o supporre che in input ci siano già le cabine da dove togliere i membri dell'equipaggio
                    //oppure devo inserire un altro stato per chiedere all'utente di rimuovere gli umani dalle cabine desiderate
                    player.updateMoney(cosmicCredits);
                    Utility.updatePosition(game.getPlayers(),game.getPlayers().indexOf(player),-days);

            }else{
                throw new RuntimeException("Not enough members");
            }

    }
    }
}