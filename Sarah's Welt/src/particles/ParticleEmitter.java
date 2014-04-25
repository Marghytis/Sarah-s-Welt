package particles;

import java.nio.FloatBuffer;
import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import resources.Shader;
import resources.Texture;

import com.sun.scenario.effect.impl.BufferUtil;

public class ParticleEmitter implements Runnable{

	public int rate;//in millis
	Thread thread;
	boolean conti = true;
	protected Texture tex;
	public int vbo;
	int startLife;
	
	public ParticleEmitter(int particleAmount, int spawnRate, Texture tex, int startLife){
		thread = new Thread(this);
		particles = new Particle[particleAmount];
		for(int i = 0; i < particles.length; i++){
			particles[i] = new Particle();
		}
		this.rate = spawnRate;
		this.tex = tex;
		this.startLife = startLife;

		float[] vertices = initialize();
		//create VBO
		FloatBuffer buffer = BufferUtil.newFloatBuffer(vertices.length*4);
		buffer.put(vertices);
		buffer.flip();
		
		vbo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	public float[] initialize(){
		float wH = tex.width/2;
		float hH = tex.height/2;
		return new float[]{
				0, 1, - wH, - hH,
				1, 1, + wH, - hH,
				1, 0, + wH, + hH,
				0, 0, - wH, + hH};
	}
	
	public void run(){
		while(conti){
			long time = System.currentTimeMillis();
			emittParticle();
			long nextTime = System.currentTimeMillis();
			try {
				Thread.sleep(Math.max(1, rate - (nextTime - time))); 
			} catch (InterruptedException e) {e.printStackTrace();}
		}
	}
	
	public void startEmitting(){
		thread.start();
	}
	
	public void stopEmitting(){
		conti = false;
	}
	

	public Particle[] particles;
	public Random random = new Random();
	
	int index;
	public void emittParticle(){
		makeParticle(particles[index++]);
		if(index == particles.length) index = 0;
	}
	
	public void makeParticle(Particle p){};
	public void velocityInterpolator(Particle p){};
	public void colorInterpolator(Particle p){};
	public void rotationInterpolator(Particle p){};
	public void radiusInterpolator(Particle p){};
	
	public void tick(int dTime){
		for(Particle p : particles){
			if(p.live > 0){
				velocityInterpolator(p);
				colorInterpolator(p);
				rotationInterpolator(p);
				radiusInterpolator(p);
				p.pos.add(p.vel.scaledBy(dTime));
				p.live -= dTime;
			}
		}
	}

	public void render(){
		
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		
		GL11.glVertexPointer(2, GL11.GL_FLOAT, 16, 8);
		GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 16, 0);
		
		renderParticles();
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	public void renderParticles(){
		tex.bind();
			Shader.Test.bind();
				for(Particle p : particles){
					if(p.live > 0){
						renderParticle(p);
					}
				}
			Shader.Test.release();
		tex.release();
	}

	public void renderParticle(Particle p) {
		GL11.glColor4f(p.col.r, p.col.g, p.col.b, p.col.a);
		GL11.glPushMatrix();
			GL11.glTranslatef(p.pos.x, p.pos.y, 0);
			GL11.glRotatef(p.rot, 0, 0, 1);
			GL11.glScalef(p.rad, p.rad, 0);
				GL11.glDrawArrays(GL11.GL_QUADS, 0, 4);
		GL11.glPopMatrix();
	}
	
	public void finalize(){
		GL15.glDeleteBuffers(vbo);
	}
}
