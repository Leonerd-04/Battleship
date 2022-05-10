
public class Main {
	public static int BOARD_SIZE = 10;
	static Board BOARD1;
	static Board BOARD2;

	public static void main(String[] args){
		BOARD1 = new Board(BOARD_SIZE);
		BOARD2 = new Board(BOARD_SIZE);
		Ship ship = new Ship(0, 0, 4, true, BOARD1);
		Ship ship2 = new Ship(1, 0, 2, false, BOARD1);

		System.out.print(BOARD1);
	}


	//Prints with a scrolling delay effect
	public static void printScroll(String str, long msDelay) throws InterruptedException{
		for(char c : str.toCharArray()){
			System.out.print(c);
			if(c != ' ') Thread.sleep(msDelay);
		}
	}

}
