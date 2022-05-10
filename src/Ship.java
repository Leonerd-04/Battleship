public class Ship {
	private int length;
	private boolean horizontal;
	private int x;
	private int y;
	private Space[] spaces;

	public Ship(int x, int y, int length, boolean horizontal, Board board) throws ArrayIndexOutOfBoundsException{
		this.length = length;
		this.horizontal = horizontal;
		spaces = new Space[length];

		for(int i = 0; i < length; i++){
			if(horizontal) spaces[i] = board.get(x, y + i);
			else spaces[i] = board.get(x + i, y);
		}

		for(Space space : spaces){
			space.occupy(this);
		}
	}

	public boolean isHorizontal(){
		return horizontal;
	}

	public boolean sunk(){
		for(Space space : spaces){
			if(!space.isHit()) return false;
		}

		return true;
	}
}
