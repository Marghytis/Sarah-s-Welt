package world.time;

import world.WorldWindow;

public class Calendar{
	
	public static boolean stop = false;
	
	public static int min;
	public static int our;
	public static int day;
	public static int sea;

	static float lightLevelStep = 0.8f/32;
	
	public static void tick(int delta){
		min += delta/10;
			our += min/32;
				day += our/12;
					sea += day/12;
		min %= 32;
		our %= 12;
		day %= 12;
		sea %= 4;
		
		if(our == 0){
			WorldWindow.lightLevel = 0.2f + (min*lightLevelStep);
		} else if(our < 6){
			WorldWindow.lightLevel = 1;
		} else if(our == 6){
			WorldWindow.lightLevel = 1f - (min*lightLevelStep);
		} else if(our > 6){
			WorldWindow.lightLevel = 0.2f;
		}
	}
	
	public static String getTime(){
		return our + ":" + min + " O' clock, day " + day + " of the " + sea + "th season";
	}
	
}
