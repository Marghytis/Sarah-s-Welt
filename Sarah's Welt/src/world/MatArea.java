package world;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import resources.Texture;
import util.Cycle;
import util.Tessellator;

import com.sun.scenario.effect.impl.BufferUtil;

import core.Settings;

public class MatArea {
	
	public List<Node> cycles = new ArrayList<>();
	
	public boolean tessellationNeeded = true;
	public Tessellator tessellator = new Tessellator();
	public int vbo;
	public int ibo;
	public int vertices;
	public int indices;
	
	public void render(Texture tex){
		if(cycles.size() == 0) return;
		if(Settings.debugView){
			for(Node n : cycles){
				GL11.glBegin(GL11.GL_LINE_LOOP);
				Cycle.iterate(n, (Node h) -> GL11.glVertex2f(h.p.x, h.p.y));
				GL11.glEnd();
			}
			return;
		}
		if(tessellationNeeded || vbo == 0){
			tess(tex);
		}
		tex.bind();
			GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
			GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
			
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
			
			GL11.glVertexPointer(2, GL11.GL_FLOAT,  16, 8);
			GL11.glTexCoordPointer(2, GL11.GL_FLOAT,  16, 0);
			
			GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
//			GL11.glDrawElements(GL11.GL_LINE_STRIP, indices, GL11.GL_UNSIGNED_INT, 0);
			GL11.glDrawElements(GL11.GL_TRIANGLES, indices, GL11.GL_UNSIGNED_INT, 0);
			
//			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, this.vertices);
	
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
			GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
			GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
			GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		tex.release();
	}
	
	public void tess(Texture tex){
		if(vbo != 0)GL15.glDeleteBuffers(vbo);
		if(ibo != 0)GL15.glDeleteBuffers(ibo);
		
		List<Integer> indices1 = new ArrayList<>();
		
		float[] vertices = tessellator.tessellateOneNode(cycles, tex.width, tex.height, indices1);
		this.vertices = vertices.length/4;
		this.indices = indices1.size();
		
		int[] indices = new int[indices1.size()];
		for(int i = 0; i < indices1.size(); i++)indices[i] = indices1.get(i);
		
		//create VBO
		FloatBuffer buffer = BufferUtil.newFloatBuffer(this.vertices*4);
		buffer.put(vertices);
		buffer.flip();
		
		vbo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		//create IBO
		IntBuffer indexBuffer = BufferUtil.newIntBuffer(this.indices);
		indexBuffer.put(indices);
		indexBuffer.flip();
		
		ibo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		
		tessellationNeeded = false;
	}
}