package world.particles2;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import util.Quad;
import world.Point;
import world.particles2.Particle.ColorParticle;

public class Fire implements Runnable{

	public ColorParticle[] particles;
	public Random random = new Random();
	
	public int spawnRate = 5;//in millis
	public Point pos;
	public Point size = new Point(10, 1);
	public Quad particle = new Quad(-3, -3, 6, 6);
	
	public void run(){
		while(true){
			long time = System.currentTimeMillis();
				spawnParticle();
			long nextTime = System.currentTimeMillis();
			try { Thread.sleep(spawnRate - (nextTime - time)); } catch (InterruptedException e) {e.printStackTrace();}
		}
	}
	
	public Fire(int x, int y){
		particles = new ColorParticle[1000];
		for(int i = 0; i < particles.length; i++){
			particles[i] = new ColorParticle();
		}
		pos = new Point(x, y);
	}
	
	int index;
	public void spawnParticle(){
		setStartAttribs(particles[index++]);
		if(index == particles.length) index = 0;
	}
	
	public void setStartAttribs(ColorParticle p){
		p.pos.set(pos.x  + ((random.nextFloat() - 0.5f)*size.x), pos.y  + ((random.nextFloat() - 0.5f)*size.y));
		p.vel.set(0, 0.1f);
		p.live = 4000;
		p.r = 0.2f;
		p.g = 0;
		p.b = 1;
		p.a = 1;
	}
	
	public void update(int dTime){
		for(ColorParticle p : particles){
			if(p.live > 0){
				simulateParticle(p, dTime);
				renderParticle(p);
			}
		}
	}
	
	public void simulateParticle(ColorParticle p, int dTime){
//		if(p.live > 3800){
//			p.b *= 0.9f;
//			p.r = 1 - p.b;
//		} else if(p.live > 2500){
//			p.r += 0.01f;
//		}
//		p.g += 0.01f;
		float l = p.vel.length();
		p.b = l;
		p.r = 1 - p.b;
		p.g += 0.005f;

		p.a -= (0.005f + (random.nextFloat()*0.003f))*(dTime/20f);
		p.vel.y += (random.nextFloat()-0.5f)*0.003f;
		p.vel.x = 1f/Math.max(1, 0.1f*p.pos.minus(pos).length());
		p.pos.add(p.vel.scaledBy(dTime));
		p.live -= dTime;
	}
	
	public void renderParticle(ColorParticle p){
		GL11.glPushMatrix();
			GL11.glTranslatef(p.pos.x, p.pos.y, 0);
			GL11.glColor4f(p.r, p.g, p.b, p.a);
			particle.draw();
		GL11.glPopMatrix();
	}
	
}
