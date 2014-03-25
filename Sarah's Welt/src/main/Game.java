package main;

import org.lwjgl.input.Keyboard;

import world.World;


public class Game {

	public static Window window;
	public static long time;
	
	public static World world;
	public static Menu menu;
	public static boolean closeRequested = false;
	
	public static void main(String[] args){
		
		Game.window = new Window(1000, 500);
		
		//TODO save last active worlds name. for now just use TestWorld all the time
		world = new World("TestWorld");
		menu = Menu.MAIN;
		
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
			
			if(!menu.pauseWorld) world.view.tick((int)(newTime - time)/1000000.0f);
			
			world.view.render();
			menu.render();
			
			time = newTime;
		}
		exit();
	}
	
	public static void keyListening(){
		while(Keyboard.next()){
			if(Keyboard.getEventKeyState() && Keyboard.getEventKey() == Keyboard.KEY_ESCAPE){
				System.out.println("test");
				if(menu == Menu.MAIN){
					menu = Menu.EMPTY;
				} else {
					menu = Menu.MAIN;
				}
			}
		}
		if(menu != Menu.MAIN){
			if(Keyboard.isKeyDown(Keyboard.KEY_G)){
				menu = Menu.DEBUG;
			} else {
				menu = Menu.EMPTY;
			}
		}
	}
	
	public static void mouseListening(){
		menu.mouseListening();
		if(!menu.pauseWorld) world.view.mouseListening();
	}
	
	/**
	 * Exit the game
	 */
	public static void exit(){
		window.close();
	}
}
