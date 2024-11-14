package Client;

import Strategy.RandomBot;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Uses the GeneralComputerClient and the RandomBot (Naive Strategy) to play the game on server.
 */
public class NaiveComputerClient {

    /**
     * The client creates a new socket with the server address and port number of Tom.
     * A new client is created using this socket. The start method of the GeneralComputerClient is called
     * with the naive strategy RandomBot and the name PentaClient as parameters.
     *
     * @param   args  CLI
     */
    public static void main(String[] args) {
        try {
            Socket sock = new Socket("130.89.253.64", 55555);
            GeneralComputerClient computerClient = new GeneralComputerClient(sock);
            computerClient.start("RandomClient", new RandomBot());
        } catch (UnknownHostException e) {
            System.out.println("Oops, something went wrong. Cannot find server.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Oops, something went wrong.");
            e.printStackTrace();
        }

    }

}
