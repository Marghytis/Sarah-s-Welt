package world.particles;


import main.GL;
import world.particles.Particle.ParticleType;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

public class ParticleSpawner {

	public static float gravityAcceleration = -0.05f;
	public static float sideWaysAcceleration = 0.002f;

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
	
	public void createParticle(ParticleType type, float x, float y){
		index++;
		if(index == particles.length){
			index = 0;
		}
		if(particles[index].inUse){
			System.out.println("Not enough Particles in reserve!!!!");
		}
		particles[index].set(type.lifeTime, new Vector2f(x, y));
		
	}
	
	public void createParticle(ParticleType type, float x, float y, float sX, float sY){
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
		
		type.applyRenderInformation();
		
		GL11.glBegin(GL11.GL_QUADS);
		for(Particle p : particles){
			if(p.inUse){
				GL.drawQuadTex(p.pos.x - type.size, p.pos.y - type.size, p.pos.x + type.size, p.pos.y + type.size, 0, 0, 1, 1);
			}
		}
		GL11.glEnd();
	}
	
}
