package world.particles2;

import java.util.Random;

import org.lwjgl.opengl.GL11;

public abstract class ParticleEmitter implements Runnable{

	public int rate;//in millis
	Thread thread;
	boolean conti = true;
	
	public ParticleEmitter(int particleAmount, int spawnRate){
		thread = new Thread(this);
		particles = new Particle[particleAmount];
		for(int i = 0; i < particles.length; i++){
			particles[i] = new Particle();
		}
		this.rate = spawnRate;
	}
	
	public void run(){
		while(conti){
			long time = System.currentTimeMillis();
			emittParticle();
			long nextTime = System.currentTimeMillis();
			try { Thread.sleep(Math.max(0, rate - (nextTime - time))); } catch (InterruptedException e) {e.printStackTrace();}
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
		Particle p = particles[index++];
		makeParticle(p);
		if(index == particles.length) index = 0;
	}

	public abstract void renderParticle(Particle p);
	
	public abstract void makeParticle(Particle p);
	public abstract void velocityInterpolator(Particle p);
	public abstract void colorInterpolator(Particle p);
	public abstract void rotationInterpolator(Particle p);
	public abstract void radiusInterpolator(Particle p);
	
	public void tick(int dTime){
		GL11.glLoadIdentity();
		for(Particle p : particles){
			if(p.live > 0){
				velocityInterpolator(p);
				colorInterpolator(p);
				rotationInterpolator(p);
				radiusInterpolator(p);
				p.pos.add(p.vel.scaledBy(dTime));
				p.live -= dTime;
				renderParticle(p);
			}
		}
	}
	
}
