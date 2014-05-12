package core;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
//github.com/Marghytis/Sarah-s-Welt.git
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import resources.PNGDecoder;

public class Window {

	public static int WIDTH, HEIGHT;
	private static ByteBuffer icon16 = loadTexture("icons/icon16.png"), icon32 = loadTexture("icons/icon32.png"), icon64 = loadTexture("icons/icon64.png");
	
	public static void create(String name, int width, int height){
		
		if(Display.isCreated()){
			Display.destroy();
			Mouse.destroy();
			Keyboard.destroy();
		}
		
		Display.setTitle(name);
		setSize(width, height);
		create();
	}
	
	public static void createFullScreen(String name){
		
		if(Display.isCreated()){
			Display.destroy();
			Mouse.destroy();
			Keyboard.destroy();
		}

		Display.setTitle(name);
		try {
			Display.setFullscreen(true);
			Display.setVSyncEnabled(true);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		WIDTH = Display.getWidth();
		HEIGHT = Display.getHeight();


		create();
	}
	
	public static void createSplash(String tex, int width, int height){

		if(Display.isCreated()){
			Display.destroy();
			Mouse.destroy();
			Keyboard.destroy();
		}
		
		System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");

		setSize(width, height);
		
		create();
		int titleTex = createTexture(tex);
		fill(titleTex);
		Display.update();
		fill(titleTex);
		
		System.setProperty("org.lwjgl.opengl.Window.undecorated", "false");
	}
	
	private static void create(){
//		Display.setIcon(new ByteBuffer[] {icon16, icon32, icon64});
		try{
			Display.create();
//			Mouse.create();
//			Keyboard.create();
			setupOpenGL();
			setupOpenAL();
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
	
	private static void setupOpenAL(){
	    try {
	    	AL.create(null, 15, 22050, true);
	    } catch (LWJGLException le) {
	    	le.printStackTrace();
	      return;
	    }
	    AL10.alGetError();
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

    private static ByteBuffer loadTexture(String pathInRes){
        try {
        	InputStream in = new FileInputStream("res/" + pathInRes);
	            PNGDecoder decoder = new PNGDecoder(in);
	            ByteBuffer bb = ByteBuffer.allocateDirect(decoder.getWidth()*decoder.getHeight()*4);
	            decoder.decode(bb, decoder.getWidth()*4, PNGDecoder.RGBA);
	            bb.flip();
        	in.close();
            return bb;
        } catch (IOException e){
        	e.printStackTrace();
        }
		return null;
    }
    
    public static int createTexture(String pathInRes){

		// Create a new texture object in memory and bind it
    	int handle = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, handle);
		
		// All RGB bytes are aligned to each other and each component is 1 byte
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		
		// Upload the texture data and generate mip maps (for scaling)
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, WIDTH, HEIGHT, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, loadTexture(pathInRes));
				
		// Setup what to do when the texture has to be scaled
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
//		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST); 
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		
		return handle;
	}
}
