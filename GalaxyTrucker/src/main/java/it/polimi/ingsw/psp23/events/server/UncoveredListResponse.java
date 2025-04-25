package it.polimi.ingsw.psp23.events.server;

import it.polimi.ingsw.psp23.events.Event;
import it.polimi.ingsw.psp23.model.components.Component;

import java.util.ArrayList;

public record UncoveredListResponse(ArrayList<Component> uncovered, int lastVersion) implements Event { }
