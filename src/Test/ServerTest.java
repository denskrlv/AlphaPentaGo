package Test;

import Server.ServerImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test that checks whether the server behaves as it should. Lots of errors and valid moves are checked. Comments
 * are used to describe what each part of the test tests.
 */
public class ServerTest {

    private ServerImplementation server;

    @BeforeEach
    void setUp() throws IOException {
        server = new ServerImplementation();
    }

    @Test
    void testServer() throws IOException {
        server.start(0);  // start the server
        Socket socket = new Socket(InetAddress.getLocalHost(), server.getPort());  // connect to the server
        Socket socket1 = new Socket(InetAddress.getLocalHost(), server.getPort());  // connect to the server
        String s;

        // using a try-with-resources block, we ensure that reader/writer are closed afterwards
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
             BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
             PrintWriter printWriter1 = new PrintWriter(new OutputStreamWriter(socket1.getOutputStream()), true)) {

            // test hello message
            printWriter.println("HELLO~Test Client");
            s = bufferedReader.readLine();
            assertEquals("HELLO~Pentago 2D", s);

            printWriter1.println("HELLO~Test Client 1");
            s = bufferedReader1.readLine();
            assertEquals("HELLO~Pentago 2D", s);

            // test login message
            printWriter.println("LOGIN~Name");
            s = bufferedReader.readLine();
            assertEquals("LOGIN", s);

            printWriter1.println("LOGIN~Name 1");
            s = bufferedReader1.readLine();
            assertEquals("LOGIN", s);

            // test allreadyloggedin message
            printWriter.println("LOGIN~Name");
            s = bufferedReader.readLine();
            assertEquals("ALREADYLOGGEDIN", s);

            // test list message
            printWriter.println("LIST");
            s = bufferedReader.readLine();
            assertEquals("LIST~Name~Name 1", s);

            // test ping message
            printWriter.println("PING");
            s = bufferedReader.readLine();
            assertEquals("PONG", s);

            // get in queue
            printWriter.println("QUEUE");
            printWriter1.println("QUEUE");

            s = bufferedReader.readLine();
            assertTrue(s.equals("NEWGAME~Name~Name 1") || s.equals("NEWGAME~Name 1~Name"));
            s = bufferedReader1.readLine();
            assertTrue(s.equals("NEWGAME~Name~Name 1") || s.equals("NEWGAME~Name 1~Name"));

            //make a valid moves
            printWriter.println("MOVE~0~0");
            s = bufferedReader.readLine();
            bufferedReader1.readLine();
            assertEquals("MOVE~0~0", s);

            //make an invalid move, index 12 because we just placed X at index 0 and rotated to the left
            printWriter1.println("MOVE~12");
            s = bufferedReader1.readLine();
            assertEquals("ERROR~Wrong parameters", s);

            //make move while not turn
            printWriter.println("MOVE~0~0");
            s = bufferedReader.readLine();
            assertEquals("ERROR~Not your turn", s);

            //make a valid moves
            printWriter1.println("MOVE~1~1");
            s = bufferedReader1.readLine();
            assertEquals("MOVE~1~1", s);
            s = bufferedReader.readLine();
            assertEquals("MOVE~1~1", s);

            //make diagonal for x
            printWriter.println("MOVE~7~4");
            s = bufferedReader.readLine();
            s = bufferedReader1.readLine();
            printWriter1.println("MOVE~1~4");
            s = bufferedReader.readLine();
            s = bufferedReader1.readLine();
            printWriter.println("MOVE~14~4");
            s = bufferedReader.readLine();
            s = bufferedReader1.readLine();
            printWriter1.println("MOVE~2~4");
            s = bufferedReader.readLine();
            s = bufferedReader1.readLine();
            printWriter.println("MOVE~21~4");
            s = bufferedReader.readLine();
            s = bufferedReader1.readLine();
            printWriter1.println("MOVE~3~4");
            s = bufferedReader.readLine();
            s = bufferedReader1.readLine();
            printWriter.println("MOVE~28~4");
            s = bufferedReader.readLine();
            s = bufferedReader1.readLine();

            //check if winner is correct
            s = bufferedReader.readLine();
            assertEquals("GAMEOVER~VICTORY~Name", s);
            s = bufferedReader1.readLine();
            assertEquals("GAMEOVER~VICTORY~Name", s);

            //make move while game not active
            printWriter.println("MOVE~28~4");
            s = bufferedReader.readLine();
            assertEquals("ERROR~No active game", s);

            // get in queue again to start a new game
            printWriter.println("QUEUE");
            printWriter1.println("QUEUE");

            s = bufferedReader.readLine();
            assertTrue(s.equals("NEWGAME~Name~Name 1") || s.equals("NEWGAME~Name 1~Name"));
            s = bufferedReader1.readLine();
            assertTrue(s.equals("NEWGAME~Name~Name 1") || s.equals("NEWGAME~Name 1~Name"));

            //make a valid moves
            printWriter.println("MOVE~0~0");
            s = bufferedReader.readLine();
            bufferedReader1.readLine();
            assertEquals("MOVE~0~0", s);

            //make invalid move by making rotation too high or invalid index
            printWriter1.println("MOVE~25~10");
            s = bufferedReader1.readLine();
            assertEquals("ERROR~You can only rotate between 0 and 7", s);
            printWriter1.println("MOVE~40~0");
            s = bufferedReader1.readLine();
            assertEquals("ERROR~This is not a position on the board", s);

        } finally {
            // All good. Close the connection, stop the server.
            socket.close();
            socket1.close();
            server.stop();
        }
    }
}

