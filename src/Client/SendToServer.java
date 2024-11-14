package Client;

import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Created thread that is able to send things to the server, while the client can simultaneously read stuff coming from the
 * server.
 */
public class SendToServer implements Runnable{

    private PrintWriter writer;
    private Scanner scanner;

    /**
     * Creates new SendToServer object
     *
     * @param   writer      output of the socket
     */
    public SendToServer(PrintWriter writer) {
        this.writer = writer;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Uses the writer (output of socket) to write messages that are entered in the TUI to the server. If the message that is
     * entered by the user is HELP, the TUI will show the starting screen again, with all commands that the user may use.
     */
    @Override
    public void run() {
        while (scanner.hasNextLine()) {
            String msg = scanner.nextLine();
            this.writer.println(msg);
            this.writer.flush();
            if (msg.equals("HELP")) {
                System.out.println("You need to initialize by using:");
                System.out.println("* HELLO~<name>");
                System.out.println("* LOGIN~<name>");
                System.out.println("* QUEUE\n");
                System.out.println("After this, a game is started and there will be instructions how to make a move");
                System.out.println("You are always allowed to use the commands HELP, LIST, PING, QUIT\n");

            }
            System.out.println("[YOU]: " + msg);
        }
    }
}
