package it.polimi.ingsw.psp23.model.cards;

import it.polimi.ingsw.psp23.Board;
import it.polimi.ingsw.psp23.Player;
import it.polimi.ingsw.psp23.Utility;
import it.polimi.ingsw.psp23.model.Events.Event;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.model.components.HousingUnit;
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

    public boolean hasEnoughUmansLeft(InputObject input) {
        Board board = Game.getInstance().getCurrentPlayer().getTruck();

        boolean valid = true;

        int i = 0;

        int sum = 0;

        int coordX;

        int coordY;

        int quantity;

        Component analizedComponent;

        int indiceCabina;

        int umaniNecessari = 0;

        // Devo trovare il numero di umani sottratti, senza considerare gli alieni perchè una nave senza alieni può
        // continuare a giocare, mentre una nave senza umani non può giocare
        while(i < input.getLista().size()){

            coordX = input.getLista().get(i)[0];

            coordY = input.getLista().get(i)[1];

            quantity = input.getLista().get(i)[3];

            analizedComponent = board.getTile(coordX, coordY);

            indiceCabina = board.getHousingUnits().indexOf(analizedComponent);

            if(board.getHousingUnits().get(indiceCabina).getAlien() == null){
                umaniNecessari += quantity;
            }
            i++;

        }

        if(umaniNecessari >= board.calculateHumanCrew()){
            valid = false;
        }

        return valid;
    }

    public boolean areCabinSelectionValid(InputObject input){

        Board board = Game.getInstance().getCurrentPlayer().getTruck();

        boolean valid = true;

        int i = 0;

        int sum = 0;

        int coordX;

        int coordY;

        int quantity;

        Component analizedComponent;

        int indiceCabina;

        while(valid && i < input.getLista().size()){

            coordX = input.getLista().get(i)[0];

            coordY = input.getLista().get(i)[1];

            quantity = input.getLista().get(i)[3];

            analizedComponent = board.getTile(coordX, coordY);

            indiceCabina = board.getHousingUnits().indexOf(analizedComponent);

            int numAstronautInCabin = board.getHousingUnits().get(indiceCabina).getNumAstronaut();

            boolean thereIsAlien = false;

            if(board.getHousingUnits().get(indiceCabina).getAlien() != null){
                thereIsAlien = true;
            }



            /*
             * Questo if controlla che la casella sia valida, che questa casella contenga una housing unit e che
             * questa housing unit abbia un numero di astronauti maggiore o uguale a quello che vogliamo togliere
             */
            if(!board.isValid(coordX, coordY) || !board.getHousingUnits().contains(analizedComponent) || numAstronautInCabin < input.getLista().get(i)[3] || (quantity == 1 && numAstronautInCabin == 0 && !thereIsAlien)){
                valid = false;
            }
            sum += quantity;
            i++;
        }

        if(sum != numMembers){
            valid = false;
        }

        return valid;
    }

    public boolean inputValidity(InputObject input) {

        return hasEnoughUmansLeft(input) && areCabinSelectionValid(input);

    }
}