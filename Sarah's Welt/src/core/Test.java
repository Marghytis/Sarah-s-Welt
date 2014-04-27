package core;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL10;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import sound.Lesson1;

import com.sun.scenario.effect.impl.BufferUtil;

public class Test {
	public static void main(String[] args) {
		new Test();
	}
	
	public int WIDTH = 1000, HEIGHT = 500;
	Lesson1 test;
	public Test() {
//		try {
//			java.applet.AudioClip clip = java.applet.Applet.newAudioClip(new java.net.URL("file://c:/res/twolf.wav"));
//			clip.loop();
//		} catch (java.net.MalformedURLException murle) {
//			System.out.println(murle);
//		}
		test = new Lesson1();
		test.execute();
		
		createDisplay();
		
		setup();
		
		while (!Display.isCloseRequested()) {
			loop();
			Display.sync(60);
			Display.update();
		}
		
		Display.destroy();
	}
	
	private void createDisplay() {		
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.setTitle("Test");
			Display.create();
			//set the viewport
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glOrtho(0, WIDTH, 0, HEIGHT, -1, 1);
			GL11.glViewport(0, 0, WIDTH, HEIGHT);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	
	int vbo; int vertexCount;
	int ibo; int indexCount;
	
	public void setup(){
		//set arrays
		float[] vertices = {
				1, 0, 0, 100, 100,
				0, 1, 0, 200, 100,
				0, 0, 1, 200, 200,
				1, 1, 0, 100, 200}; vertexCount = 4;
		int[] indices = {0, 1, 2, 2, 3, 0}; indexCount = 6;
		
		//create VBO
		FloatBuffer vertexBuffer = BufferUtil.newFloatBuffer(20);
		vertexBuffer.put(vertices);
		vertexBuffer.flip();
		
		vbo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		//create IBO
		IntBuffer indexBuffer = BufferUtil.newIntBuffer(indexCount);
		indexBuffer.put(indices);
		indexBuffer.flip();
		
		ibo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		
		//set color
		GL11.glColor4f(1, 1, 1, 1);
	}
	
	public void loop(){
		update();
		render();
	}
	
	public void update(){
		while(Keyboard.next()){
			if(Keyboard.getEventKeyState())
			switch(Keyboard.getEventKey()){
			case Keyboard.KEY_P: AL10.alSourcePlay(test.source.get(0)); break;
			case Keyboard.KEY_S: AL10.alSourceStop(test.source.get(0)); break;
			case Keyboard.KEY_SPACE: AL10.alSourcePause(test.source.get(0)); break;
			}
		}
	}
	
	public void render(){
		//Enable vertex buffer
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL11.glColorPointer(3, GL11.GL_FLOAT, 20, 0);
		GL11.glVertexPointer(2, GL11.GL_FLOAT, 20, 12);
		
		//Draw vertices with index buffer
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL11.glDrawElements(GL11.GL_TRIANGLES, indexCount, GL11.GL_UNSIGNED_INT, 0);
		
		//draw vertices without index buffer
//		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vertexCount);
		
		//release buffers
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		
	}
}
