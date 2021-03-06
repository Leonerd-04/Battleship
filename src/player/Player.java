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
	public static String ALREADY_HIT_FEEDBACK = "This space was previously hit. Choose another space.";

	protected final String name;
	protected final ArrayList<Ship> ships;
	protected final Board board;
	protected boolean print; //Specifies whether to print prompts; true for Players, false for ComputerPlayers

	public Player(String name, Board board){
		this.name = name;
		this.board = board;
		this.print = true;
		ships = new ArrayList<>();
	}

	public String getName(){
		return name;
	}

	public Board getBoard(){
		return board;
	}

	//Prompts the user to place their ships on their board
	public void setUpShips(int n){
		System.out.printf("%s, place your ships on the battlefield.%n", name);
		if(print) System.out.println(board);

		for(int i = Math.min(Ship.SHIP_NAMES.length, n) - 1; i > -1; i--){
			addShip(Ship.SHIP_NAMES[i]);
		}
	}

	//Adds a ship to the list of player ships, while checking for its validity
	private void addShip(String shipName){
		int[] coords;
		boolean horizontal;

		if(print) System.out.printf("Choose where your %s should go.%n", shipName);

		while(true){
			try{
				coords = takeSpaceInput(board); //Gets coordinates from user
			} catch(Exception e){
				if(print) System.out.println("Input could not be read, please try again");
				continue;
			}

			if(print) System.out.println("Should this be horizontal or vertical? (h/v, default is horizontal)");

			//Orientation will only be vertical if the input string begins with 'v'.
			try{
				horizontal = takeBooleanInput();
			} catch(Exception e){
				horizontal = true;
			}

			//Checks if the ship would fall out of bounds and reprompts the user if it does.
			if(wouldBeOutOfBounds(coords[0], coords[1], Ship.lengthFromName(shipName), horizontal)){
				if(print) System.out.println("This ship would fall out of bounds. Choose a different location or orientation.");
				continue;
			}

			//Checks if the ship would intersect another and reprompts the user if it does.
			if(wouldIntersect(coords[0], coords[1], Ship.lengthFromName(shipName), horizontal)){
				if(print) System.out.println("This ship would intersect another ship. Choose a different location or orientation.");
				continue;
			}

			break;
		}

		ships.add(new Ship(shipName, coords[0], coords[1], horizontal, board));
		if(print) System.out.print(board);
	}

	//Checks if a ship would intersect an existing ship. Used to prevent overlap when placing ships on the board
	private boolean wouldIntersect(int x, int y, int length, boolean horizontal){
		for(int i = 0; i < length; i++){
			Space space;

			if(horizontal) space = board.get(x + i, y);
			else space = board.get(x, y + i);

			if(space.isOccupied()) return true;
		}

		return false;
	}

	//Checks if a ship would fall out of bounds before it's placed
	private boolean wouldBeOutOfBounds(int x, int y, int length, boolean horizontal){
		if(horizontal) return x + length > board.getLength();
		return y + length > board.getLength();
	}

	//Prompts the player to take a shot at their opponent
	//Returns a feedback string
	public String takeTurn(Player other) throws InterruptedException{
		if(print) System.out.printf("%s's turn%n", name);
		Thread.sleep(300);
		if(print) System.out.println("Choose a space to shoot.");
		int[] coords;

		try{
			coords = takeSpaceInput(other.getBoard()); //Gets coordinates
		} catch(Exception e){
			return TRY_AGAIN_FEEDBACK;
		}

		//Checks if they are usable
		if(other.getBoard().outOfBounds(coords)) return TRY_AGAIN_FEEDBACK;
		if(other.getBoard().get(coords).isHit()) return ALREADY_HIT_FEEDBACK;

		//Shoots the space on the board that was chosen
		Space space = other.getBoard().get(coords);
		shoot(coords, other.getBoard());
		if(!print) System.out.printf("%s has shot %s%n", name, coordString(coords));

		//Returns a feedback string
		//ie. "Hit!", "Miss!", "<Player> shot <Player2>'s <ship>"
		return space.hitString();
	}

	//Prompts the player for coordinates and interprets the results
	protected int[] takeSpaceInput(Board board) throws NumberFormatException{
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

	//Prompts the player to specify whether they want a horizontal or vertical ship
	//horizontal -> true, vertical -> false
	protected boolean takeBooleanInput() throws StringIndexOutOfBoundsException{
		return Main.SCN.nextLine().toLowerCase().charAt(0) != 'v';
	}

	protected void shoot(int[] coords, Board board){
		board.get(coords).shoot();
	}

	//Turns a coordinate array back into a letter/number pair
	//ie (2, 7) -> "C8", (0, 9) -> "A10"
	protected String coordString(int[] coords){
		char col = (char)('A' + coords[0]);
		int row = coords[1] + 1;

		return col + "" + row;
	}

	//Checks if a player has lost the game
	public boolean hasLost(){
		for(Ship ship : ships){
			if(!ship.sunk()) return false;
		}

		return true;
	}
}
