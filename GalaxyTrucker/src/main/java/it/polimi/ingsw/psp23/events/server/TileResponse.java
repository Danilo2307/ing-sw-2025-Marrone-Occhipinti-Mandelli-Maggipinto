package it.polimi.ingsw.psp23.events.server;


import it.polimi.ingsw.psp23.model.components.Component;

/** response event sent from the server; sends the component requested from the client */
public record TileResponse(Component requested) implements Event { }
