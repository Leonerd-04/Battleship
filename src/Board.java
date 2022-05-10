public class Board {
	private Space[][] grid;

	public Board(int size){
		grid = new Space[size][size];

		for(int i = 0; i < grid.length; i++){
			for(int j = 0; j < grid[i].length; j++){
				grid[i][j] = new Space();
			}
		}
	}

	public Space get(int x, int y){
		return grid[x][y];
	}

	public String toString(boolean hide){
		String str = "";

		for(Space[] row : grid){
			for(Space space : row){
				str = str + space.display(hide) + ' ';
			}
			str = str + "\n";
		}

		return str;
	}

	public String toString(){
		return toString(false);
	}
}
