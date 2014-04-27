package core;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import resources.Texture;
import world.WorldWindow;
import world.time.Calendar;
import core.geom.Quad;

public class Main {

	public static void main(String[] args){
//		Window.createSplash("Titel.png", 600, 400);
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		int titelTex = Window.createTexture("Titel.png");
		
		Window.createFullScreen("Sarahs Welt");
		new Quad(Window.WIDTH/2 - 600, Window.HEIGHT/2 - 400, 1200, 800).drawTex(new Texture("Titel", 0, 0));
		Display.update();
		WorldWindow.load("TestWelt");
		
		long timeLastWorldTick = System.currentTimeMillis();
		while(!Display.isCloseRequested() && !beenden){
			Display.sync(60);

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
