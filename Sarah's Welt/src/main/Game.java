package main;

import world.World;


public class Game {

	public static Window window;
	public static long time;
	
	public static boolean inWorld = false;
	public static boolean closeRequested = false;
	
	public static void main(String[] args){
//		Point intersection = new Point();
//		boolean is = Geom.intersectionLines(new Point(-2, -3), new Point(4, 1), new Point(1, 4), new Point(4, -2), intersection);
//		System.out.println(is);
//		if(is)System.out.println(intersection.x + "  " + intersection.y);
//		System.out.println((new Point(-8, -6)).cross(new Point(-7, -6)));
		
//		Line l1 = new Line();
//		l1.addPoints(new Point(0, 1), new Point(0, 2), new Point(0, 3), new Point(0, 4));
//		
//		Line l2 = new Line();
//		l2.addPoints(new Point(1, 1), new Point(1, 2), new Point(1, 3), new Point(1, 4));
//		
//		l1.appendLine(l2, false);
//		
//		Node n = l1.start;
//		while(!(n.next == null)){
//			System.out.print(n.p.toString());
//			n = n.next;
//		}
//		System.out.print(n.p.toString());
		
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

			if(inWorld){
				
				World.tick((int)(newTime - time)/1000000.0f);
				World.render();
				
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
