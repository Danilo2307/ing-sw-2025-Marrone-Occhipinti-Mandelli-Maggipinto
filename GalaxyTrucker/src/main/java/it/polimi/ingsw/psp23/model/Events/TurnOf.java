// Segnale usato quando bisogna notificare il cambiamento dei turni per carte come ad esempio Smugglers, Pirates etc...

package it.polimi.ingsw.psp23.model.Events;

import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

public class TurnOf extends Event {

    String nickname;

    public TurnOf(GameStatus newStatus, String nickname) {
        super(newStatus);
        this.nickname = nickname;
    }

    @Override
    public String describe(int gameId) {
        StringBuilder sb = new StringBuilder();
        sb.append("È il turno di ").append(nickname).append("\n");
        return sb.toString();
    }


}
