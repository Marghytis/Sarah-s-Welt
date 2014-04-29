package core;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import resources.Res;
import resources.Texture;
import world.Calendar;
import world.WorldWindow;

public class Main {

	public static void main(String[] args){
		
		Window.create("Sarahs Welt", 1000, 300);
//		Window.createFullScreen("Sarahs Welt");
		Window.fill(new Texture("titelbild", 0, 0).handle);
		Display.update();
		Res.load();
		WorldWindow.load("TestWelt");
		long endTime = System.currentTimeMillis() + 20000;
		int counter = 0;
		int heapCounter = 0;
		int max = 0;
		int sum = 0;
		
		long timeLastWorldTick = System.currentTimeMillis();
		while(timeLastWorldTick < endTime && !Display.isCloseRequested() && !beenden){
			long testTime = System.currentTimeMillis();
//			Display.sync(1000);
			
			if(Settings.sound && !Res.test.playing) Res.test.play();

			render();
			
			listening();
			long t = System.currentTimeMillis();
			calculate((int)(t - timeLastWorldTick));
			timeLastWorldTick = t;
			
			int delta = (int)(System.currentTimeMillis() - testTime);
			 if(delta > 17){
				 System.out.print("-----");
				 heapCounter++;
				 if(counter != 0)max = Math.max(delta, max);
			 } System.out.println(delta);
			 counter++;
			 if(counter != 1)sum += delta;
			Display.update();
		}
		
		System.out.println("\nExtremwerte: " + heapCounter + "  Durchläufe: " + counter + "  Maximum: " + max + "  Average: " + (sum/counter));

		Res.test.stop();
		Res.unload();
		Display.destroy();
	}
	
	public static boolean beenden;
	
	public static void render(){
		GL11.glLoadIdentity();
		GL11.glColor4f(1, 1, 1, 1);
		WorldWindow.render();
		GL11.glLoadIdentity();
		Menu.render();
	}
	
	public static void calculate(int delta){
		
		if(!Menu.pauseWorld()){
			WorldWindow.tick(delta);
			Calendar.tick(delta);
		}
	}

	public static void listening(){
		if(Menu.pauseWorld()){
			Menu.keyListening();
		} else {
			WorldWindow.keyListening();
		}//extra
		if(Menu.pauseWorld()){
			Menu.mouseListening();
		} else {
			WorldWindow.mouseListening();
		}
	}	
}
