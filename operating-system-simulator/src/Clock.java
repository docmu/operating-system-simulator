
public class Clock {

	private static int count = 0;
	
	public static void reset() {
		count = 0;
	}
	
	public static void setClock(int num) {
		count = num;
	}
	
	public static int getClock() {
		return count;
	}
	
	public static void count(){
		count++;
	}
	
}
