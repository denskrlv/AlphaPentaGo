package Client;

import Game.Board;
import Game.Mark;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import static java.lang.System.exit;

/**
 * The Client class is the class that makes it possible for the user to connect to a server and play the Pentago game.
 * <p>
 * First, the client prompts the user to enter a server address and port number. These are used to create a new socket
 * and call the start method of an instance of this class. Some commands that can be sent by the server need to be
 * processed in the Client as well, for instance the command MOVE. The Client will create a board and create a visual
 * representation of the board on the server. In this way, a TUI is provided to the user so that they are able to
 * play the game.
 */
public class Client {

    private static int port;
    private static String address;
    private static Socket socket;
    private BufferedReader in;
    private Scanner scanner;
    private PrintWriter out;

    private Board board;
    private boolean turn = true;

    /**
     * Creates a new Client object with in- and output of socket
     *
     * @param   socket         socket which connects to server
     * @throws  IOException
     */
    public Client(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new PrintWriter(socket.getOutputStream());
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.scanner = new Scanner(System.in);
    }

    /**
     * The user is prompted to enter a server address and port number. These are used to create a new socket.
     * A new client is created using this socket. The start method of the client is called.
     *
     * @param   args  CLI
     */
    public static void main(String[] args) {
        while (true) {
            Scanner systemScanner = new Scanner(System.in);
            System.out.println("Give a server address to connect to:");
            address = systemScanner.nextLine();
            System.out.println("Give a port number to connect to:");
            port = systemScanner.nextInt();
            if (port < 0 || port > 65536) {
                System.out.println("Invalid port! Try again.");
            } else {
                break;
            }
        }
        try {
            Socket sock = new Socket(address, port);
            Client client = new Client(sock);
            client.start();
        } catch (UnknownHostException e) {
            System.out.println("Oops, something went wrong. Cannot find server.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Oops, something went wrong.");
            e.printStackTrace();
        }

    }

    /**
     * Prints starting screen of TUI to user.
     * A new thread is started with the output of the socket as parameter, which is used to write things to the server.
     * Simultaneously, all incoming messages are read using the input of the socket, these messages are passed to the handleMsg method.
     */
    public void start() {
        new Thread(new SendToServer(out)).start();
        try {
            System.out.println("\nWelcome to the server. These are the command you can use: ");
            System.out.println("You need to initialize by using:");
            System.out.println("* HELLO~<name>");
            System.out.println("* LOGIN~<name>");
            System.out.println("* QUEUE\n");
            System.out.println("After this, a game is started and there will be instructions how to make a move");
            System.out.println("You are always allowed to use the commands HELP, LIST, PING, QUIT\n");
            while (true) {
                handleMsg(in.readLine());
            }

        } catch (IOException e) {
            System.out.println("Oh no, connection to the server is lost");
            exit(0);
        }

    }

    /**
     * Reads messages from server and acts accordingly.
     * First checks if message is not null, after this it checks whether the message contains a tilda.
     * If there is no tilda, the message is just printed. If there is a tilda, the method checks whether
     * NEWGAME, MOVE or GAMEOVER was sent. A new board is created in order to show the user how the board
     * of the server looks like in the TUI. When NEWGAME is read, the user is prompted with some text on
     * what commands they can use. For GAMEOVER, the TUI tells the user what to do next.
     *
     * @param   msg     Message received by server
     */
    public void handleMsg(String msg) {
        String[] splittedCommand = new String[3];
        splittedCommand[0] = msg;
        if (msg == null) {
            System.out.println("No response from server");
        }
        else if (msg.contains("~")) {
            splittedCommand = msg.split("~");
            if (splittedCommand[0].equals("NEWGAME")) {
                board = new Board();
                System.out.println("Welcome to a new game! Enter a move in the format MOVE~<index>~<rotation>.");
                System.out.println(board.toString());
            }
            if (splittedCommand[0].equals("MOVE")) {
                int index = Integer.parseInt(splittedCommand[1]);
                int rotation = Integer.parseInt(splittedCommand[2]);
                if (board.isEmptyField(index)) {
                    if(turn == true) {
                        board.setField(index, Mark.XX);
                        turn = false;
                    } else {
                        board.setField(index, Mark.OO);
                        turn = true;
                    }
                    board.rotate(rotation);
                }
                System.out.println(board.toString());
            }
            if (splittedCommand[0].equals("GAMEOVER")) {
                System.out.println("The game is over! You can add yourself to the queue again to play a new game.");
            }
        }
        System.out.println("[SERVER]: " + msg);
    }

}
