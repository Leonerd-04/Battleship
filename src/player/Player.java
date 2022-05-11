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

	private final String name;
	private final ArrayList<Ship> ships;
	private final Board board;

	public Player(String name, Board board){
		this.name = name;
		this.board = board;
		ships = new ArrayList<>();
	}

	public Board getBoard(){
		return board;
	}

	public void setUpShips(){
		System.out.printf("%s, place your ships on the battlefield.%n", name);
		System.out.println(board);
		addShip(Ship.CARRIER);
		addShip(Ship.BATTLESHIP);
		addShip(Ship.CRUISER);
		addShip(Ship.SUBMARINE);
		addShip(Ship.DESTROYER);
	}

	public void addShip(String name){
		int[] coords;
		boolean horizontal;

		System.out.printf("Choose where your %s should go.%n", name);

		while(true){
			coords = takeSpaceInput(); //Gets coordinates from user

			//Orientation will only be vertical if the input string begins with 'v'.
			System.out.println("Should this be horizontal or vertical? (h/v, default is horizontal)");
			horizontal = Main.SCN.nextLine().toLowerCase().charAt(0) != 'v';

			if(!wouldIntersect(coords[0], coords[1], Ship.lengthFromName(name), horizontal)) break;
			System.out.println("This ship would intersect another ship. Choose a different location or orientation.");
		}

		ships.add(new Ship(name, coords[0], coords[1], horizontal, board));
		System.out.print(board);
	}

	private boolean wouldIntersect(int x, int y, int length, boolean horizontal){
		for(int i = 0; i < length; i++){
			Space space;

			if(horizontal) space = board.get(x + i, y);
			else space = board.get(x, y + i);

			if(space.isOccupied()) return true;
		}

		return false;
	}

	//Prompts the Player to take a shot at their opponent
	//Returns a feedback string
	public String takeTurn(Player other){
		System.out.printf("%s's turn%n", name);
		System.out.println("Choose a space to shoot.");

		int[] coords = takeSpaceInput(); //Gets coordinates

		//Checks if they are usable
		if(coords[0] > Main.BOARD_SIZE || coords[1] > Main.BOARD_SIZE) return TRY_AGAIN_FEEDBACK;

		//Shoots the space on the board that was chosen
		Space space = other.getBoard().get(coords[0], coords[1]);
		space.shoot();

		//Returns a feedback string
		//ie. "Hit!", "Miss!", "<Player> shot <Player2>'s <ship>"
		return space.hitString();
	}

	//Prompts the user for coordinates and interprets the results
	public int[] takeSpaceInput(){
		String input = Main.SCN.nextLine(); //Raw input
		if(input.length() < 3 || !Character.isDigit(input.charAt(2))) input = input.substring(0, 2); //Trims it
		input = input.toLowerCase(); //Lowercase for simplicity

		//Gives the index of the letter in the alphabet minus 1
		//ie 'b' -> 1, 'e' -> 4, 'h' -> 7, etc
		int column = input.charAt(0) - 'a';

		//Parses for a number to use for the row
		int row = Integer.parseInt(input.substring(1)) - 1;

		//Returns the coordinates as an array
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
