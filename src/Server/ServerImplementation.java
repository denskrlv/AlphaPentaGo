package Server;

import Game.Board;
import Game.Mark;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Provides the fundamental behavior of the server.
 * <p>
 * The class provides a clientHandlerList, clientHandlerListInGame and the board in order to keep track of all users
 * on the server and the users who are participating in the game. It will add clients to these lists when a connection
 * between a client and this server is established. The handleMessage method takes care of all the inputs of the client
 * and sends messages back by using the ClientHandler class.
 */
public class ServerImplementation implements Runnable{

    private ServerSocket ss;
    private Thread newThread;

    private List<ClientHandler> clientHandlerList = new ArrayList<>();
    private List<ClientHandler> clientHandlerListInGame = new ArrayList<>();
    private LinkedList<String> queue = new LinkedList<>();
    private Board board;
    private int current = 0;

    /**
     * Initializes the ServerSocket and starts new thread on this object.
     *
     * @param   port    port used to create new server socket
     */
    public void start(int port) {
        try {
            ss = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Can't connect to the server.");
        }
        newThread = new Thread(this, "Pentago Server");
        newThread.start();
    }

    /**
     * Returns private port of the server.
     *
     * @return      port of server
     */
    public int getPort() {
        return ss.getLocalPort();
    }

    /**
     * Closes the server socket and waits for the thread to terminate.
     */
    public void stop() {
        try {
            ss.close();
            newThread.join();
        } catch (IOException | InterruptedException e) {
            System.out.println("Can't close the connection.");
        }
    }

    /**
     * Accepts sockets to the server and prints that it got a connection. New ClientHandler is created using this socket
     * and ClientHandler is added to a list.
     */
    @Override
    public void run() {
        try {
            while(!ss.isClosed()) {
                Socket s = ss.accept();
                System.out.println("Got connection from: " + s.getInetAddress().getHostAddress());

                ClientHandler clientHandler = new ClientHandler(s, this);

                clientHandlerList.add(clientHandler);
            }
        } catch (IOException e) {
            System.out.println("The server is closed");
        }
    }

