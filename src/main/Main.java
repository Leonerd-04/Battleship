package main;

import board.Board;
import player.ComputerPlayer;
import player.Player;

import java.util.Scanner;

public class Main {
	public static String WIN = "%s has won!";
	public static Scanner SCN = new Scanner(System.in);

	public static void main(String[] args) throws InterruptedException{
		System.out.println("Welcome to Battleship!");
		boolean keepPlaying = true;

		while(keepPlaying){
			System.out.println("Choose your board size (4 - 15, default 10):");
			int size = Math.min(Math.max(SCN.nextInt(), 4), 15);

			System.out.println("Choose the number of ships on the field (2 - 5, default 4):");
			int numShips = Math.min(Math.max(SCN.nextInt(), 2), 5);

			SCN.nextLine();
			playGame(size, numShips);

			System.out.println("Would you like to play again? (y/n, default is y)");

			try{
				keepPlaying = SCN.nextLine().charAt(0) != 'n';
			} catch(Exception ignored){

			}

		}

		System.out.println("Thanks for playing!");

	}

	static void playGame(int boardSize, int numShips) throws InterruptedException{
		Board playerBoard = new Board(boardSize);
		Board compBoard = new Board(boardSize);
		Player player = new Player("Player", playerBoard);
		ComputerPlayer computer = new ComputerPlayer("Computer", compBoard);

		//Prompts both players to set up their ships
		player.setUpShips(numShips);
		computer.setUpShips(numShips);
		String feedback;

		while(true){
			do {
				System.out.println("Your Board:");
				System.out.println(player.getBoard());
				System.out.println("Computer's Board:");
				System.out.println(computer.getBoard().toString(true));

				feedback = player.takeTurn(computer);
				Thread.sleep(300);
				System.out.println(feedback);
				Thread.sleep(2000);

			} while(feedback.equals(Player.TRY_AGAIN_FEEDBACK) || feedback.equals(Player.ALREADY_HIT_FEEDBACK));


			if(computer.hasLost()){
				System.out.printf(WIN, player.getName());
				System.out.println();

				System.out.println("Your Board:");
				System.out.println(player.getBoard());
				System.out.println("Computer's Board:");
				System.out.println(computer.getBoard());

				return;
			}

			System.out.printf("%s's turn%n", computer.getName());
			do {
				feedback = computer.takeTurn(player);
			} while(feedback.equals(Player.TRY_AGAIN_FEEDBACK) || feedback.equals(Player.ALREADY_HIT_FEEDBACK));

			Thread.sleep(1500);
			System.out.println(feedback);
			Thread.sleep(1200);

			if(player.hasLost()){
				System.out.printf(WIN, computer.getName());
				System.out.println();

				System.out.println("Your Board:");
				System.out.println(player.getBoard());
				System.out.println("Computer's Board:");
				System.out.println(computer.getBoard());

				return;
			}
		}
	}
}
