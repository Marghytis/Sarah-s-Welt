package core;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import util.T;
import world.WorldWindow;
import world.time.Calendar;

public class Main {

	public static void main(String[] args){
		Window.createSplash("Titel.png", 600, 400);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Window.createFullScreen("Sarahs Welt");
		WorldWindow.load("TestWelt");

		long timeLastWorldTick = System.currentTimeMillis();
		while(!Display.isCloseRequested() && !beenden){
			Display.sync(60);

			GL11.glClearColor(0.5f, 0.5f, 1f, 1);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			render();
			
			listening();
			int delta = (int)(System.currentTimeMillis() - timeLastWorldTick);
			timeLastWorldTick += delta;
			calculate(delta);
			Display.update();
		}
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
