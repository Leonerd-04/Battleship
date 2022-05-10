package player;

import board.Board;
import board.Ship;
import board.Space;
import main.Main;

import java.util.ArrayList;

public class Player {
	public static String HIT_FEEDBACK = "Hit!";
	public static String MISS_FEEDBACK = "Miss!";
	public static String SINK_FEEDBACK = "The opposing player's %s was sunk!";
	public static String TRY_AGAIN_FEEDBACK = "Sorry, we can't understand your input. Try again.";

	private ArrayList<Ship> ships;
	private Board board;

	public Player(Board board){
		this.board = board;
	}

	public Board getBoard(){
		return board;
	}

	public String takeTurn(Player other){
		String input = takeInput().toLowerCase();
		int column = input.charAt(0) - 'a';
		int row = Integer.parseInt(input.substring(1)) - 1;
		if(column > Main.BOARD_SIZE || row > Main.BOARD_SIZE) return TRY_AGAIN_FEEDBACK;

		Space space = other.getBoard().get(row, column);
		space.shoot();

		return space.hitString();
	}

	public String takeInput(){
		String input = Main.SCN.nextLine();
		if(input.length() < 3 || !Character.isDigit(input.charAt(2))) input = input.substring(0, 2);

		return input;
	}

	//Checks if a player has lost the game
	public boolean hasLost(){
		for(Ship ship : ships){
			if(!ship.sunk()) return false;
		}

		return true;
	}

}
