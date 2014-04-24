package world.particles2;

import static org.lwjgl.opengl.ARBShaderObjects.glGetUniformLocationARB;
import main.Window;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;

import resources.Shader;
import resources.Texture;
import world.Point;

public class FireEffect {
	
	public Point pos = new Point(Window.WIDTH/2, Window.HEIGHT/2);
	public int size = 20;

	public ParticleEmitter smoke = new ParticleEmitter(1000, 10){

		Texture tex = new Texture("Smoke");
		int startLife = 2000;
		
		public void renderParticle(Particle p) {
			tex.bind();
			Shader.Test.bind();
			ARBShaderObjects.glUniform4fARB(glGetUniformLocationARB(Shader.Test.handle, "particleColor"), p.col.r, p.col.g, p.col.b, p.col.a);
			System.out.println(p.col);
			GL11.glPushMatrix();
			GL11.glTranslatef(p.pos.x, p.pos.y, 0);
			GL11.glRotatef(p.rot, 0, 0, 1);
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2f(0, 1); GL11.glVertex2f(- p.rad, - p.rad);
				GL11.glTexCoord2f(1, 1); GL11.glVertex2f(+ p.rad, - p.rad);
				GL11.glTexCoord2f(1, 0); GL11.glVertex2f(+ p.rad, + p.rad);
				GL11.glTexCoord2f(0, 0); GL11.glVertex2f(- p.rad, + p.rad);
			GL11.glEnd();
			GL11.glPopMatrix();
			tex.release();
			Shader.Test.release();
		}

		public void makeParticle(Particle p) {
			p.pos.set(pos.x + random.nextInt(size), pos.y);
			p.vel.set((random.nextFloat() - 0.5f)*0.05f, 0.1f);
			p.col.set(0.4f, 0.4f, 0.4f, 1f);
			p.live = startLife;
		}

		public void velocityInterpolator(Particle p) {
			p.vel.x += 0.001f;
		}

		public void colorInterpolator(Particle p) {
			p.col.a = (float) p.live /startLife;
		}

		public void rotationInterpolator(Particle p) {}
		
		public void radiusInterpolator(Particle p){
			p.rad = 3 + ((float)(startLife - p.live)/startLife)*20;
		}
		
	};
	
	public ParticleEmitter flame = new ParticleEmitter(10000, 20){

		Texture tex = new Texture("Fire");
		int startLife = 1000;
		
		public void renderParticle(Particle p) {
			tex.bind();
			Shader.Test.bind();
			ARBShaderObjects.glUniform4fARB(glGetUniformLocationARB(Shader.Test.handle, "particleColor"), p.col.r, p.col.g, p.col.b, p.col.a);
			GL11.glPushMatrix();
			GL11.glTranslatef(p.pos.x, p.pos.y, 0);
			GL11.glRotatef(p.rot, 0, 0, 1);
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2f(0, 1); GL11.glVertex2f(- p.rad, - p.rad);
				GL11.glTexCoord2f(1, 1); GL11.glVertex2f(+ p.rad, - p.rad);
				GL11.glTexCoord2f(1, 0); GL11.glVertex2f(+ p.rad, + p.rad);
				GL11.glTexCoord2f(0, 0); GL11.glVertex2f(- p.rad, + p.rad);
			GL11.glEnd();
			GL11.glPopMatrix();
			tex.release();
			Shader.Test.release();
		}

		public void makeParticle(Particle p) {
			p.pos.set(pos.x + random.nextInt(size), pos.y);
			p.vel.set((random.nextFloat() - 0.5f)*0.01f, 0.1f);
			p.col.set(0.5f, 0.5f, 0.1f, 0.5f);
			p.rot = random.nextInt(3)-1;
			p.live = startLife;
		}

		public void velocityInterpolator(Particle p) {}

		public void colorInterpolator(Particle p) {
			p.col.a = (float) (p.live*0.8f) /startLife;
			p.col.r = 0.5f + p.live*0.4f/startLife;
			p.col.g = 0.5f - p.live*0.2f/startLife;//0.9f, 0.3f, 0.1f, 0.5f
		}

		public void rotationInterpolator(Particle p) {
			if(p.rot > 0){
				p.rot = ((float)Math.PI/10)*p.live;
			} else {
				p.rot = -((float)Math.PI/10)*p.live;
			}
		}
		
		public void radiusInterpolator(Particle p){
			p.rad = ((float)p.live/startLife)*20;
		}
		
	};
	
	public void start(){
		smoke.startEmitting();
		flame.startEmitting();
	}
	
	public void tick(int dTime){
		smoke.tick(dTime);
		flame.tick(dTime);
		GL11.glColor3f(0.4f, 0.2f, 0.1f);
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2f(Window.WIDTH/2 - 20, Window.HEIGHT/2 - 20);
			GL11.glVertex2f(Window.WIDTH/2 + 40, Window.HEIGHT/2 - 20);
			GL11.glVertex2f(Window.WIDTH/2 + 40, Window.HEIGHT/2);
			GL11.glVertex2f(Window.WIDTH/2 - 20, Window.HEIGHT/2);
		GL11.glEnd();
	}
	
	public void stop(){
		smoke.stopEmitting();
		flame.stopEmitting();
	}
	
}
