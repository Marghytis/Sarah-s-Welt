package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL10;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import sound.Lesson1;
import core.geom.Vec;

public class Test {
	public static void main(String[] args) {
//		new Test();
		List<Vec> vecs = new ArrayList<>();
		for(int i = 0; i < 1000000; i++){
			vecs.add(new Vec(1,1));
		}
		Vec shift = new Vec(1, 1);
		long time = System.currentTimeMillis();
		for(int i = 0; i < 1000; i++){
			vecs.get(i).shift(shift);
		}
		long time2 = System.currentTimeMillis();
		System.out.println((time2-time));
	}
	
	public int WIDTH = 1000, HEIGHT = 500;
	Lesson1 test;
	public Test() {
		
		List<int[][]> coords = readTextureCoordinator("res/creatures/Sarah.txt");
		
		for(int[][] pff : coords){
			System.out.println("");
			for(int[] vertex : pff){
				System.out.print(" [" + vertex[0] + ", " + vertex[1] + ", " + vertex[2] + "]");
			}
		}
		
//		createDisplay();
//		
//		setup();
//		
//		while (!Display.isCloseRequested()) {
//			loop();
//			Display.sync(60);
//			Display.update();
//		}
//		
//		Display.destroy();
	}
	
	public static List<int[][]> readTextureCoordinator(String file){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(file)));
			String line;
			
			List<int[][]> kA = new ArrayList<>();
			
			while((line = reader.readLine()) != null){
				String[] vertices = line.split(";");
				
				int[][] output = new int[vertices.length][3];
				
				for(int i = 0; i < vertices.length; i++){
					String[] coords = vertices[i].split(",");
					output[i] = new int[]{Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), Integer.parseInt(coords[2])};
				}
				
				kA.add(output);
			}
			
			reader.close();
			
			return kA;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
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
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(20);
		vertexBuffer.put(vertices);
		vertexBuffer.flip();
		
		vbo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		//create IBO
		IntBuffer indexBuffer = BufferUtils.createIntBuffer(indexCount);
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
		
		//Draw vertices with _id buffer
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL11.glDrawElements(GL11.GL_TRIANGLES, indexCount, GL11.GL_UNSIGNED_INT, 0);
		
		//draw vertices without _id buffer
//		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vertexCount);
		
		//release buffers
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		
	}
}
