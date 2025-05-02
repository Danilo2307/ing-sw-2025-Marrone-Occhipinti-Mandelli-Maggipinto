package it.polimi.ingsw.psp23.model.cards;

import it.polimi.ingsw.psp23.Board;
import it.polimi.ingsw.psp23.Player;
import it.polimi.ingsw.psp23.Utility;
import it.polimi.ingsw.psp23.exceptions.CardException;
import it.polimi.ingsw.psp23.model.Events.Event;
import it.polimi.ingsw.psp23.model.Events.EventForPirates;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.components.Cannon;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

import java.util.ArrayList;
import java.util.List;

public class Pirates extends Card {
    // Danilo
    private final int prize;
    private final int days;
    private final int firepower;
    private final List <CannonShot> cannonShot;
    private Game game;
    List<Player> losers;

    public Pirates(int level, int prize, int days, int firepower, List <CannonShot> cannonShot) {
        super(level);
        this.prize = prize;
        this.days = days;
        this.firepower = firepower;
        this.cannonShot = cannonShot;
        this.losers = new ArrayList<>();
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

    public void initPlay(){
        game = Game.getInstance();
        game.setGameStatus(GameStatus.INIT_PIRATES);
        game.getInstance().fireEvent(new EventForPirates(Game.getInstance().getGameStatus(), days, firepower, prize, cannonShot));
    }

    public void fightPirates(String username){
        if (game.getGameStatus() != GameStatus.INIT_PIRATES) {
            throw new CardException("User '" + username + "' cannot fight pirates in phase: " + game.getGameStatus());
        }

        if (!username.equals(game.getPlayers().get(game.getTurn()))) {
            throw new CardException("User '" + username + "' is not the current player");
        }
        double potenzaDiFuocoPlayer = game.getPlayerFromNickname(username).getTruck().calculateCannonStrength();

        // Adesso faccio il confronto tra la potenza di fuoco del player e quella dei pirates per vedere se vince il player
        if(potenzaDiFuocoPlayer > firepower){
                Utility.updatePosition(game.getPlayers(), Game.getInstance().getTurn(), -days);
                game.getPlayerFromNickname(username).updateMoney(prize);
                endPlay();
        }
        else if(potenzaDiFuocoPlayer == firepower){
            // Qui non bisogna fare niente perchè i pirati attaccano i player presenti dopo
        }
        else{
            losers.add(game.getPlayerFromNickname(username));
        }

        if(game.getPlayers().size() == (game.getTurn()+1)){
            endPlay();
        }
    }

    public void endPlay(){
        int impactLine;
        for(CannonShot c : cannonShot) {
            impactLine = Utility.roll2to12();
            for (Player player : losers) {
                player.getTruck().handleCannonShot(c, impactLine);
            }
        }
        game.setGameStatus(GameStatus.Playing);
    }

    public String help() {
        GameStatus status = game.getGameStatus();
        return switch (status) {
            case INIT_PIRATES -> "Available commands: FIGHTPIRATES";
            default -> "No commands available in current phase.";
        };
    }


}
