package main;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import com.sun.scenario.effect.impl.BufferUtil;

public class Test {
	public static void main(String[] args) {
		new Test();
	}
	
	public int WIDTH = 1000, HEIGHT = 500;
	
	public Test() {
		
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
		float[] vertices = {100, 100, 200, 100, 200, 200, 100, 200}; vertexCount = 4;
		int[] indices = {0, 1, 2, 3}; indexCount = 4;
		
		//create VBO
		FloatBuffer vertexBuffer = BufferUtil.newFloatBuffer(vertexCount*2);
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
		//Enable vertex buffer
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL11.glVertexPointer(2, GL11.GL_FLOAT, 0, 0);
		
		//Draw vertices with index buffer
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL11.glDrawElements(GL11.GL_QUADS, indexCount, GL11.GL_UNSIGNED_INT, 0);
		
		//release buffers
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
	}
}
