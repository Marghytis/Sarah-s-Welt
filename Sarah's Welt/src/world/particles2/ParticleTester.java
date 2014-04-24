package world.particles2;

import java.awt.Toolkit;

import main.Window;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class ParticleTester {
	
	public static Window window;
	
	public static void main(String[] args){
		window = new Window((int)(Toolkit.getDefaultToolkit().getScreenSize().width*0.8f), 700);
		System.setProperty("org.lwjgl.opengl.Window.undecorated", "false");
		try {Display.setDisplayMode(new DisplayMode(Window.WIDTH, Window.HEIGHT));} catch (LWJGLException e) {e.printStackTrace();}
		Window.resize();
		
		FireEffect fire = new FireEffect();
		fire.start();
		
		long time = System.currentTimeMillis();
		while(window.nextFrame()){
			
			long nextTime = System.currentTimeMillis();
			fire.tick((int)(nextTime - time));
			time = nextTime;
		}
		fire.stop();
		window.close();
	}
}
