package it.polimi.ingsw.psp23.network.messages.fromclient;

import it.polimi.ingsw.psp23.network.messages.Message;

public final class SetUsernameMsg {
    private final String username;

    public SetUsernameMsg(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }
}
