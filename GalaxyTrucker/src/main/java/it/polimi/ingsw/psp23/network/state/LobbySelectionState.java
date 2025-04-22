package it.polimi.ingsw.psp23.network.state;

import it.polimi.ingsw.psp23.logger.FlightRecorder;
import it.polimi.ingsw.psp23.network.ClientInfo;
import it.polimi.ingsw.psp23.network.messages.fromclient.*;
import it.polimi.ingsw.psp23.network.messages.fromserver.GenericServerMsg;
import it.polimi.ingsw.psp23.network.messages.fromserver.LobbyFullMsg;
import it.polimi.ingsw.psp23.network.messages.fromserver.LobbyJoinOKMsg;

/**
 * State where the client can create or join existing lobbies.
 */
public class LobbySelectionState extends State {

    public LobbySelectionState(ClientInfo user) {
        super(user, StateType.LOBBY_SELECTION);
        FlightRecorder.info("[LobbySelectionState] " + user.getConnectionUUID().substring(0, 3) + " started lobby selection");
    }

    @Override
    public void handleLobbyCreationInfo(CreateLobbyMsg createLobbyRequest) {
        try {
            MatchController.getInstance()
                    .createLobby(createLobbyRequest.getName(), createLobbyRequest.getMaxPlayers(), user);

            user.send(new LobbyJoinOKMsg());
            user.setState(new InLobbyState(user));
        } catch (GameLobbyException gle) {
            switch (gle.getReason()) {
                case INVALID_LOBBY_INFO_FOR_CREATION -> user.send(new InvalidLobbyConfigMsg());
                case LOBBY_ALREADY_FULL_EXCEPTION  -> user.send(new LobbyFullMsg());
                case NO_MORE_SPACE_FOR_NEW_LOBBIES -> user.send(new NoSpaceForLobbiesMsg());
            }
        } catch (MatchControllerException mce) {
            user.send(new ServerErrorMsg());
        }
    }

    @Override
    public void handleLobbyJoinRequest(SelectLobbyMsg joinLobbyRequest) {
        try {
            boolean alreadyStarted = MatchController.getInstance()
                    .joinLobby(joinLobbyRequest.getLobbyId(), user);

            user.send(new LobbyJoinOKMsg());
            if (alreadyStarted) {
                // lobby already started, reconnecting to ongoing match
                user.setState(new ActiveGameState(user));
                user.getGameController().reconnect(user.getUsername());
            } else {
                user.setState(new InsideLobbyState(user));
            }
        } catch (GameLobbyException gle) {
            if (gle.getReason() == GameLobbyException.Reason.LOBBY_ALREADY_FULL_EXCEPTION) {
                user.send(new LobbyFullMsg());
            } else {
                user.send(new GenericServerMsg());
            }
        } catch (MatchControllerException mce) {
            user.send(new GenericServerMsg());
        }
    }

    @Override
    public void handleLobbyListQuery(RequestLobbyListMsg lobbyListRequest) {
        try {
            user.send(new ListLobbyMsg(MatchController.getInstance().getAvailableLobbies()));
        } catch (MatchControllerException mce) {
            user.send(new GenericServerMsg());
        }
    }
}
