package particles;

import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import particles.Particle.ParticleType;
import resources.TextureFile;

public class ParticleEmitter{

	public int interval;//interval between two emits
	public ParticleType type;
	public boolean emitting = true;
	int startLife;
	
	/**
	 * 
	 * @param particleAmount
	 * @param spawnRate spawns per second
	 * @param type
	 * @param startLife in microseconds
	 */
	public ParticleEmitter(int particleAmount, int spawnRate, ParticleType type, int startLife){
		particles = new Particle[particleAmount];
		for(int i = 0; i < particles.length; i++){
			particles[i] = new Particle();
		}
		if(spawnRate > 0){
			this.interval = 1000000/spawnRate;
		} else {
			emitting = false;
			this.interval = Integer.MAX_VALUE;
		}
		
		this.type = type;
		this.startLife = startLife;
	}

	public Particle[] particles;
	public Random random = new Random();
	
	int index;
	public void emittParticle(float age){
		Particle p = particles[index++];
		makeParticle(p);
		tickParticle(p, age);
		p.justSpawned = true;
		if(index == particles.length)index = 0;
	}
	
	public void makeParticle(Particle p){};
	public void killParticle(Particle p){};
	public void velocityInterpolator(Particle p){};
	public void colorInterpolator(Particle p){};
	public void rotationInterpolator(Particle p){};
	public void radiusInterpolator(Particle p){};
	
	float overFlow;
	public void tick(float delta){
		if(emitting){
			float timePassed = delta+overFlow;
			float count = timePassed/interval;
			for(int i = 0; i < count; i++){
				emittParticle(timePassed - (count*interval));
			}
			overFlow = timePassed%interval;
		}
		for(Particle p : particles){
			if(p.justSpawned){
				p.justSpawned = false;
			} else if(p.live > 0){
				tickParticle(p, delta);
			}
		}
	}
	
	public void tickParticle(Particle p, float delta){
		velocityInterpolator(p);
		colorInterpolator(p);
		rotationInterpolator(p);
		radiusInterpolator(p);
		p.pos.shift(p.vel.scaledBy(delta));
		p.live -= delta;
		if(p.live <= 0){
			killParticle(p);
		}
	}

	public void render(){
		
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, type.vbo);
		
		GL11.glVertexPointer(2, GL11.GL_FLOAT, 16, 8);
		GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 16, 0);
		
		renderParticles();
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
	}
	
	public void renderParticles(){
		type.tex.bind();
			for(Particle p : particles){
				if(p.live > 0){
					renderParticle(p);
				}
			}
		TextureFile.bindNone();
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
	
	@Override
	public void finalize(){
		GL15.glDeleteBuffers(type.vbo);
	}
}