    /**
     * Handles the messages that are given from the user.
     * If the message equals QUEUE. If the username of the client is not already in the queue, they are added. If
     * the length of the queue is longer than 2, the first two in the queue are put in a game. In this case,
     * a new game should be started with the first two clients. The ClientHandlers of the two clients that are put into the
     * game are added to a separate list. The two clients are removed from the queue, they are notified using NEWGAME command,
     * and a new board is created. If the command is LIST, the client that requested this will get the LIST command returned
     * from the server with all usernames of the clients added after this with tilda's. If the message is QUIT, the client
     * is removed from the server. If the message is MOVE, then this move will be performed on the board object. It first
     * checks if the board is not null and if the user that uses the MOVE command is in the game. If not, an ERROR is sent
     * to this user. Then it checks if the input is in the form MOVE~element~rotation. If not, an ERROR is sent to the user.
     * It will also check if it is the users turn when using this command and if the cell is empty and valid. If this is all
     * the case, the move is performed. Before and after the rotation, the checkWinner method is called to check if the board
     * has a winner. In the end, the name of the user and the message are passed to the sendChat method in ClientHandler.
     *
     * @param   from        ClientHandler that gave the message
     * @param   message     the message itself
     * @throws  IOException
     */
    public synchronized void handleMessage(ClientHandler from, String message) {

        //check winner each time the method is called in order to check if someone disconnected
        if (!(board == null)) {
            checkWinner();
        }
        String userName = from.getUserName();

        //login the user, send either LOGIN, ERROR or ALREADYLOGGEDIN depending on input of user
        if (message.contains("LOGIN")) {
            System.out.println("[" + from.getUserName() + "]: " + message);
            if (message.split("~").length != 2) {
                from.passString("ERROR~Wrong Parameters");
            } else {
                String name = message.split("~")[1];
                boolean doesNotExist = true;
                //send ALREADYLOGGEDIN if another player has same name
                for (ClientHandler c : clientHandlerList) {
                    if (name.equals(c.getUserName())) {
                        doesNotExist = false;
                        from.passString("ALREADYLOGGEDIN");
                        System.out.println("[SERVER]: ALREADYLOGGEDIN");
                    }
                }
                //send ALREADYLOGGEDIN if the user already logged in before
                if (!from.getUserName().equals("NONAME") && doesNotExist) {
                    doesNotExist = false;
                    from.passString("ALREADYLOGGEDIN");
                    System.out.println("[SERVER]: ALREADYLOGGEDIN");
                }
                //if the user does not exist yet, set username and add to clients
                if (doesNotExist) {
                    from.setUserName(name);
                    from.addClient(from.getUserName());
                    System.out.println("[SERVER]: LOGIN");
                    from.passString("LOGIN");
                }
            }
        }

        //put the user in the queue, also immediately check if the queue is already of length 2, if so, start a game
        if (message.equals("QUEUE")) {
            //send error if user did not login
            if (from.getUserName().equals("NONAME")) {
                from.passString("ERROR~You did not login yet");
            } else {
                System.out.println("[" + from.getUserName() + "]: " + message);
                if (!queue.contains(from.getUserName())) {
                    queue.add(from.getUserName());
                }
                for (int i = 0; i < queue.size(); i++) {
                    System.out.println("Member in queue: " + queue.get(i));
                }
                //start a new game when there are 2 people in the queue
                if (queue.size() > 1) {
                    String startGame = "NEWGAME";
                    String player1 = queue.get(0);
                    String player2 = queue.get(1);
                    startGame += "~" + player1;
                    startGame += "~" + player2;
                    for (ClientHandler c : clientHandlerList) {
                        if (c.getUserName().equals(player1)) {
                            c.passString(startGame);
                            clientHandlerListInGame.add(c);
                        }
                    }
                    for (ClientHandler c : clientHandlerList) {
                        if (c.getUserName().equals(player2)) {
                            c.passString(startGame);
                            clientHandlerListInGame.add(c);
                        }
                    }
                    System.out.println("[SERVER]: " + startGame);
                    System.out.println(player1 + " and " + player2 + " are in a game");
                    queue.pop();
                    queue.pop();
                    board = new Board();
                }
            }
        }

        //send a list of all users of the server
        else if (message.equals("LIST")) {
            System.out.println("[" + from.getUserName() + "]: " + message);
            String allUsers = "LIST";
            for (ClientHandler c: clientHandlerList) {
                allUsers += "~" + c.getUserName();
            }
            System.out.println("[SERVER]: " + allUsers);
            for (ClientHandler c: clientHandlerList) {
                if (c.getUserName().equals(from.getUserName())) {
                    c.passString(allUsers);
                }
            }
        }

        //return a hint for the user
        else if (message.equals("HINT")) {
            boolean valid = false;
            while (!valid) {
                int i = 0;
                if (board.isEmptyField(i)) {
                    System.out.println("HINT: MOVE~"+i+"~"+0);
                    from.passString("HINT~MOVE~"+i+"~"+0);
                    valid = true;
                } else {
                    i += 1;
                }
            }
        }

        //someone disconnected from the server, if there is a game, then the user that is left over wins
        else if (message.equals("DISCONNECT")) {
            if (clientHandlerListInGame.size() == 1) {
                System.out.println("The game will be ended because connection is lost");
                for (ClientHandler c : clientHandlerListInGame) {
                    c.passString("GAMEOVER~DISCONNECT~" + c.getUserName());
                }
            }
        }

        //keeps track of the board and returns in errors when the user does something wrong
        else if (message.contains("MOVE")) {
            //error if user is not in a game
            if (board == null || !(clientHandlerListInGame.contains(from))) {
                from.passString("ERROR~No active game");
            }
            //error if the user gave more or less parameters
            else if (message.split("~").length != 3) {
                from.passString("ERROR~Wrong parameters");
            }
            //error if the user wants to rotate a board that does not exist
            else if (Integer.parseInt(message.split("~")[2]) > 7 || Integer.parseInt(message.split("~")[2]) < 0) {
                from.passString("ERROR~You can only rotate between 0 and 7");
            }
            //error if the user wants to place something on an index that does not exist
            else if (Integer.parseInt(message.split("~")[1]) > 35 || Integer.parseInt(message.split("~")[1]) < 0) {
                from.passString("ERROR~This is not a position on the board");
            }
            else if (!board.gameOver()) {
                String[] splittedCommand = message.split("~");
                System.out.println("[" + from.getUserName() + "]: " + message);
                //if it is the players turn, then send the message to all clients in the game and set the board
                if (from.getUserName().equals(clientHandlerListInGame.get(0).getUserName()) && current == 0) {
                    for (ClientHandler c : clientHandlerListInGame) {
                        c.passString(message);
                    }
                    if (board.isEmptyField(Integer.parseInt(splittedCommand[1]))) {
                        board.setField(Integer.parseInt(splittedCommand[1]), Mark.XX);
                        if (!checkWinner()) {
                            board.rotate(Integer.parseInt(splittedCommand[2]));
                            current = 1;
                        }
                    } else {
                        //error if there is already something on the cell
                        clientHandlerListInGame.get(0).passString("ERROR~Cell is not empty");
                        current = 0;
                    }
                    System.out.println(board.toString());
                }
                //if it is the players turn, then send the message to all clients in the game and set the board
                else if (from.getUserName().equals(clientHandlerListInGame.get(1).getUserName()) && current == 1) {
                    for (ClientHandler c : clientHandlerListInGame) {
                        c.passString(message);
                    }
                    if (board.isEmptyField(Integer.parseInt(splittedCommand[1]))) {
                        board.setField(Integer.parseInt(splittedCommand[1]), Mark.OO);
                        if (!checkWinner()) {
                            board.rotate(Integer.parseInt(splittedCommand[2]));
                            current = 0;
                        }
                    } else {
                        //error if there is already something on the cell
                        clientHandlerListInGame.get(1).passString("ERROR~Cell is not empty");
                        current = 1;
                    }
                    System.out.println(board.toString());
                } else {
                    //errors when the user wants to make a move while it is not their turn
                    if (current == 0) {
                        clientHandlerListInGame.get(1).passString("ERROR~Not your turn");
                    }
                    else if (current == 1) {
                        clientHandlerListInGame.get(0).passString("ERROR~Not your turn");
                    }
                }
                //check if there is a winner or if someone disconnected
                checkWinner();
            }
        }
        //send the message to the user
        from.sendChat(userName, message);
    }

