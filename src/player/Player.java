package player;

import board.Board;
import board.Ship;
import board.Space;
import main.Main;

import java.util.ArrayList;
import java.util.Locale;

public class Player {
	public static String HIT_FEEDBACK = "Hit!";
	public static String MISS_FEEDBACK = "Miss!";
	public static String SINK_FEEDBACK = "The opposing player's %s was sunk!";
	public static String TRY_AGAIN_FEEDBACK = "Sorry, we can't understand your input. Try again.";

	private final ArrayList<Ship> ships;
	private final Board board;

	public Player(Board board){
		this.board = board;
		ships = new ArrayList<>();
	}

	public Board getBoard(){
		return board;
	}

	public void setUpShips(){
		System.out.println("Place your ships on the battlefield.");
		System.out.println(board);
		addShip(Ship.CARRIER);
		addShip(Ship.BATTLESHIP);
		addShip(Ship.CRUISER);
		addShip(Ship.SUBMARINE);
		addShip(Ship.DESTROYER);
	}

	public void addShip(String name){
		System.out.printf("Choose where your %s should go.%n", name);
		int[] coords = takeSpaceInput();

		System.out.println("Should this be horizontal or vertical? (h/v, default is horizontal)");
		boolean horizontal = Main.SCN.nextLine().toLowerCase().charAt(0) != 'v';

		ships.add(new Ship(name, coords[0], coords[1], horizontal, board));
		System.out.println(board);
	}

	public String takeTurn(Player other){
		int[] coords = takeSpaceInput();

		if(coords[0] > Main.BOARD_SIZE || coords[1] > Main.BOARD_SIZE) return TRY_AGAIN_FEEDBACK;

		Space space = other.getBoard().get(coords[0], coords[1]);
		space.shoot();

		return space.hitString();
	}

	public int[] takeSpaceInput(){
		String input = Main.SCN.nextLine();
		if(input.length() < 3 || !Character.isDigit(input.charAt(2))) input = input.substring(0, 2);
		input = input.toLowerCase();

		int column = input.charAt(0) - 'a';
		int row = Integer.parseInt(input.substring(1)) - 1;

		return new int[]{column, row};
	}

	//Checks if a player has lost the game
	public boolean hasLost(){
		for(Ship ship : ships){
			if(!ship.sunk()) return false;
		}

		return true;
	}

}
