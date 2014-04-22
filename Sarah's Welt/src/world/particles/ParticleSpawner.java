package world.particles;


import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import world.particles.Particle.ParticleType;

public class ParticleSpawner {

	public static float gravityAcceleration = -0.000005f;
	public static float sideWaysAcceleration = 0.0000002f;

	public Particle[] particles;
	public int index = 0;
	public ParticleType type;
	
	public ParticleSpawner(ParticleType type, int maxAmount){
		this.type = type;
		particles = new Particle[maxAmount];
		for(int i = 0; i < particles.length; i++){
			particles[i] = new Particle();
		}
	}
	
	public void createParticle(float x, float y){
		if(index == particles.length){
			index = 0;
		}
		if(particles[index].inUse){
			System.out.println("Not enough Particles in reserve!!!!");
		}
		particles[index].set(type.lifeTime, new Vector2f(x, y));
		
		index++;
	}
	
	public void createParticle(float x, float y, float sX, float sY){
		index++;
		if(index == particles.length){
			index = 0;
		}
		if(particles[index].inUse){
			System.out.println("Not enough Particles in reserve!!!!");
		}
		particles[index].set(type.lifeTime, new Vector2f(x, y), new Vector2f(sX, sY));
	}
	
	public void update(int dTime){
		for(int i = 0; i < particles.length; i++){
			particles[i].update(dTime, type);
		}
	}
	
	public void render(){
		GL11.glClearColor(0.6f, 0.3f, 0.4f, 0.5f);
//		GL11.glEnable(GL11.GL_TEXTURE_2D);

		GL11.glColor4f(type.color.x, type.color.y, type.color.z, type.color.w);
//		type.tex.bind();
		
		for(Particle p : particles){
			if(p.inUse){
				GL11.glPushMatrix();
				GL11.glTranslatef(p.pos.x, p.pos.y, 0);
				type.quad.draw();
				GL11.glPopMatrix();
			}
		}
		
//		type.tex.release();
	}
	
}
