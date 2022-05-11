package main;

import board.Board;
import board.Ship;
import player.Player;

import java.util.Scanner;

public class Main {
	public static int BOARD_SIZE = 10;
	static Board BOARD1;
	static Board BOARD2;
	public static Scanner SCN = new Scanner(System.in);

	public static void main(String[] args){
		BOARD1 = new Board(BOARD_SIZE);
		BOARD2 = new Board(BOARD_SIZE);
		Player player = new Player(BOARD1);
		Player player2 = new Player(BOARD2);

		player.setUpShips();
	}

	public static void test(){
		BOARD1 = new Board(BOARD_SIZE);
		BOARD2 = new Board(BOARD_SIZE);
		Player player = new Player(BOARD1);
		Player player2 = new Player(BOARD2);

		Ship ship = new Ship(Ship.BATTLESHIP, 0, 0, true, BOARD2);
		Ship ship2 = new Ship(Ship.SUBMARINE, 3, 4, true, BOARD2);

		while(true){
			System.out.print(BOARD2.toString(true));
			String feedback = player.takeTurn(player2);
			System.out.println(feedback);
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
