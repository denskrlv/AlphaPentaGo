package Server;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * The class that has a main method to start the server.
 * This class asks the user to enter a port on which the server will be started. It will check if the port is available,
 * and if not the user needs to enter another port. It will then use the start method of the ServerImplementation class
 * to fulfill the desired tasks of the server.
 */
public class ServerApplication {

    private static ServerImplementation serverImplementation = new ServerImplementation();
    private static int port;
    private static int actualPort;
    private static Scanner scanner = new Scanner(System.in);

    /**
     * Starts server. First asks user for port number to connect, then checks if it is valid. If it is, the start
     * method of serverImplementation is called. The port is printed to System.out. If someone types QUIT in the
     * System.in of this server, the server will terminate.
     *
     * @param   args    CLI
     */
    public static void main(String[] args) {
        while (true) {
            System.out.println("---WELCOME TO THE SERVER---");
            System.out.println("Give a port number to connect to:");
            port = scanner.nextInt();
            if (port < 0 || port > 65536) {
                System.out.println("Invalid port! Try again.");
            } else if (!available(port)) {
                System.out.println("Unfortunately, this port is not available. Try another port.\n");
            } else {
                break;
            }
        }
        serverImplementation.start(port);
        actualPort = serverImplementation.getPort();
        System.out.println("The port number is " + actualPort+"\n");
        while (true) {
            String input = scanner.nextLine();
            if (input.equals("QUIT")) {
                serverImplementation.stop();
                break;
            }
        }
    }

    /**
     * Checks if given port is available. If this is the case, true is returned. If the port is already in use,
     * false is returned.
     *
     * @param   port    the port of which availability needs to be checked
     * @return
     */
    private static boolean available(int port) {
        try (Socket ignored = new Socket("localhost", port)) {
            return false;
        } catch (IOException ignored) {
            return true;
        }
    }

}
