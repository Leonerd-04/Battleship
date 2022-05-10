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

		Ship ship = new Ship("Carrier", 0, 0, 4, true, BOARD2);
		Ship ship2 = new Ship("Submarine", 3, 4, 3, true, BOARD2);

		while(true){
			System.out.print(BOARD2.toString(true));
			String feedback = player.takeTurn(player2);
			System.out.println(feedback);
		}
	}

	public static void test(){
		BOARD1 = new Board(BOARD_SIZE);
		BOARD2 = new Board(BOARD_SIZE);
		Ship ship = new Ship("Carrier", 0, 0, 4, true, BOARD1);
		Ship ship2 = new Ship("Carrier", 0, 1, 2, false, BOARD1);
		Ship ship3 = new Ship("Carrier", 5, 6, 3, false, BOARD1);

		BOARD1.get(1, 0).shoot();
		BOARD1.get(0, 1).shoot();
		BOARD1.get(3, 4).shoot();
		BOARD1.get(9, 9).shoot();

		System.out.print(BOARD1);
	}


	//Prints with a scrolling delay effect
	public static void printScroll(String str, long msDelay) throws InterruptedException{
		for(char c : str.toCharArray()){
			System.out.print(c);
			if(c != ' ') Thread.sleep(msDelay);
		}
	}

}
