package core;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import resources.Res;
import resources.Texture;
import util.T;
import world.Calendar;
import world.WorldWindow;

public class Main {

	public static void main(String[] args){
		
		Window.create("Sarahs Welt", 1000, 600);
//		Window.createFullScreen("Sarahs Welt");
		Window.fill(new Texture("titelbild", 0, 0).handle);
		Display.update();
		Res.load();
		WorldWindow.load("TestWelt");
		
		
		long timeLastWorldTick = System.currentTimeMillis();
		while(!Display.isCloseRequested() && !beenden){
			long time = System.currentTimeMillis();
			
			if(Settings.sound && !Res.test.playing) Res.test.play();

			long testTime = System.nanoTime();
			render();
			printTime(testTime);
			
			listening();
			long t = System.currentTimeMillis();
			calculate(Math.min((int)(t - timeLastWorldTick), 20));
			timeLastWorldTick = t;
			
			
			Display.update();
			try {
				Thread.sleep(17 - (System.currentTimeMillis() - time));
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch(IllegalArgumentException e){}
		}
		
		Res.test.stop();
		Res.unload();
		Display.destroy();
	}

	static int average = 180000;
	static int sum;
	static int count;
	public static void printTime(long testTime){
		int delta = (int) (System.nanoTime() - testTime);
		
		sum += delta;
		count++;
		average = sum/count;
		
		if(delta > average + 20000){
			System.out.println(delta);
		}
	}
	
	public static boolean beenden;
	
	public static void render(){
		GL11.glLoadIdentity();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0.55f, 0.53f, 0.76f, 1);
		GL11.glColor4f(1, 1, 1, 1);
//		WorldWindow.render();
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
