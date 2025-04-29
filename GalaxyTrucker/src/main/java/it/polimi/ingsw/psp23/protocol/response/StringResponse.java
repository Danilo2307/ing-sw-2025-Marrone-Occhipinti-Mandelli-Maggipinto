package it.polimi.ingsw.psp23.protocol.response;


/* evento usato da server per inviare info testuali di qualsiasi genere al client (conferme, esiti operazioni)*/
public record StringResponse(String message) implements Event { }
