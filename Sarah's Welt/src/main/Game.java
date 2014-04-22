package main;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import world.WorldWindow;


public class Game {

	public static Window window;
	public static long time;
	
	public static Menu menu;
	public static boolean closeRequested = false;
	
	public static void main(String[] args){
//		Line l = new Line(0,1,2,3,4,5,6,7,8,9,10,11);
//		l.end.next = l.start;
//		l.start.last = l.end;
//		Cycle.iterate(l.start, (Node n) -> System.out.print(n.p.toString()));
		window = new Window(1000, 500);
		//TODO save last active worlds name. for now just use TestWorld all the time
		WorldWindow.load("TestWelt");
		menu = Menu.MAIN;
		

		try {
			Thread.sleep(1000);
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
