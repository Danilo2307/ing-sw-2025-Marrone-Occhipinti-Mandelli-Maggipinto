package it.polimi.ingsw.psp23.network.state;

import it.polimi.ingsw.psp23.logger.FlightRecorder;
import it.polimi.ingsw.psp23.network.ClientInfo;
import it.polimi.ingsw.psp23.network.messages.fromclient.ExitLobbyMsg;
import it.polimi.ingsw.psp23.network.messages.fromclient.InitiateLobbyMsg;
import it.polimi.ingsw.psp23.network.messages.fromclient.LobbyDetailsMsg;
import it.polimi.ingsw.psp23.network.messages.fromserver.ExitLobbyOkMsg;
import it.polimi.ingsw.psp23.network.messages.fromserver.GenericServerMsg;
import it.polimi.ingsw.psp23.network.messages.fromserver.InitiateLobbyKoMsg;
import it.polimi.ingsw.psp23.network.messages.fromserver.InitiateLobbyOkMsg;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents the state of a client inside a lobby,
 * before the match has started.
 */
public class InsideLobbyState extends State {

    public InsideLobbyState(ClientInfo user) {
        super(user, StateType.INSIDE_LOBBY);
        FlightRecorder.info("Client [" + user.getUsername() + "] transitioned to InsideLobbyState");
    }

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

    @Override
    public void handleStartLobbyCommand(InitiateLobbyMsg request) {
        // Centralized handling of controller actions with known error reasons
        executeControllerAction(
                () -> MatchController.getInstance().startLobby(user.getUsername()),
                () -> {
                    user.send(new InitiateLobbyOkMsg());
                    user.setState(new InGameState(user));
                },
                Arrays.asList(
                        MatchControllerException.Reason.NO_MORE_SPACE_FOR_NEW_MATCHES,
                        MatchControllerException.Reason.START_ATTEMPT_FROM_NON_MASTER,
                        MatchControllerException.Reason.BAD_STATE_FOR_START
                ),
                () -> user.send(new InitiateLobbyKoMsg())
        );
    }

    @Override
    public void handleLobbyExit(ExitLobbyMsg request) {
        try {
            MatchController.getInstance().exitLobby(user);
            user.send(new ExitLobbyOkMsg());

            user.setGameController(null);

            user.setState(new ChooseCreateJoinState(user));
        } catch (MatchControllerException e) {
            user.send(new GenericServerMsg());
        }
    }

    /**
     * Helper to centralize handling of MatchController calls:
     * - action: the controller operation
     * - onSuccess: what to do if action succeeds
     * - knownErrors: list of exception reasons to handle with onKnownError
     * - onKnownError: action to perform for known errors
     */
    private void executeControllerAction(
            Runnable action,
            Runnable onSuccess,
            List<MatchControllerException.Reason> knownErrors,
            Runnable onKnownError
    ) {
        try {
            action.run();
            onSuccess.run();
        } catch (MatchControllerException e) {
            if (knownErrors.contains(e.getReason())) {
                onKnownError.run();
            } else {
                user.send(new GenericServerMsg());
            }
        }
    }
}
