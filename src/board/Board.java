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
	public Space get(int x, int y){
		return grid[y][x];
	}

	//Returns a string representing the board
	public String toString(boolean hide){
		StringBuilder str = new StringBuilder();

		for(Space[] row : grid){
			for(Space space : row){
				str.append(space.display(hide)).append(' ');
			}
			str.append("\n");
		}

		return str.toString();
	}

	@Override
	public String toString(){
		return toString(false);
	}
}
