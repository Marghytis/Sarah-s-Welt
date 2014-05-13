package particles;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;

import resources.Texture;
import util.Color;
import core.geom.Vec;


public class Particle {
	public Vec pos = new Vec();
	public Vec vel = new Vec();//per millis
	public Color col = new Color();
	public float rot;
	public float rad;
	public int live;//millis
	public boolean justSpawned = true;
	
	public static class ParticleType {
		public Texture tex;
		public int vbo;
		
		public ParticleType(Texture tex){
			this.tex = tex;

			float wH = tex.width/2;
			float hH = tex.height/2;
			float[] vertices = new float[]{
					0, 1, - wH, - hH,
					1, 1, + wH, - hH,
					1, 0, + wH, + hH,
					0, 0, - wH, + hH};
			
			//create VBO
			FloatBuffer buffer = BufferUtils.createFloatBuffer(vertices.length);
			buffer.put(vertices);
			buffer.flip();
			
			vbo = GL15.glGenBuffers();
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		}
	}
}
