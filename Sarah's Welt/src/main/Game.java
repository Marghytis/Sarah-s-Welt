package main;

import world.World;


public class Game {

	public static Window window;
	public static long time;
	
	public static World world;
	public static boolean closeRequested = false;
	
	public static void main(String[] args){
		
		Game.window = new Window(1000, 500);
		Menu.refresh();
		
		Game.startLoop();
	}
	
	/**
	 * Starts the game loop, which ticks and renders the game
	 */
	public static void startLoop(){
		while(window.nextFrame() && !closeRequested){
			
			long newTime = System.nanoTime();

			if(world.isActive){
				
				world.view.tick((int)(newTime - time)/1000000.0f);
				world.view.render();
				
			} else {
				
				Menu.tick((int)(newTime - time)/1000000.0f);
				Menu.render();
				
			}
			
			time = newTime;
		}
		exit();
	}
	
	/**
	 * Exit the game
	 */
	public static void exit(){
		window.close();
	}
}
