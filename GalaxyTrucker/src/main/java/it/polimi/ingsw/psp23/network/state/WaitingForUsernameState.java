package it.polimi.ingsw.psp23.network.state;

import it.polimi.ingsw.psp23.network.ClientInfo;
import it.polimi.ingsw.psp23.logger.FlightRecorder;
import it.polimi.ingsw.psp23.network.messages.fromclient.*;
import it.polimi.ingsw.psp23.network.messages.fromserver.*;

/**
 * WaitingForUsernameState is responsible for processing the initial username choice.
 * It validates the username and progresses the user to the next state if successful.
 */
public class WaitingForUsernameState extends State {

    public WaitingForUsernameState(ClientInfo user) {
        super(user, StateType.WAITING_FOR_USERNAME);
        FlightRecorder.info("User " + user.getConnectionUUID().substring(0, 3) + " entered WaitingForUsernameState.");
    }

    /**
     * Processes a username proposal from the client.
     * Responds with confirmation or appropriate error message.
     *
     * @param proposal the proposed username submitted by the client
     */
    @Override
    public void handleUsernameSubmission(SetUsernameMsg proposal) {
        String chosenName = proposal.getUsername();

        if (Profiles.getInstance().isUsernameUnavailable(chosenName)) {
            user.send(new UsernameConflictMsg());
            return;
        }

        try {
            Profiles.getInstance().setUserUsername(user.getConnectionUUID(), chosenName);
            user.send(new UsernameAcceptedMsg());
            user.setState(new ChooseCreateJoinState(user));
        } catch (UserException e) {
            user.send(new InvalidUsernameMsg(e.getMessage()));
        } catch (ProfilesException e) {
            user.send(new GenericServerMsg());
        }
    }
}

