package animationEditor.main;

import java.awt.Canvas;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
//github.com/Marghytis/Sarah-s-Welt.git
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class Window {

	public static int WIDTH, HEIGHT;
	
	public static void create(String name, Canvas parent){
		
		if(Display.isCreated()){
			Display.destroy();
			Mouse.destroy();
			Keyboard.destroy();
		}
		
		Display.setTitle(name);
		try {
			Display.setParent(parent);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		setSize(1000, 500);
		create();
	}
	
	private static void create(){
		try{
			Display.create();
			Mouse.create();
			Keyboard.create();
			setupOpenGL();
		} catch(LWJGLException e){
			e.printStackTrace();
		}
	}
	
	private static void setupOpenGL(){
		//set the viewport
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, WIDTH, 0, HEIGHT, -1, 1);
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	public static void setSize(int w, int h){
		WIDTH = w;
		HEIGHT = h;
		try {
			Display.setDisplayMode(new DisplayMode(w, h));
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	public static void fill(int texture){
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0, 1);	GL11.glVertex2i(0, 0);
			GL11.glTexCoord2f(1, 1);	GL11.glVertex2i(WIDTH, 0);
			GL11.glTexCoord2f(1, 0);	GL11.glVertex2i(WIDTH, HEIGHT);
			GL11.glTexCoord2f(0, 0);	GL11.glVertex2i(0, HEIGHT);
		GL11.glEnd();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
	
	public static void fill(){
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2i(0, 0);
			GL11.glVertex2i(WIDTH, 0);
			GL11.glVertex2i(WIDTH, HEIGHT);
			GL11.glVertex2i(0, HEIGHT);
		GL11.glEnd();
	}
}
