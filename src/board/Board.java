package board;

public class Board {
	private final Space[][] grid;

	public Board(int size){
		grid = new Space[size][size];

		//Fills the board's grid with new Spaces
		for(int i = 0; i < grid.length; i++){
			for(int j = 0; j < grid[i].length; j++){
				grid[i][j] = new Space();
			}
		}
	}

	//Gets an index in the grid
	public Space get(int x, int y) throws ArrayIndexOutOfBoundsException{
		return grid[y][x];
	}

	public Space get(int[] coords) throws ArrayIndexOutOfBoundsException{
		return grid[coords[1]][coords[0]];
	}

	public boolean outOfBounds(int[] coords){
		return coords[0] < 0 || coords[0] >= grid.length || coords[1] < 0 || coords[1] >= grid.length;
	}

	//Returns a string representing the board
	public String toString(boolean hide){
		StringBuilder str = new StringBuilder();
		str.append(letterRow()).append("\n");

		for(int i = 0; i < grid.length; i++){
			Space[] row = grid[i];
			str.append(String.format("%02d", i + 1)).append("  ");

			for(Space space : row){
				str.append(space.display(hide)).append("  ");
			}

			str.append("\n");
		}

		return str.toString();
	}

	//Returns a string representing a row of letters to be used to print the grid
	private String letterRow(){
		StringBuilder str = new StringBuilder();
		str.append("    ");

		for(int i = 0; i < grid.length; i++){
			str.append((char)('A' + i)).append("  ");
		}

		return str.toString();
	}

	@Override
	public String toString(){
		return toString(false);
	}
}