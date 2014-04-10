package main;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import world.World;


public class Game {

	public static Window window;
	public static long time;
	
	public static Menu menu;
	public static boolean closeRequested = false;
	
	public static void main(String[] args){
		
		window = new Window(1000, 500);
		//TODO save last active worlds name. for now just use TestWorld all the time
		WorldWindow.load("TestWelt");
		menu = Menu.MAIN;
		

		try {
			Thread.sleep(4000);
			System.setProperty("org.lwjgl.opengl.Window.undecorated", "false");
			Display.setDisplayMode(new DisplayMode(Window.WIDTH, Window.HEIGHT));
		} catch (InterruptedException e){
			e.printStackTrace();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		Game.startLoop();
	}
	
	/**
	 * Starts the game loop, which ticks and renders the game
	 */
	public static void startLoop(){
		while(window.nextFrame() && !closeRequested){
			
			long newTime = System.nanoTime();
			
			keyListening();
			mouseListening();
			
			if(!menu.pauseWorld) WorldWindow.tick((int)(newTime - time)/1000000.0f);
			
			WorldWindow.render();
			menu.render();
			
			time = newTime;
		}
		exit();
	}
	
	public static void keyListening(){
		while(Keyboard.next()){
			if(Keyboard.getEventKeyState() && Keyboard.getEventKey() == Keyboard.KEY_ESCAPE){
				if(menu == Menu.MAIN){
					menu = Menu.EMPTY;
				} else {
					menu = Menu.MAIN;
				}
			}
			if(menu != Menu.MAIN && Keyboard.getEventKeyState()&& Keyboard.getEventKey() == Keyboard.KEY_G){
				if(menu == Menu.DEBUG){
					menu = Menu.EMPTY;
				} else {
					menu = Menu.DEBUG;
				}
			}
		}
	}
	
	public static void mouseListening(){
		menu.mouseListening();
		if(!menu.pauseWorld) WorldWindow.mouseListening();
	}
	
	/**
	 * Exit the game
	 */
	public static void exit(){
		window.close();
	}
}
