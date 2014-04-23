package world.time;

import world.WorldWindow;

public class Calendar implements Runnable{

	static {
		(new Thread(new Calendar())).start();
	}
	
	public static boolean stop = false;
	
	public static int min;
	public static int our;
	public static int day;
	public static int season;
	
	public void run(){
		while(!stop){
			min++;
			if(min == 32){
				min = 0;
				our++;
				if(our == 12){
					our = 0;
					day++;
					if(day == 12){
						day = 0;
						season++;
						if(season == 4){
							season = 0;
						}
					}
				}
			}
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void tick(){
		float step = 0.8f/32;
		if(our == 0){
			WorldWindow.lightLevel = 0.2f + (min*step);
		} else if(our == 6){
			WorldWindow.lightLevel = 1f - (min*step);
		} else if(our < 6){
			WorldWindow.lightLevel = 1;
		} else if(our > 6){
			WorldWindow.lightLevel = 0.2f;
		}
//		switch(our){
//		case 0: WorldWindow.lightLevel = 1; break;
//		
//		case 1: WorldWindow.lightLevel = 0.9f; break;
//		case 2: WorldWindow.lightLevel = 0.8f; break;
//		case 3: WorldWindow.lightLevel = 0.7f; break;
//		case 4: WorldWindow.lightLevel = 0.6f; break;
//		case 5: WorldWindow.lightLevel = 0.4f; break;
//		
//		case 6: WorldWindow.lightLevel = 0.1f; break;
//		
//		case 7: WorldWindow.lightLevel = 0.4f; break;
//		case 8: WorldWindow.lightLevel = 0.6f; break;
//		case 9: WorldWindow.lightLevel = 0.7f; break;
//		case 10: WorldWindow.lightLevel = 0.8f; break;
//		case 11: WorldWindow.lightLevel = 0.9f; break;
//		}
	}
	
}
