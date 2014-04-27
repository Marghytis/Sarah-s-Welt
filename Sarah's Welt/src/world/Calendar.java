package world;

import core.Settings;

public class Calendar{
	
	public static boolean stop = false;
	
	public static int sec;
	public static int min;
	public static int our;
	public static int day;
	public static int sea;
	public static int yea;

	static int secsPerMin = 100;//seconds are actually milliseconds
	static int minsPerOur = 100;
	static int oursPerDay = 10;
	static int daysPerSea = 12;
	static int seasPerYea = 4;

	static float lightLevelStep = 0.9f/minsPerOur;
	
	public static void tick(int delta){
		if(Settings.time){
			sec += delta;
				min += sec/secsPerMin;
					our += min/minsPerOur;
						day += our/oursPerDay;
							sea += day/daysPerSea;
								yea += sea/seasPerYea;
			sec %= secsPerMin;
			min %= minsPerOur;
			our %= oursPerDay;
			day %= daysPerSea;
			sea %= seasPerYea;
		}
		
		if(our == 0){
			WorldWindow.lightLevel = 0.1f + (min*lightLevelStep);
		} else if(our < oursPerDay/2){
			WorldWindow.lightLevel = 1;
		} else if(our == oursPerDay/2){
			WorldWindow.lightLevel = 1f - (min*lightLevelStep);
		} else if(our > oursPerDay/2){
			WorldWindow.lightLevel = 0.1f;
		}
	}
	
	public static void setMorning(){
		min = 0;
		our = 0;
	}
	
	public static void setEvening(){
		min = 0;
		our = oursPerDay/2;
	}
	
	public static String getTime(){
		return our + ":" + min + " O' clock, day " + day + " of the " + sea + "th season";
	}
	
}
