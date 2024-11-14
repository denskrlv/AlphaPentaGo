package Client;

import Strategy.AlphaPentaGo;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Uses the GeneralComputerClient and the AlphaPentaGo (Smart Strategy) to play the game on server.
 */
public class SmartComputerClient {

    /**
     * The client creates a new socket with the server address and port number of Tom.
     * A new client is created using this socket. The start method of the GeneralComputerClient is called
     * with the smart strategy AlphaPentaGo and the name PentaClient as parameters.
     *
     * @param   args  CLI
     */
    public static void main(String[] args) {
        AlphaPentaGo alphaPentaGo;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter difficulty level of Ai (1-5, 5 is most difficult): ");
            int level = scanner.nextInt();
            alphaPentaGo = new AlphaPentaGo(level);
            if (level < 1 || level > 5) {
                System.out.println("The difficulty level you chose is not between 1 and 5, try again.");
            } else {
                break;
            }
        }
        try {
            Socket sock = new Socket("130.89.253.64", 55555);
            GeneralComputerClient computerClient = new GeneralComputerClient(sock);
            computerClient.start(alphaPentaGo.getName(), alphaPentaGo);
        } catch (UnknownHostException e) {
            System.out.println("Oops, something went wrong. Cannot find server.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Oops, something went wrong.");
            e.printStackTrace();
        }

    }

}
