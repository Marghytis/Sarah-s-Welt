package main;

import java.awt.Font;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import resources.SimpleText;
import resources.Texture;
import util.Quad;

public class Window {

	public static int WIDTH;
	public static int HEIGHT;
	
	public static SimpleText font;
	
	public long lastTime;
	
	public Window(int width, int height){
		WIDTH = width;
		HEIGHT = height;
		init();

		GL11.glClearColor(0.5f, 0.5f, 1f, 1);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		(new Quad(0, 0, 600, 400)).draw(Texture.TITEL);
		Display.update();
		
		font = new SimpleText(new Font("Russel Write TT", Font.BOLD, 45), true);
	}
	
	/**
	 * Initialize the new Window
	 */
	public void init(){
		setupDisplay();
		setupOpenGL();
	}
	
	/**
	 * Close the window
	 */
	public void close(){
		Display.destroy();
		System.exit(0);
	}
	
	/**
	 * Setup the Display of LWJGL
	 */
	public void setupDisplay(){
		try{
			Display.setTitle("Sarahs Welt");
			Display.setResizable(true);
			System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
			Display.setDisplayMode(new DisplayMode(600, 400));
			Display.setVSyncEnabled(true);
			Display.create();
		} catch(LWJGLException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Updates the Window and clears the screen
	 * @return if the game should do another tick
	 */
	public boolean nextFrame(){
		Display.update();
		if(Display.wasResized()) resize();
		GL11.glClearColor(0.5f, 0.5f, 1f, 1);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		if(Display.wasResized()) resize();
		Display.sync(60);
		return !Display.isCloseRequested();
	}
	
	/**
	 * Resize the Window
	 */
	public static void resize(){
		WIDTH = Display.getWidth();
		HEIGHT = Display.getHeight();
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, WIDTH, 0, HEIGHT, -1, 1);
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		Menu.refresh();
		Game.world.view.refresh();
	}
	
	/**
	 * Setup OpenGL
	 */
	public void setupOpenGL(){
		//set the viewport
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, WIDTH, 0, HEIGHT, -1, 1);
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		//enable blending
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        //enable texturing
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
	/**
	 * Fill the whole window with the selected color/ texture
	 */
	public void fill(){
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0, 1);
			GL11.glVertex2i(0, 0);
			GL11.glTexCoord2f(1, 1);
			GL11.glVertex2i(WIDTH, 0);
			GL11.glTexCoord2f(1, 0);
			GL11.glVertex2i(WIDTH, HEIGHT);
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex2i(0, HEIGHT);
		GL11.glEnd();
	}
}
