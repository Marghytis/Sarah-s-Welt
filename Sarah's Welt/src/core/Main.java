package core;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import world.WorldWindow;
import world.time.Calendar;

public class Main {

	public static void main(String[] args){
		Window.createSplash("Titel.png", 600, 400);

		WorldWindow.load("TestWelt");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Window.create("Sarahs Welt", 1000, 500);

		long timeLastWorldTick = System.currentTimeMillis();
		while(!Display.isCloseRequested() && !beenden){
			Display.sync(60);
			
			int delta = (int)(System.currentTimeMillis() - timeLastWorldTick);
			timeLastWorldTick += delta;
			
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glClearColor(0.5f, 0.5f, 1, 1);
			render();
			Display.update();
			
			calculate(delta);
		}
	}
	
	public static boolean beenden;
	
	public static void render(){

		WorldWindow.render();
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
