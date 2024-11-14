package Test;

import Client.Client;
import Server.ServerImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test that checks whether the clients and server interact with each other in a correct manner.
 */
public class ClientAndServerTest {

    private ServerImplementation server;
    private Client client;
    private Client client1;
    private Socket socket;
    private Socket socket1;

    @BeforeEach
    void setUp() throws IOException {
        server = new ServerImplementation();
        server.start(0);
        socket = new Socket(InetAddress.getLocalHost(), server.getPort());
        socket1 = new Socket(InetAddress.getLocalHost(), server.getPort());
        client = new Client(socket);
        client1 = new Client(socket1);
    }

    @Test
    void testClientAndServerAlreadyLoggedIn() throws IOException {

        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer1 = new PrintWriter(new OutputStreamWriter(socket1.getOutputStream()), true);
             BufferedReader reader1 = new BufferedReader(new InputStreamReader(socket1.getInputStream()))) {

            //login first client
            writer.println("HELLO~test");
            String s = reader.readLine();
            assertEquals("HELLO~Pentago 2D", s);
            writer.println("LOGIN~Test");
            s = reader.readLine();
            assertEquals("LOGIN", s);

            //login second client
            writer1.println("HELLO~test");
            s = reader1.readLine();
            assertEquals("HELLO~Pentago 2D", s);
            writer1.println("LOGIN~Test");
            s = reader1.readLine();
            assertEquals("ALREADYLOGGEDIN", s);
        }
    }

    @Test
    void testClientAndServerCorrectLogin() throws IOException {

        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer1 = new PrintWriter(new OutputStreamWriter(socket1.getOutputStream()), true);
             BufferedReader reader1 = new BufferedReader(new InputStreamReader(socket1.getInputStream()))) {

            //login first client
            writer.println("HELLO~test");
            String s = reader.readLine();
            assertEquals("HELLO~Pentago 2D", s);
            writer.println("LOGIN~Test");
            s = reader.readLine();
            assertEquals("LOGIN", s);

            //login second client
            writer1.println("HELLO~test");
            s = reader1.readLine();
            assertEquals("HELLO~Pentago 2D", s);
            writer1.println("LOGIN~Test1");
            s = reader1.readLine();
            assertEquals("LOGIN", s);
        }
    }

    @Test
    void testClientAndServerCorrectGame() throws IOException {

        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer1 = new PrintWriter(new OutputStreamWriter(socket1.getOutputStream()), true);
             BufferedReader reader1 = new BufferedReader(new InputStreamReader(socket1.getInputStream()))) {

            //login first client
            writer.println("HELLO~test");
            String s = reader.readLine();
            assertEquals("HELLO~Pentago 2D", s);
            writer.println("LOGIN~Test");
            s = reader.readLine();
            assertEquals("LOGIN", s);
            writer.println("QUEUE");

            //login second client
            writer1.println("HELLO~test");
            s = reader1.readLine();
            assertEquals("HELLO~Pentago 2D", s);
            writer1.println("LOGIN~Test1");
            s = reader1.readLine();
            assertEquals("LOGIN", s);
            writer1.println("QUEUE");

            s = reader.readLine();
            s = reader1.readLine();
            assertEquals("NEWGAME~Test~Test1", s);

            writer.println("MOVE~0~0");
            reader.readLine();
            reader1.readLine();
            writer1.println("MOVE~5~7");
            reader.readLine();
            reader1.readLine();
            writer.println("MOVE~0~7");
            reader.readLine();
            reader1.readLine();
            writer1.println("MOVE~17~7");
            reader.readLine();
            reader1.readLine();
            writer.println("MOVE~6~7");
            reader.readLine();
            reader1.readLine();
            writer1.println("MOVE~29~7");
            reader.readLine();
            reader1.readLine();
            writer.println("MOVE~18~7");
            reader.readLine();
            reader1.readLine();
            writer1.println("MOVE~10~7");
            reader.readLine();
            reader1.readLine();
            writer.println("MOVE~24~7");
            reader.readLine();
            reader1.readLine();

            s = reader.readLine();
            assertEquals(s, "GAMEOVER~VICTORY~Test");
            s = reader1.readLine();
            assertEquals(s, "GAMEOVER~VICTORY~Test");
        }
    }

}
