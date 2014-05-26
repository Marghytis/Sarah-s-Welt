package core;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import resources.Res;
import resources.TextureFile;
import world.Calendar;
import world.World;

public class Main {

	public static void main(String[] args){
//		Window.create("Sarahs Welt", 1200, 800);
		Window.createFullScreen("Sarahs Welt");
		Window.fill(new TextureFile("titelbild", 0, 0).handle);
		Display.update();
		Res.load();
		World.load("TestWelt", 10);
		
		long timeLastWorldTick = System.currentTimeMillis();
		while(!Display.isCloseRequested() && !beenden){
			long testTime = System.currentTimeMillis();
//			Display.sync(500);
			
//			if(Settings.sound && !Res.test.playing) Res.test.play();

			render();
			
			listening();
			long t = System.currentTimeMillis();
			calculate(Math.min((int)(t - timeLastWorldTick), 20));
			timeLastWorldTick = t;
			
			Display.update();
			try {
				Thread.sleep(17 - (System.currentTimeMillis() - testTime));
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch(IllegalArgumentException e){}
//			System.out.println(System.currentTimeMillis() - testTime);
		}
		
//		Res.test.stop();
		Res.unload();
		Display.destroy();
	}
	
	public static boolean beenden;
	
	public static void render(){
		GL11.glLoadIdentity();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0.55f, 0.53f, 0.76f, 1);
		GL11.glColor4f(1, 1, 1, 1);
		World.render();
		GL11.glLoadIdentity();
		Menu.render();
	}
	
	public static void calculate(int delta){
		
		if(!Menu.pauseWorld()){
			World.update(delta);
			Calendar.tick(delta);
		}
	}

	public static void listening(){
		if(Menu.pauseWorld()){
			Menu.keyListening();
		} else {
			World.keyListening();
		}//extra
		if(Menu.pauseWorld()){
			Menu.mouseListening();
		} else {
			World.mouseListening();
		}
	}	
}
