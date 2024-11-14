package Pentago;

import Game.Game;
import Game.Mark;
import Player.AbstractPlayer;
import Player.BotPlayer;
import Player.HumanPlayer;
import Strategy.Strategy;
import Strategy.AlphaPentaGo;
import Strategy.RandomBot;

import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Pentago is the class that executes the methods in the class Game. It first gets inputs from the users, namely which names
 * they want to use. After this, a new game is started.
 */
public class Pentago {

    public static void main(String[] args) {

        AbstractPlayer player1;
        AbstractPlayer player2;
        String randomBotName = "-R";
        String alphaPentaGoName = "-A";

        Strategy randomBot = new RandomBot();
        Strategy alphaPentaGo = new AlphaPentaGo(5);

        Scanner scanner = new Scanner(new InputStreamReader(System.in));

        if (args.length == 2) {
            if (args[0].equals(randomBotName)) {
                player1 = new BotPlayer(Mark.XX, randomBot);
            } else if (args[0].equals(alphaPentaGoName)) {
                player1 = new BotPlayer(Mark.XX, alphaPentaGo);
            } else {
                player1 = new HumanPlayer(args[0], Mark.XX);
            }
            if (args[1].equals(randomBotName)) {
                player2 = new BotPlayer(Mark.OO, randomBot);
            } else if (args[1].equals(alphaPentaGoName)) {
                player2 = new BotPlayer(Mark.OO, alphaPentaGo);
            } else {
                player2 = new HumanPlayer(args[1], Mark.OO);
            }
        } else {
            System.out.println("What is the name of player 1?");
            String namePlayer1 = scanner.nextLine();
            System.out.println("What is the name of player 2?");
            String namePlayer2 = scanner.nextLine();
            if (namePlayer1.equals(randomBotName)) {
                player1 = new BotPlayer(Mark.XX, randomBot);
            } else if (namePlayer1.equals(alphaPentaGoName)) {
                player1 = new BotPlayer(Mark.XX, alphaPentaGo);
            } else {
                player1 = new HumanPlayer(namePlayer1, Mark.XX);
            }
            if (namePlayer2.equals(randomBotName)) {
                player2 = new BotPlayer(Mark.OO, randomBot);
            } else if (namePlayer2.equals(alphaPentaGoName)) {
                player2 = new BotPlayer(Mark.OO, alphaPentaGo);
            } else {
                player2 = new HumanPlayer(namePlayer2, Mark.OO);
            }
        }

        Game game = new Game(player1, player2);
        game.start();
    }

}
