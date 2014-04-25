package main;

import java.awt.Toolkit;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import util.T;
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
		} 
		catch (LWJGLException e) {
			e.printStackTrace();
		}
		Game.startLoop();
	}
	
	static long timeLastWorldTick;
	
	/**
	 * Starts the game loop, which ticks and renders the game
	 */
	public static void startLoop(){
//		Node n1 = new Node(0, 0);
//		Node n2 = new Node(10, 0); n1.next = n2; n2.last = n1;
//		Node n3 = new Node(10, 10); n2.next = n3; n3.last = n2;
//		Node n4 = new Node(0, 10); n3.next = n4; n4.last = n3;
//		n4.next = n1; n1.last = n4;
//		
//		MatArea test = new MatArea();
//		test.cycles.add(n1);
		
		while(window.nextFrame() && !closeRequested){
			keyListening();
			mouseListening();

			int dTime = (int)(System.currentTimeMillis() - timeLastWorldTick);
			if(!menu.pauseWorld) WorldWindow.tick((int)dTime);
			timeLastWorldTick += dTime;


			
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
