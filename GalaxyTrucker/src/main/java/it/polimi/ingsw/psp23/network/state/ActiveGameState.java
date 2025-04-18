package it.polimi.ingsw.psp23.network.state;

import it.polimi.ingsw.psp23.logger.FlightRecorder;
import it.polimi.ingsw.psp23.network.ClientInfo;
import it.polimi.ingsw.psp23.network.messages.fromclient.ExitLobbyMsg;
import it.polimi.ingsw.psp23.network.messages.fromclient.LobbyDetailsMsg;
import it.polimi.ingsw.psp23.network.messages.fromserver.ExitLobbyOkMsg;
import it.polimi.ingsw.psp23.network.messages.fromserver.GenericServerMsg;

/**
 * Represents the state when a player is actively in a match.
 */
public class ActiveGameState extends State {
    public ActiveGameState(ClientInfo client) {
        super(client, StateType.ACTIVE_GAME);
        FlightRecorder.info("Client '" + client.getUsername() + "' entered ActiveGameState");
    }

    /**
     * Handles client's request for current lobby details.
     * Replies with LobbyInfoMessage or UnknownErrorMessage.
     */
    @Override
    public void handleLobbyDetailsRequest(LobbyDetailsMsg request) {
        try {
            LobbyInfo info = MatchController.getInstance()
                    .getLobbyInfoByPlayerUsername(user.getUsername());
            user.send(new LobbyDetailsMsg(info, user.getUsername()));
        } catch (MatchControllerException e) {
            user.send(new GenericServerMsg());
        }
    }

    /**
     * Handles client's exit-from-match request.
     * On success, notifies client and reverts to lobby-choosing state.
     */
    @Override
    public void handleLobbyExit(ExitLobbyMsg request) {
        try {
            MatchController.getInstance().exitLobby(user);
            user.send(new ExitLobbyOkMsg());

            user.setGameController(null);

            user.setState(new LobbySelectionState(user));
        } catch (MatchControllerException e) {
            user.send(new GenericServerMsg());
        }
    }
}
