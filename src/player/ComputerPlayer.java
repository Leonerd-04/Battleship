package player;

import board.Board;
import board.Space;
import main.Main;

import java.util.ArrayList;

public class ComputerPlayer extends Player{
	boolean shooting;
	ArrayList<int[]> shotSpaces;

	public ComputerPlayer(String name, Board board){
		super(name, board);
		shooting = false;
		shotSpaces = new ArrayList<>();
		print = false;
	}

	private ArrayList<int[]> searchForCriticalSpaces(Board board){
		ArrayList<int[]> result = new ArrayList<>();

		for(int[] space : shotSpaces){
			if(!isCritical(board.get(space))) continue;
			result.add(space);
		}

		return result;
	}

	private static boolean isCritical(Space space){
		return !space.isSunk() && space.isHit() && space.isOccupied();
	}

	private static boolean isCritical(int[] space, Board board){
		return !board.outOfBounds(space) && isCritical(board.get(space));
	}

	private int[] chooseRandomSpace(){
		return new int[]{(int) (Main.BOARD_SIZE * Math.random()), (int) (Main.BOARD_SIZE * Math.random())};
	}

	@Override
	public String takeTurn(Player other) throws InterruptedException{
		shooting = true;
		return super.takeTurn(other);
	}

	@Override
	public int[] takeSpaceInput(Board board){
		if(!shooting) return chooseRandomSpace();

		ArrayList<int[]> criticalSpaces = searchForCriticalSpaces(board);

		if(criticalSpaces.isEmpty()) return chooseRandomSpace();

		for(int[] space : criticalSpaces){
			int[] left = new int[]{space[0] - 1, space[1]};
			int[] right = new int[]{space[0] + 1, space[1]};
			int[] down = new int[]{space[0], space[1] - 1};
			int[] up = new int[]{space[0], space[1] + 1};

			if(isCritical(left, board)){
				if(!board.outOfBounds(right) && !board.get(right).isHit()) return right;
				else continue;
			}
			if(isCritical(right, board)){
				if(!board.outOfBounds(left) && !board.get(left).isHit()) return left;
				else continue;
			}
			if(isCritical(up, board)){
				if(!board.outOfBounds(down) && !board.get(down).isHit()) return up;
				else continue;
			}
			if(isCritical(down, board)){
				if(!board.outOfBounds(up) && !board.get(up).isHit()) return down;
				else continue;
			}

			if(!board.get(right).isHit()) return right;
			if(!board.get(left).isHit()) return left;
			if(!board.get(up).isHit()) return up;
			if(!board.get(down).isHit()) return down;
		}

		return chooseRandomSpace();
	}

	@Override
	public boolean takeBooleanInput(){
		return Math.random() > 0.5;
	}

	@Override
	protected void shoot(int[] coords, Board board){
		shotSpaces.add(coords);
		super.shoot(coords, board);
	}
}
