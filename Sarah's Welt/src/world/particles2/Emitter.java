package world.particles2;

import java.util.Random;

public abstract class Emitter implements Runnable{

	public Particle[] particles;
	public Random random = new Random();
	
	public int spawnRate;//in millis
	
	public void run(){
		long time = System.currentTimeMillis();
		while(true){
			spawnParticle();
			long nextTime = System.currentTimeMillis();
			try { Thread.sleep(spawnRate - (nextTime - time)); } catch (InterruptedException e) {e.printStackTrace();}
		}
	}
	
	public Emitter(int size){
		particles = new Particle[size];
		for(int i = 0; i < particles.length; i++){
			particles[i] = new Particle();
		}
	}
	
	int index;
	public void spawnParticle(){
		setStartAttribs(particles[index++]);
		if(index == particles.length) index = 0;
	}
	
	public abstract void setStartAttribs(Particle p);
	
	public void simulate(int dTime){
		for(Particle p : particles){
			if(p.live > 0){
				simulateParticle(p, dTime);
				p.x += p.dx*dTime;
				p.y += p.dy*dTime;
				p.live -= dTime;
			}
		}
	}
	
	public abstract void simulateParticle(Particle p, int dTime);

	public abstract void render();
	
	public abstract void renderParticle(Particle p);
	
}
