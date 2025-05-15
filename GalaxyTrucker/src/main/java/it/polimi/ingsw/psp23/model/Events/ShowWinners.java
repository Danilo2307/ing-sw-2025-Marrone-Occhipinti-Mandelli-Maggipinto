package it.polimi.ingsw.psp23.model.Events;

import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

public class ShowWinners extends Event {

    public ShowWinners(GameStatus newStatus) {
        super(newStatus);
    }

    @Override
    public String describe() {
        StringBuilder sb = new StringBuilder();
        sb.append("Il volo è terminato. Ecco i crediti cosmici guadagnati:\n");
        for(Player player : Game.getInstance().getPlayers()) {
            sb.append(player.getNickname()).append(": ").append(player.getMoney());
            if(player.getMoney() > 0){
                sb.append(" -> Ha vinto!").append("\n");
            }
            else{
                sb.append(" -> Ha perso");
            }
        }
        return sb.toString();
    }

}
