package board;

public class Ship {
	private final String name;
	private final boolean horizontal;
	private final Space[] spaces; //The spaces occupied by a ship

	public Ship(String name, int x, int y, int length, boolean horizontal, Board board) throws ArrayIndexOutOfBoundsException{
		this.name = name;
		this.horizontal = horizontal;
		spaces = new Space[length];

		//Sets up the spaces array depending on position and whether the ship is horizontal
		for(int i = 0; i < length; i++){
			if(horizontal) spaces[i] = board.get(x + i, y);
			else spaces[i] = board.get(x, y + i);
		}

		//Tells the spaces that they are occupied by a ship
		for(Space space : spaces){
			space.occupy(this);
		}
	}

	public String getName(){
		return name;
	}


	public boolean isHorizontal(){
		return horizontal;
	}

	//Checks if a ship has been sunk
	public boolean sunk(){
		for(Space space : spaces){
			if(!space.isHit()) return false;
		}

		return true;
	}
}
