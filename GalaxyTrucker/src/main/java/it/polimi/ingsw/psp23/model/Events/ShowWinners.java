package it.polimi.ingsw.psp23.model.Events;

import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.network.socket.Server;

public class ShowWinners extends Event {
    /// USELESS DA ORA : ELIMINARE
    public ShowWinners(GameStatus newStatus) {
        super(newStatus);
    }

    @Override
    public String describe(int gameId) {
        StringBuilder sb = new StringBuilder();
        sb.append("Il volo è terminato. Ecco i crediti cosmici guadagnati dai giocatori rimasti in partita:\n");
        for(Player player : Server.getInstance().getGame(gameId).getPlayers()) {
            sb.append(player.getNickname()).append(": ").append(player.getMoney());
            if(player.getMoney() > 0){
                sb.append(" -> Ha vinto!").append("\n");
            }
            else{
                sb.append(" -> Ha perso").append("\n");
            }
        }
        sb.append("Ecco i crediti cosmici dei giocatori costretti ad abbandonare:\n");
        for(Player player : Server.getInstance().getGame(gameId).getPlayersNotOnFlight()) {
            sb.append(player.getNickname()).append(": ").append(player.getMoney());
            if(player.getMoney() > 0){
                sb.append(" -> Ha vinto!").append("\n");
            }
            else{
                sb.append(" -> Ha perso").append("\n");
            }
        }
        return sb.toString();
    }

}
