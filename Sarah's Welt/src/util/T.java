package util;

public class T {

	static long time;
	static int sum;
	static int count;
	
	public static void start______________________________O(){
		time = System.nanoTime();
	}
	
	/**
	 * in 
	 * @return
	 */
	public static int stop(){
		int ret = (int) (System.nanoTime() - time);
		count++;
		sum += ret;
		return ret;
	}
	
	public static void print______________________________O(){
		System.out.println(stop()/1000);
	}
	
	public static void resetAverage(){
		count = 0;
		sum = 0;
	}
	
	public static int getAverage(){
		return sum/count;
	}
	
}
