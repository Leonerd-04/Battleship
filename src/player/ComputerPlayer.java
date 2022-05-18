package player;

import board.Board;
import board.Space;
import main.Main;

import java.util.ArrayList;

public class ComputerPlayer extends Player{
	boolean shooting; //Tracks whether ships are being set up or whether the game is being played
	ArrayList<int[]> shotSpaces; //Tracks spaces that've been shot (on the opponent board)

	public ComputerPlayer(String name, Board board){
		super(name, board);
		shooting = false;
		shotSpaces = new ArrayList<>();
		print = false;
	}

	//Searches for spaces that have been hit but not sunk
	private ArrayList<int[]> searchForCriticalSpaces(Board board){
		ArrayList<int[]> result = new ArrayList<>();

		for(int[] space : shotSpaces){
			if(!isCritical(board.get(space))) continue;
			result.add(space);
		}

		return result;
	}

	//Checks if a single space is critical
	private static boolean isCritical(Space space){
		return !space.isSunk() && space.isHit() && space.isOccupied();
	}

	//Checks if a single space is critical and checks out of bound conditions
	private static boolean isCritical(int[] space, Board board){
		return !board.outOfBounds(space) && isCritical(board.get(space));
	}

	//Picks a random space on a board as an array of integers
	private int[] chooseRandomSpace(){
		return new int[]{(int) (Main.BOARD_SIZE * Math.random()), (int) (Main.BOARD_SIZE * Math.random())};
	}

	//Overrides takeTurn() to tell the ComputerPlayer that the game has started
	@Override
	public String takeTurn(Player other) throws InterruptedException{
		shooting = true;
		return super.takeTurn(other);
	}

	//Overrides takeSpaceInput to use an ai instead of user input
	@Override
	public int[] takeSpaceInput(Board board){
		if(!shooting) return chooseRandomSpace(); //If the game hasn't started, the computer places ships randomly.

		ArrayList<int[]> criticalSpaces = searchForCriticalSpaces(board);

		if(criticalSpaces.isEmpty()) return chooseRandomSpace();

		for(int[] space : criticalSpaces){
			//Checks the spaces next to a critical space
			int[] left = new int[]{space[0] - 1, space[1]};
			int[] right = new int[]{space[0] + 1, space[1]};
			int[] down = new int[]{space[0], space[1] - 1};
			int[] up = new int[]{space[0], space[1] + 1};

			//Checks for situations like:
			//- - -
			//x x - <-
			//- - -
			//Wherein the space pointed to would be a good option to shoot
			if(isCritical(left, board)){
				if(!board.outOfBounds(right) && !board.get(right).isHit()) return right;
				else continue;
			}
			if(isCritical(right, board)){
				if(!board.outOfBounds(left) && !board.get(left).isHit()) return left;
				else continue;
			}
			if(isCritical(up, board)){
				if(!board.outOfBounds(down) && !board.get(down).isHit()) return down;
				else continue;
			}
			if(isCritical(down, board)){
				if(!board.outOfBounds(up) && !board.get(up).isHit()) return up;
				else continue;
			}

			//Just picks any space that hasn't already been hit if the critical space is by itself
			if(!board.get(right).isHit()) return right;
			if(!board.get(left).isHit()) return left;
			if(!board.get(up).isHit()) return up;
			if(!board.get(down).isHit()) return down;
		}

		return chooseRandomSpace();
	}

	//Chooses a random boolean; used for ship orientation only
	@Override
	public boolean takeBooleanInput(){
		return Math.random() > 0.5;
	}

	//Overrides shoot() to also add the space to the ArrayList tracked by a ComputerPlayer
	@Override
	protected void shoot(int[] coords, Board board){
		shotSpaces.add(coords);
		super.shoot(coords, board);
	}
}
