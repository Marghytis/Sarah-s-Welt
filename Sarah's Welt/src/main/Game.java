package main;

import java.awt.Toolkit;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import world.WorldWindow;
import world.time.Calendar;


public class Game {

	public static Window window;
	public static long time;
	
	public static Menu menu;
	public static boolean closeRequested = false;
	
	public static void main(String[] args){
		window = new Window((int)(Toolkit.getDefaultToolkit().getScreenSize().width*0.8f), 700);
		//TODO save last active worlds name. for now just use TestWorld all the time
		WorldWindow.load("TestWelt");
		menu = Menu.MAIN;

		try {
			Thread.sleep(1000);
			System.setProperty("org.lwjgl.opengl.Window.undecorated", "false");
			Display.setDisplayMode(new DisplayMode(Window.WIDTH, Window.HEIGHT));
			Window.resize();
		} catch (InterruptedException e){
			e.printStackTrace();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		Game.startLoop();
	}
	
	static long timeLastWorldTick;
	
	/**
	 * Starts the game loop, which ticks and renders the game
	 */
	public static void startLoop(){
		while(window.nextFrame() && !closeRequested){
			keyListening();
			mouseListening();

			long dTime = System.nanoTime() - timeLastWorldTick;
			if(!menu.pauseWorld) WorldWindow.tick((int)dTime/1000000.0f);
			timeLastWorldTick += dTime;

			Calendar.tick();

			WorldWindow.render();
			menu.render();

			
			GL11.glColor4f(1, 1, 1, 1);
			if(fpsC <= 0){fpsC = 20; fpsT = dTime;}fpsC--;
			Window.font.drawString(0, 0, "fps -- " + 1000000000/fpsT, 1, 1);
		}
		exit();
	}
	static long fpsT;
	static int fpsC;
	
	public static void keyListening(){
		if(!menu.pauseWorld){
			WorldWindow.keyListening();
		} else {
			while(Keyboard.next()){
				if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE && Keyboard.getEventKeyState()){
					if(menu == Menu.MAIN){
						menu = Menu.EMPTY;
					} else {
						menu = Menu.MAIN;
					}
				}
				if(Keyboard.getEventKey() == Keyboard.KEY_G && Keyboard.getEventKeyState()){
					if(menu == Menu.DEBUG){
						menu = Menu.EMPTY;
					} else {
						menu = Menu.DEBUG;
					}
				}
			}
		}
		
	}
	
	public static void mouseListening(){
		if(menu.pauseWorld){
			menu.mouseListening();
		} else {
			WorldWindow.mouseListening();
		}
	}
	
	/**
	 * Exit the game
	 */
	public static void exit(){
		window.close();
	}
}
