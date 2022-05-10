import com.sun.istack.internal.Nullable;

public class Space {
	public static char UNKNOWN = '-';
	public static char MISS = 'o';
	public static char HIT = '*';
	public static char SHIP_HORIZONTAL = '=';
	public static char SHIP_VERTICAL = '|';

	private boolean hit;
	@Nullable private Ship ship;

	public Space(){
		hit = false;
	}

	public boolean isHit(){
		return hit;
	}

	public void shootThis(){
		hit = true;

	}

	public void occupy(Ship ship){
		this.ship = ship;
	}

	public char display(boolean hide){
		if(!hit){
			if(hide | ship == null) return UNKNOWN;

			if(ship.isHorizontal()) return SHIP_HORIZONTAL;
			return SHIP_VERTICAL;
		}

		if(ship == null) return MISS;
		return HIT;

	}


}
