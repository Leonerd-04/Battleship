package board;

import com.sun.istack.internal.Nullable;
import player.Player;

public class Space {
	//Chars representing how Spaces are displayed on a game board
	public static char UNKNOWN = '-';
	public static char MISS = 'o';
	public static char HIT = '*';
	public static char SHIP_HORIZONTAL = '=';
	public static char SHIP_VERTICAL = '|';

	private boolean hit;
	@Nullable private Ship ship; //The ship occupying this space, null if the space is empty

	public Space(){
		hit = false;
	}

	public boolean isHit(){
		return hit;
	}

	//Shoots a space
	public void shoot(){
		this.hit = true;
	}

	//Called whenever a ship is placed on a space
	public void occupy(Ship ship){
		this.ship = ship;
	}

	public boolean isOccupied(){
		return this.ship != null;
	}

	//Checks if a ship is sunk; the isOccupied() call prevents a NullPointerException
	public boolean isSunk(){
		return isOccupied() && ship.sunk();
	}

	//Returns a char that decides how a space should be displayed on the board depending on its state
	public char display(boolean hide){
		if(!hit){
			if(hide || ship == null) return UNKNOWN;

			if(ship.isHorizontal()) return SHIP_HORIZONTAL;
			return SHIP_VERTICAL;
		}

		if(ship == null) return MISS;
		return HIT;
	}

	//Gives String feedback when a space is shot at
	public String hitString(){
		if(ship == null) return Player.MISS_FEEDBACK;
		if(!ship.sunk()) return Player.HIT_FEEDBACK;
		return String.format(Player.SINK_FEEDBACK, ship.getName());
	}
}
