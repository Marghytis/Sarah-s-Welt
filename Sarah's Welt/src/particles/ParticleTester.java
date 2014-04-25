package particles;

import main.Window;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import resources.Lightmap;
import resources.Texture;
import util.Quad;
import world.Point;
import world.structures.Structure;

public class ParticleTester {
	
	public static Window window;
	public static Texture background;
	
	public static void main(String[] args){
		window = new Window(1000, 700);
		System.setProperty("org.lwjgl.opengl.Window.undecorated", "false");
		try {Display.setDisplayMode(new DisplayMode(Window.WIDTH, Window.HEIGHT));} catch (LWJGLException e) {e.printStackTrace();}
		Window.resize();
		background = new Texture("particles/Background");
		
		Lightmap lightmap = new Lightmap(new Texture(Window.WIDTH/2, Window.HEIGHT));
		lightmap.resetDark(window, 0);
		Quad leftWindow = new Quad(0, 0, Window.WIDTH/2, Window.HEIGHT);
		
		FireEffect fire = new FireEffect(Window.WIDTH/4, Window.HEIGHT/2, lightmap);
		fire.start();

		RainEffect rain = new RainEffect(new Point(Window.WIDTH*2/3 + 50, Window.HEIGHT*3/4 + 10), 100, 20);
		rain.start();
		Quad cloud = new Quad(Window.WIDTH*2/3, Window.HEIGHT*3/4 - 20, 200, 140);
		
		float t = 0;
		float r = 20;
		float d = 100;
		
		long time = System.currentTimeMillis();
		while(window.nextFrame()){
//			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			fire.pos.set(Window.WIDTH/4 + (r * (float) Math.cos(t)), Window.HEIGHT/2 + (r * (float) Math.sin(t)));
			t += (float)Math.PI/200;
			rain.pos.x = Window.WIDTH*2/3 + 50 + (d * (float) Math.cos(t));
			cloud.x = rain.pos.x - 50;
			
			lightmap.bind();
			lightmap.resetDark(window, 0.5f);
			lightmap.release();
			background.bind();
			window.fill();
			background.release();
			
			long nextTime = System.currentTimeMillis();
			fire.tick((int)(nextTime - time));
			rain.tick((int)(nextTime - time));
			fire.render();
			rain.render();
			time = nextTime;
			GL11.glColor4f(0.8f, 0.8f, 0.8f, 1);
			cloud.draw(Structure.CLOUD);
			
			GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_ZERO);
			leftWindow.draw(lightmap.texture);
	        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		}
		fire.stop();
		window.close();
	}
}
