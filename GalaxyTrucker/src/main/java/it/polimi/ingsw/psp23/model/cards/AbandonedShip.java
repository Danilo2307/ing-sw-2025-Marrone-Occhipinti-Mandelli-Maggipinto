package it.polimi.ingsw.psp23.model.cards;

import it.polimi.ingsw.psp23.Board;
import it.polimi.ingsw.psp23.Player;
import it.polimi.ingsw.psp23.Utility;
import it.polimi.ingsw.psp23.model.Events.Event;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.model.components.HousingUnit;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

import java.util.List;

public class AbandonedShip extends Card {
    //Danilo

    private final int days;
    private final int cosmicCredits;
    private final int numMembers;
    private String isSold = null;

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
        Game.getInstance().setGameStatus(GameStatus.INIT_ABANDONEDSHIP);
        Game.getInstance().fireEvent(new Event(Game.getInstance().getGameStatus(),days, cosmicCredits,numMembers));
    }

    public void play(){

        for(Player p : Game.getInstance().getPlayers()){
            if(isSold == null){
                break;
            }
            if(p.getNickname().equals(isSold)){
                Utility.updatePosition(Game.getInstance().getPlayers(), Game.getInstance().getPlayers().indexOf(p),-days);
                p.updateMoney(cosmicCredits);
                break;
            }
        }
        endPlay();
    }


    public void endPlay(){
        Game.getInstance().setGameStatus(GameStatus.END_ABANDONEDSHIP);
    }

    public void setIsSold(String nickname){
        isSold = nickname;
    }

    public String getIsSold(){
        return isSold;
    }

    //TODO: visitor per il setIsSold ed il getIsSold per controllare che il player a cui è stata venduta la nave tolga gli umani



   /* public boolean hasEnoughUmansLeft(InputObject input) {
        Board board = Game.getInstance().getCurrentPlayer().getTruck();

        List<HousingUnit> housingUnits = board.getHousingUnits();

        boolean valid = true;

        int i = 0;

        int sum = 0;

        int coordX;

        int coordY;

        int quantity;

        Component analizedComponent;

        int indiceCabina;

        int umaniNecessari = 0;

        int[] rimozioni = new int[housingUnits.size()];

        // Devo trovare il numero di umani sottratti, senza considerare gli alieni perchè una nave senza alieni può
        // continuare a giocare, mentre una nave senza umani non può giocare
        while(i < input.getLista().size()){

            coordX = input.getLista().get(i)[0];

            coordY = input.getLista().get(i)[1];

            quantity = input.getLista().get(i)[3];

            analizedComponent = board.getTile(coordX, coordY);

            indiceCabina = housingUnits.indexOf(analizedComponent);

            // Se indiceCabina è meno uno vuol dire che non sto analizzando una HousingUnit
            if(indiceCabina == -1){
                return false;
            }

            rimozioni[indiceCabina] += quantity;

            i++;

        }

        int umaniRimossi = 0;

        for (i = 0; i < housingUnits.size(); i++) {
            HousingUnit h = housingUnits.get(i);
            int totOccupanti = h.getNumAstronaut() + (h.getAlien() != null ? 1 : 0);
            int daRimuovere = rimozioni[i];

            // Se voglio togliere più di quanto ho a bordo non posso farlo ed è un errore
            if (daRimuovere > totOccupanti) return false;

            // Calcolo solo umani da rimuovere
            if (h.getAlien() == null) {
                umaniRimossi += daRimuovere;
            } else {
                // Posso togliere solo 1 alieno, altrimenti devono essere tutti umani
                int umaniPresenti = h.getNumAstronaut();
                if (daRimuovere > 1 && daRimuovere > umaniPresenti)
                    return false;
                // In questo caso sto rimuovendo un alieno
                if (daRimuovere == 1 && umaniPresenti == 0)
                    umaniRimossi += 0;
                else
                    umaniRimossi += Math.min(daRimuovere, umaniPresenti);
            }
        }

        return umaniRimossi < board.calculateHumanCrew();

    }*/

    /*public boolean areCabinSelectionValid(InputObject input){

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
        /*    if(!board.isValid(coordX, coordY) || !board.getHousingUnits().contains(analizedComponent) || numAstronautInCabin < input.getLista().get(i)[3] || (quantity == 1 && numAstronautInCabin == 0 && !thereIsAlien)){
                valid = false;
            }
            sum += quantity;
            i++;
        }

        if(sum != numMembers){
            valid = false;
        }

        return valid;
    } */

    /*public boolean inputValidity(InputObject input) {

        if(input == null || input.getLista() == null || input.getLista().isEmpty()) return false;

        return hasEnoughUmansLeft(input) && areCabinSelectionValid(input);

    }*/
}