    /**
     * Checks if there is a winner on the board. this is done by using the isWinner, hasWinner and gameOver methods
     * from class Board. If X is the winner, GAMEOVER~VICTORY~name is passed to the clients in the game with name
     * being the username of the user that won. If O is the winner, the same is done but then with the name of this
     * player. If the board has no winner but the game is over (because the board is full), GAMEOVER~DRAW is passed
     * to the clients in the game. In all of these cases, true is returned. If none of these apply, false is returned.
     *
     * @return      true or false, depending on whether there is a winner, a draw, or no winner
     */
    public synchronized boolean checkWinner() {
        if (board.isWinner(Mark.XX)) {
            for (ClientHandler c : clientHandlerListInGame) {
                c.passString("GAMEOVER~VICTORY~" + clientHandlerListInGame.get(0).getUserName());
            }
            clientHandlerListInGame.clear();
            return true;
        } if (board.isWinner(Mark.OO)) {
            for (ClientHandler c : clientHandlerListInGame) {
                c.passString("GAMEOVER~VICTORY~" + clientHandlerListInGame.get(1).getUserName());
            }
            clientHandlerListInGame.clear();
            return true;
        } if (!board.hasWinner() && board.gameOver()) {
            for (ClientHandler c : clientHandlerListInGame) {
                c.passString("GAMEOVER~DRAW");
            }
            clientHandlerListInGame.clear();
            return true;
        } if (clientHandlerListInGame.size() == 1) {
            System.out.println("The game will be ended because connection is lost");
            for (ClientHandler c : clientHandlerListInGame) {
                c.passString("GAMEOVER~DISCONNECT~" + c.getUserName());
            }
            clientHandlerListInGame.clear();
        }
        return false;
    }

    /**
     * Client is removed from the clientHandlerList
     *
     * @param   from    the ClientHandler that needs to be removed
     */
    public synchronized void removeClient(ClientHandler from) {
        clientHandlerList.remove(from);
    }

    /**
     * Client is removed form the clientHandlerListInGame
     *
     * @param   from    the ClientHandler that needs to be removed
     */
    public synchronized void removeClientInGame(ClientHandler from) {
        System.out.println("Client is removed from active game");
        clientHandlerListInGame.remove(from);
    }

}
