package it.polimi.ingsw.psp23.events.server;

import it.polimi.ingsw.psp23.events.Event;
import it.polimi.ingsw.psp23.model.components.Component;

public record ShipResponse(Component[][] ship) implements Event { }
