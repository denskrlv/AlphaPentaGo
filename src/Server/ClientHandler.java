package Server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Handles the client by providing information to the server.
 * <p>
 * This is accomplished by using several methods like getUsername, setUsername, addClient and removeClient. It also
 * makes sure that the client is removed from the server when they disconnect and the class provides a communication
 * function so that the server is able to send stuff to a client.
 */
public class ClientHandler implements Runnable{

    private ServerImplementation server;

    private Socket socket;
    private final BufferedWriter out;
    private boolean run;
    private ArrayList<String> clients = new ArrayList();
    private String username;

    /**
     * New ClientHandler object is created
     *
     * @param   s           the socket which connects to server
     * @param   server      the server itself
     * @throws  IOException
     */
    public ClientHandler(Socket s, ServerImplementation server) throws IOException {
        this.server = server;
        this.socket = s;
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.run = true;
        new Thread(this::run).start();
    }

    /**
     * Reads from the user by using the input of the socket, the messages are passed to the handleMessage method.
     * When connection is lost, this is sent to the handleMessage method as well. After this, clientHandler is closed
     * and is removed from server.
     */
    @Override
    public void run() {
        try {
            var reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String nextLine = reader.readLine();
            while (nextLine != null) {
                if (nextLine == null) {
                    continue;
                }
                else {
                    server.handleMessage(this, nextLine);
                }
                nextLine = reader.readLine();
            }
            server.handleMessage(this, "Connection to " + server.getPort() + " is lost");
        } catch (IOException e) {
            System.out.println("Oh no, connection to client " + getUserName() + " is lost");
            server.removeClientInGame(this);
            server.removeClient(this);
            server.handleMessage(this, "DISCONNECT");
            this.close();
        } finally {
            this.close();
            server.removeClient(this);
        }
    }

    /**
     * Checks what the message of the user is and acts accordingly.
     * If the message is PING, PONG is replied. If message contains HELLO, HELLO~Pentago 2D is replied.
     * If the message is LOGIN, first there is a check if this message was in the form LOGIN~username. If not, error
     * is replied. If this is the case, the method checks whether this user already exists in the system and
     * whether the user not already logged themselves in. If this is the case, ALREADYLOGGEDIN is replied. If this
     * is not the case, the username is set, the client is added to the list of clients and LOGIN is replied.
     *
     * @param   from        the username of the client that sent the message
     * @param   message     the message that was sent by the client
     */
    public synchronized void sendChat(String from, String message) {
        try {
            if (message.equals("PING")) {
                System.out.println("[" + from + "]: " + message);
                System.out.println("[SERVER]: PONG");
                out.write("PONG");
                out.newLine();
                out.flush();
            }
            if (message.contains("HELLO")) {
                System.out.println("[" + from + "]: " + message);
                System.out.println("[SERVER]: HELLO~Pentago 2D");
                out.write("HELLO~Pentago 2D");
                out.newLine();
                out.flush();
            }
        } catch(IOException e) {
            e.printStackTrace();
            this.close();
        }
    }

    /**
     * Used by the server itself to pass strings to the client.
     *
     * @param   string  message to send to client
     */
    public void passString(String string) {
        try {
            out.write(string);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the socket
     */
    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns private field username
     * @return  username of client
     */
    public String getUserName() {
        if (username == null) {
            return "NONAME";
        }
        return this.username;
    }

    /**
     * Sets username of client
     */
    public void setUserName(String name) {this.username = name;}

    /**
     * Adds username of client to list
     */
    public void addClient(String userName) {
        clients.add(userName);
    }

    /**
     * Adds username of client to list
     */
    public void removeClient(String userName) {
        clients.remove(userName);
    }

}
