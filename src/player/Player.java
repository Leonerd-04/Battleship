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
	protected boolean print;

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

	public void setUpShips(){
		System.out.printf("%s, place your ships on the battlefield.%n", name);
		if(print) System.out.println(board);

		addShip(Ship.CARRIER);
		addShip(Ship.BATTLESHIP);
		addShip(Ship.CRUISER);
		addShip(Ship.SUBMARINE);
		addShip(Ship.DESTROYER);
	}

	//Just for debugging the ai
	public void autoSetUp(){
		ships.add(new Ship(Ship.CARRIER, 0, 0, true, board));
		ships.add(new Ship(Ship.CARRIER, 0, 1, true, board));
		ships.add(new Ship(Ship.CARRIER, 0, 2, true, board));
		ships.add(new Ship(Ship.CARRIER, 0, 3, true, board));
		ships.add(new Ship(Ship.CARRIER, 0, 4, true, board));
		ships.add(new Ship(Ship.CARRIER, 0, 5, true, board));
		ships.add(new Ship(Ship.CARRIER, 0, 6, true, board));
		ships.add(new Ship(Ship.CARRIER, 0, 7, true, board));
		ships.add(new Ship(Ship.CARRIER, 0, 8, true, board));
		ships.add(new Ship(Ship.CARRIER, 0, 9, true, board));
	}

	//Adds a ship to the list of player ships, while checking for its validity
	public void addShip(String shipName){
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

			//Orientation will only be vertical if the input string begins with 'v'.
			if(print) System.out.println("Should this be horizontal or vertical? (h/v, default is horizontal)");

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

	//Checks if a ship would intersect an existing ship
	private boolean wouldIntersect(int x, int y, int length, boolean horizontal){
		for(int i = 0; i < length; i++){
			Space space;

			if(horizontal) space = board.get(x + i, y);
			else space = board.get(x, y + i);

			if(space.isOccupied()) return true;
		}

		return false;
	}

	//Checks if a ship would fall out of bounds
	private boolean wouldBeOutOfBounds(int x, int y, int length, boolean horizontal){
		if(horizontal) return x + length >= Main.BOARD_SIZE;
		return y + length >= Main.BOARD_SIZE;
	}

	//Prompts the Player to take a shot at their opponent
	//Returns a feedback string
	public String takeTurn(Player other) throws InterruptedException{
		System.out.printf("%s's turn%n", name);
		Thread.sleep(300);
		System.out.println("Choose a space to shoot.");
		int[] coords;

		try{
			coords = takeSpaceInput(other.getBoard()); //Gets coordinates
		} catch(Exception e){
			e.printStackTrace();
			return TRY_AGAIN_FEEDBACK;
		}

		//Checks if they are usable
		if(other.getBoard().outOfBounds(coords)) return TRY_AGAIN_FEEDBACK;
		if(other.getBoard().get(coords).isHit()) return ALREADY_HIT_FEEDBACK;

		//Shoots the space on the board that was chosen
		Space space = other.getBoard().get(coords);
		shoot(coords, other.getBoard());

		//Returns a feedback string
		//ie. "Hit!", "Miss!", "<Player> shot <Player2>'s <ship>"
		return space.hitString();
	}

	//Prompts the user for coordinates and interprets the results
	public int[] takeSpaceInput(Board board) throws NumberFormatException{
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

	public boolean takeBooleanInput() throws StringIndexOutOfBoundsException{
		return Main.SCN.nextLine().toLowerCase().charAt(0) != 'v';
	}

	//Checks if a player has lost the game
	public boolean hasLost(){
		for(Ship ship : ships){
			if(!ship.sunk()) return false;
		}

		return true;
	}

	protected void shoot(int[] coords, Board board){
		board.get(coords).shoot();
	}
}
