package main;

import board.Board;
import player.ComputerPlayer;
import player.Player;

import java.util.Scanner;

public class Main {
	public static String WIN = "%s has won!";
	public static int BOARD_SIZE = 10;
	static Board BOARD1;
	static Board BOARD2;
	public static Scanner SCN = new Scanner(System.in);

	public static void main(String[] args) throws InterruptedException{
		BOARD1 = new Board(BOARD_SIZE);
		BOARD2 = new Board(BOARD_SIZE);
		Player player = new Player("Player", BOARD1);
		ComputerPlayer computer = new ComputerPlayer("Computer", BOARD2);

		//player.setUpShips();
		player.autoSetUp();
		computer.setUpShips();
		String feedback;

		while(true){
			System.out.println("Your Board:");
			System.out.println(player.getBoard());
			Thread.sleep(2500);
			/*
			do {
				System.out.println("Your Board:");
				System.out.println(player.getBoard());
				System.out.println("Computer's Board:");
				System.out.println(computer.getBoard());

				feedback = player.takeTurn(computer);
				Thread.sleep(300);
				System.out.println(feedback);
				Thread.sleep(2000);

			} while(feedback.equals(Player.TRY_AGAIN_FEEDBACK) || feedback.equals(Player.ALREADY_HIT_FEEDBACK));
			*/

			if(computer.hasLost()){
				System.out.printf(WIN, player.getName());
				return;
			}

			do {
				feedback = computer.takeTurn(player);
				System.out.println(feedback);
				//Thread.sleep(2500);
			} while(feedback.equals(Player.TRY_AGAIN_FEEDBACK) || feedback.equals(Player.ALREADY_HIT_FEEDBACK));

			if(player.hasLost()){
				System.out.printf(WIN, computer.getName());
				return;
			}
		}
	}


	//Prints with a scrolling delay effect
	public static void printScroll(String str, long msDelay) throws InterruptedException{
		for(char c : str.toCharArray()){
			System.out.print(c);
			if(c != ' ') Thread.sleep(msDelay);
		}
	}
}
