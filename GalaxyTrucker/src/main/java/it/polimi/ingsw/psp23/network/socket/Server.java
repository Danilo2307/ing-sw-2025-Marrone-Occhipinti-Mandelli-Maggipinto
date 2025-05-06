package it.polimi.ingsw.psp23.network.socket;

import it.polimi.ingsw.psp23.controller.Controller;
import it.polimi.ingsw.psp23.exceptions.PlayerExistsException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.messages.GetActionVisitor;
import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.protocol.request.Action;
import it.polimi.ingsw.psp23.protocol.request.HandleActionVisitor;
import it.polimi.ingsw.psp23.protocol.request.SetUsernameActionVisitor;
import it.polimi.ingsw.psp23.protocol.response.AppropriateUsername;
import it.polimi.ingsw.psp23.protocol.response.WrongUsername;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server {

    private static Server instance;
    private ServerSocket serverSocket;
    private final HashMap<String, SocketHandler> clients;


    // Qui creo il server
    Server(int port) {
        try {

            serverSocket = new ServerSocket(port, 10, InetAddress.getByName("localhost"));

            clients = new HashMap<>();

            System.out.println("Server started at port " + port + " and with address " + serverSocket.getInetAddress());

        } catch (IOException e) {
            throw new RuntimeException("Could not load the server: error in constructor of class Server" + e.getMessage() + ")");
        }
    }

    Server(int port, String host) {
        try {

            serverSocket = new ServerSocket(port, 10, InetAddress.getByName(host));

            clients = new HashMap<>();

            System.out.println("Server started at port " + port + " and with address " + serverSocket.getInetAddress());

        } catch (IOException e) {
            throw new RuntimeException("Could not load the server: error in constructor of class Server" + e.getMessage() + ")");
        }
    }

    public static synchronized Server getInstance() {

        if (instance == null) {
            throw new RuntimeException("The server has not been initialized yet(from getInstance in class Server)");
        } else {
            return instance;
        }
    }

    public static synchronized Server getInstance(int port) {
        if (instance == null) {
            instance = new Server(port);
        }

        return instance;
    }

    public static synchronized Server getInstance(String host, int port) {
        if (instance == null) {
            instance = new Server(port, host);
        }
        return instance;
    }

    public void close() {
        try {
            serverSocket.close();
        }
        catch (IOException e) {
            System.out.println("Error closing server: " + e.getMessage());
        }
    }

    public void setServerSocket(String host, int port) {
        try {
            ServerSocket newSock = new ServerSocket();
            newSock.setReuseAddress(true);
            newSock.bind(new InetSocketAddress(host, port), 10);
            serverSocket = newSock;
        }
        catch (IOException e){
            System.out.println("Error re-instancing server: " + e.getMessage());
        }
    }

    public void connectClients(String nameConnection) {
        if (clients.containsKey(nameConnection)) {
            throw new RuntimeException("La connessione è già presente!!");
        }
        synchronized (serverSocket) {
            try {

                Socket socket = serverSocket.accept();

                synchronized (clients) {
                    // Nel caso in cui si sia registrato solo il primo client dobbiamo chiudere la connessione con altri
                    // client perchè prima il "leader" della partita deve dichiarare quanti giocatori possono entrare
                    if(clients.isEmpty()){
                        ConnectionThread.getInstance().stopAccepting();
                    }
                }

                // Questa istruzione serve per non andare avanti all'infinito ma, nel caso in cui dopo aver stabilito
                // la connessione il client non dovesse mandare niente per 5 sec, questa istruzione lancerà un'eccezione
                // socket.setSoTimeout(60000);

                SocketHandler socketHandler = new SocketHandler(socket);

                // Adesso mi occupo di ricevere lo username del player
                // Queste sono le istruzioni necessarie per ricevere lo username dal client, ELABORARLO e permettere
                // al game di aggiungere il player alla lista di player tramite l'handle delle action

                Action a = null;
                String username = null;

                boolean error;
                do {
                    try {
                        a = socketHandler.readMessage().call(new GetActionVisitor());
                        username = a.call(new SetUsernameActionVisitor());
                        a.call(new HandleActionVisitor(), username);
                        error = false;
                    }
                    catch(PlayerExistsException e){
                        socketHandler.sendMessage(new DirectMessage(new WrongUsername()));
                        error = true;
                    }
                } while(error);

                socketHandler.sendMessage(new DirectMessage(new AppropriateUsername()));
                socketHandler.setUsername(username);

                synchronized (clients) {
                    clients.put(nameConnection, socketHandler);
                    System.out.println("Client connected: " + nameConnection + " with username " + socketHandler.getUsername());
                }

                if(clients.size() == Game.getInstance().getNumRequestedPlayers()){
                    Controller.getInstance().startBuildingPhase();
                }


            } catch (IOException e) {
                throw new RuntimeException("Could not accept the client in connectClients in Server" + e.getMessage());
            }

        }
    }


    // Questo metodo ci fornirà l'indirizzo ip del server
    public InetAddress getInetAddress() {

        // Sto ritornando un riferimento all'InetAddress, ma tanto gli InetAddress sono immutabili, quindi
        // non corriamo rischi
        return serverSocket.getInetAddress();

    }


    // Metodo incaricato dell'invio dei messaggi ai client. Il parametro indice ci dice quale elemento della lista di
    // socket bisogna considerare
    public void sendMessage(Message message, String nameConnection) {

        SocketHandler socketHandler = null;

        synchronized (clients) {
            socketHandler = clients.get(nameConnection);
        }

        if (socketHandler != null) {
            System.out.println("Messaggio inviato con esito: " + socketHandler.sendMessage(message));
        } else {
            throw new RuntimeException("No client with name " + nameConnection);
        }
    }

    /* Questo metodo permette di inviare messaggi conoscendo lo username e non il connectionID
       TODO: bisogna gestire il caso in cui ci siano client con username uguali, magari si potrebbe mettere un controllo
        al momento dell'inserimento
    */
    public void sendMessage(String username, Message message) {
        if (username != null && message != null) {
            String connectionID = null;
            synchronized (clients) {
                for (String connection : clients.keySet()) {
                    if (clients.get(connection).getUsername().equals(username)) {
                        connectionID = connection;
                        break;
                    }
                }
            }
            sendMessage(message, connectionID);
        } else {
            throw new RuntimeException("message or username in Server.sendMessage(username, message) are null (tried to send: " + message.toString() + ")");
        }
    }

    public Message receiveMessage(String nameConnection) {

        SocketHandler socketHandler = null;

        synchronized (clients) {
            socketHandler = clients.get(nameConnection);
        }

        if (socketHandler != null) {
            return socketHandler.readMessage();
        }
        return null;
    }

    public void notifyAllObservers(Message message) {
        synchronized (clients) {
            for (String connection : clients.keySet()) {
                sendMessage(message, connection);
                System.out.println("Notify observer: " + getUsernameForConnection(connection) + " with message: " + message.toString());
            }
        }
    }

    // Metodo per chiudere la connessione con un solo client specifico
    public void closeConnection(String nameConnection) {
        synchronized (clients) {
            SocketHandler socketHandler = clients.get(nameConnection);

            if (socketHandler != null) {
                socketHandler.close();
                clients.remove(nameConnection);
            }
        }
    }

    public void stampa() {
        if (clients.isEmpty()) {
            System.out.println("No client connected");
        } else {
            for (HashMap.Entry<String, SocketHandler> entry : clients.entrySet()) {
                String connectionId = entry.getKey();
                SocketHandler handler = entry.getValue();
                Socket socket = handler.socket;  // stessa package; altrimenti aggiungi getSocket()

                System.out.println("ConnectionId: " + connectionId + ", RemoteAddress: " + socket.getRemoteSocketAddress());
                System.out.println("");
            }
        }
    }

    public int getSize() {
        return clients.size();
    }


    public String getNameConnection(SocketHandler socketHandler) {
        for (String key : clients.keySet()) {
            if (clients.get(key) == socketHandler) {
                return key;
            }
        }
        return null;
    }

    public HashMap<String, SocketHandler> getClients() {
        return new HashMap<>(clients);
    }

    public String getUsernameForConnection(String connectionID) {
        return clients.get(connectionID).getUsername();
    }

}
