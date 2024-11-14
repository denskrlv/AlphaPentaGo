package Client;

import Game.Board;
import Game.Mark;
import Player.AbstractPlayer;
import Player.BotPlayer;
import Strategy.Strategy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * GeneralComputerClient is a class that provides an important structure for both the NaiveComputerClient and SmartComputerClient.
 * This is a fully automated process. By running either the Naive- or SmartComputerClient, the start method in this class will be
 * called. This method will use the computer bot to determine the next move. This is done by using the classes in the Strategy
 * package. This class also creates a board on which all moves are, similarly to the server, visually shown.
 */
public class GeneralComputerClient {

    private BufferedReader in;
    private PrintWriter out;

    private Board board;
    private boolean turn = true;

    private AbstractPlayer player;
    private int playerNumber = 0;
    private int index;
    private int rotation;

    /**
     * Creates a new GeneralComputerClient object with in- and output of socket
     *
     * @param   socket         socket which connects to server
     * @throws  IOException
     */
    public GeneralComputerClient(Socket socket) throws IOException {
        this.out = new PrintWriter(socket.getOutputStream());
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    /**
     * Starts the general computer client with the name of the bot and the desired strategy
     *
     * @param   nameBot     name that will be used on the server
     * @param   strategy    strategy that the bot will use on the server
     */
    public void start(String nameBot, Strategy strategy) {
        try {
            //do initialization
            out.println("HELLO~"+nameBot);
            System.out.println("[YOU]: HELLO~"+nameBot);
            out.flush();
            out.println("LOGIN~"+nameBot);
            System.out.println("[YOU]: LOGIN~"+nameBot);
            out.flush();
            System.out.println("[SERVER]: " + in.readLine());
            System.out.println("[SERVER]: " + in.readLine());

            //loop to get in queue and play a game
            outerloop: while (true) {
                //get into queue
                out.flush();
                out.println("QUEUE");
                System.out.println("[YOU]: QUEUE\n");
                out.flush();
                String msg = in.readLine();

                //waits till a new game is started, will reply to PING or other messages if they are sent
                while (!msg.contains("NEWGAME")) {
                    System.out.println("[SERVER]:" + msg + "\n");
                    if (msg.contains("GAMEOVER") || msg.contains("ERROR")) {
                        continue outerloop;
                    }
                    if (msg.equals("PING")) {
                        System.out.println("[YOU]: PONG");
                        out.println("PONG");
                        out.flush();
                    }
                    msg = in.readLine();
                }
                System.out.println("[SERVER]: " + msg + "\n");
                if (msg.contains("NEWGAME")) {
                    board = new Board();
                    while (!board.gameOver()) {

                        //determine which player is our bot
                        String[] splitted = msg.split("~");
                        String player1 = splitted[1];
                        String player2 = splitted[2];
                        if (player1.equals(nameBot)) {
                            player = new BotPlayer(Mark.XX, strategy);
                            playerNumber = 1;
                            turn = true;
                            System.out.println("Bot is player 1\n");
                        } else if (player2.equals(nameBot)) {
                            player = new BotPlayer(Mark.OO, strategy);
                            playerNumber = 2;
                            turn = false;
                            System.out.println("Bot is player 2\n");
                        }

                        //will play the game according to whether we start or not
                        if (playerNumber == 2) {
                            turn = false;
                            while (!board.gameOver()) {

                                //we do not start, so first read move from other user
                                String move = in.readLine();
                                while (!move.contains("MOVE")) {
                                    System.out.println("Got something from server without MOVE, namely " + move);
                                    if (move.contains("GAMEOVER")) {
                                        System.out.println("The game is over: " + move);
                                        continue outerloop;
                                    }
                                    if (move.equals("PING")) {
                                        System.out.println("PONG");
                                        out.println("PONG");
                                        out.flush();
                                    }
                                    move = in.readLine();
                                }
                                System.out.println("Their move: " + move);
                                String[] splittedCommand = move.split("~");
                                index = Integer.parseInt(splittedCommand[1]);
                                rotation = Integer.parseInt(splittedCommand[2]);

                                //set the board
                                if (board.isEmptyField(index)) {
                                    if (turn == true) {
                                        board.setField(index, Mark.XX);
                                        turn = false;
                                    } else {
                                        board.setField(index, Mark.OO);
                                        turn = true;
                                    }
                                    board.rotate(rotation);
                                }
                                System.out.println(board.toString());

                                //now read our move
                                if (!board.gameOver()) {
                                    String[] ourMove = player.determineMove(board);
                                    String ourMoveString = ourMove[0]+"~"+ourMove[1]+"~"+ourMove[2];
                                    out.println(ourMoveString);
                                    out.flush();
                                    System.out.println("Our move: " + ourMoveString);
                                    String temp = in.readLine();
                                    while (!temp.contains("MOVE")) {
                                        System.out.println("Got something from server without MOVE, namely " + temp);
                                        if (temp.contains("GAMEOVER")) {
                                            System.out.println("The game is over: " + temp);
                                            continue outerloop;
                                        }
                                        if (temp.equals("PING")) {
                                            System.out.println("PONG");
                                            out.println("PONG");
                                            out.flush();
                                        }
                                        temp = in.readLine();
                                    }
                                    index = Integer.parseInt(ourMove[1]);
                                    rotation = Integer.parseInt(ourMove[2]);

                                    //set the board
                                    if (board.isEmptyField(index)) {
                                        if (turn == true) {
                                            board.setField(index, Mark.XX);
                                            turn = false;
                                        } else {
                                            board.setField(index, Mark.OO);
                                            turn = true;
                                        }
                                        board.rotate(rotation);
                                    }
                                    System.out.println(board.toString());
                                } else {
                                    String result = in.readLine();
                                    if (result.contains("GAMEOVER")) {
                                        System.out.println("The game is over: " + result);
                                        continue outerloop;
                                    }
                                }
                            }
                        } else if (playerNumber == 1) {
                            turn = true;
                            while (!board.gameOver()) {

                                //we do start now, so first send and read our move
                                String[] ourMove = player.determineMove(board);
                                String ourMoveString = ourMove[0]+"~"+ourMove[1]+"~"+ourMove[2];
                                out.println(ourMoveString);
                                out.flush();
                                System.out.println("Our move: " + ourMoveString);
                                String temp = in.readLine();
                                while (!temp.contains("MOVE")) {
                                    System.out.println("Got something from server without MOVE, namely " + temp);
                                    if (temp.contains("GAMEOVER")) {
                                        System.out.println("The game is over: " + temp);
                                        continue outerloop;
                                    }
                                    if (temp.equals("PING")) {
                                        System.out.println("PONG");
                                        out.println("PONG");
                                        out.flush();
                                    }
                                    temp = in.readLine();
                                }
                                index = Integer.parseInt(ourMove[1]);
                                rotation = Integer.parseInt(ourMove[2]);

                                //set the board
                                if (board.isEmptyField(index)) {
                                    if (turn == true) {
                                        board.setField(index, Mark.XX);
                                        turn = false;
                                    } else {
                                        board.setField(index, Mark.OO);
                                        turn = true;
                                    }
                                    board.rotate(rotation);
                                }
                                System.out.println(board.toString());

                                //now get their move
                                if (!board.gameOver()) {
                                    String move = in.readLine();
                                    while (!move.contains("MOVE")) {
                                        System.out.println("Got something from server without MOVE, namely " + move);
                                        if (move.contains("GAMEOVER")) {
                                            System.out.println("The game is over: " + move);
                                            continue outerloop;
                                        }
                                        if (move.equals("PING")) {
                                            System.out.println("PONG");
                                            out.println("PONG");
                                            out.flush();
                                        }
                                        move = in.readLine();
                                    }
                                    System.out.println("Their move: " + move);
                                    String[] splittedCommand = move.split("~");
                                    index = Integer.parseInt(splittedCommand[1]);
                                    rotation = Integer.parseInt(splittedCommand[2]);

                                    //set the board
                                    if (board.isEmptyField(index)) {
                                        if (turn == true) {
                                            board.setField(index, Mark.XX);
                                            turn = false;
                                        } else {
                                            board.setField(index, Mark.OO);
                                            turn = true;
                                        }
                                        board.rotate(rotation);
                                    }
                                    System.out.println(board.toString());
                                } else {
                                    String result = in.readLine();
                                    if (result.contains("GAMEOVER")) {
                                        System.out.println("The game is over: " + result);
                                        continue outerloop;
                                    }
                                }
                            }
                        }
                    }
                    //if everything went well, we go to flag outer loop to play another game
                    System.out.println(in.readLine());
                    continue outerloop;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
