package it.polimi.ingsw.psp23.events.server;

import it.polimi.ingsw.psp23.events.Event;

/* evento usato da server per inviare info testuali di qualsiasi genere al client (conferme, esiti operazioni)*/
public record StringResponse(String message) implements Event { }
