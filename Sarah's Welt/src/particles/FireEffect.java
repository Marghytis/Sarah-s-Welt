package particles;

import static org.lwjgl.opengl.ARBShaderObjects.glGetUniformLocationARB;
import main.Window;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;

import resources.Lightmap;
import resources.Shader;
import resources.Texture;
import world.Point;

public class FireEffect {

	public ParticleEmitter smoke = new ParticleEmitter(1000, 10, new Texture("particles/Smoke"), 2000){
		
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
		
		public void radiusInterpolator(Particle p){
			p.rad = 0.1f + (2.5f*(float)(startLife - p.live)/startLife);
		}
		
	};
	
	public ParticleEmitter flame = new ParticleEmitter(10000, 20, new Texture("particles/Flame"), 1000){

		public void makeParticle(Particle p) {
			p.pos.set(pos.x + random.nextInt(size), pos.y);
			p.vel.set((random.nextFloat() - 0.5f)*0.01f, 0.1f);
			p.col.set(0.5f, 0.5f, 0.1f, 0.5f);
			p.rot = random.nextInt(3)-1;
			p.live = startLife;
		}

		public void velocityInterpolator(Particle p) {
			p.vel.x += 0.001f;
		}

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
			p.rad = 2*((float)p.live/startLife);
		}
		
	};
	
	public ParticleEmitter spark = new ParticleEmitter(1000, 200, new Texture("particles/Spark"), 2000){

		public void makeParticle(Particle p) {
			p.pos.set(pos.x + random.nextInt(size), pos.y);
			p.vel.set((random.nextFloat() - 0.5f)*0.01f, 0.1f);
			p.col.set(1f, 0.6f, 0.1f, 1f);
			p.rot = random.nextInt(3)-1;
			p.rad = 0.5f;
			p.live = startLife;
		}

		public void velocityInterpolator(Particle p) {
			p.vel.x += (random.nextFloat() - 0.5f)*0.01f + 0.001f;
		}

		public void colorInterpolator(Particle p) {
//			p.col.a = (float) (p.live*0.8f) /startLife;
//			p.col.r = 0.5f + p.live*0.4f/startLife;
//			p.col.g = 0.5f - p.live*0.2f/startLife;//0.9f, 0.3f, 0.1f, 0.5f
			if(p.live < startLife/2){
				p.col.set(0.1f, 0.1f, 0.1f, 1);
			}
		}

		public void rotationInterpolator(Particle p) {
			if(p.rot > 0){
				p.rot = ((float)Math.PI/10)*p.live;
			} else {
				p.rot = -((float)Math.PI/10)*p.live;
			}
		}
	};
	
	public ParticleEmitter light = new ParticleEmitter(1000, 200, new Texture("particles/Fire"), 1000){
		
		public void renderParticles(){
			tex.bind();
				Shader.Test.bind();
					lightmap.bind();
						for(Particle p : particles){
							if(p.live > 0){
								renderParticle(p);
							}
						}
					lightmap.release();
				Shader.Test.release();
			tex.release();
		}
		
		public void renderParticle(Particle p){
			ARBShaderObjects.glUniform4fARB(glGetUniformLocationARB(Shader.Test.handle, "particleColor"), p.col.r, p.col.g, p.col.b, p.col.a);
			GL11.glPushMatrix();
				GL11.glTranslatef(p.pos.x, Window.HEIGHT - p.pos.y, 0);
				GL11.glRotatef(p.rot, 0, 0, 1);
				GL11.glScalef(p.rad, p.rad, 0);
					GL11.glDrawArrays(GL11.GL_QUADS, 0, 4);
			GL11.glPopMatrix();
		}

		public void makeParticle(Particle p) {
			p.pos.set(pos.x + random.nextInt(size), pos.y);
			p.vel.set((random.nextFloat() - 0.5f)*0.01f, 0.1f);
			p.col.set(1f, 1f, 1f, 1f);
			p.rad = 1 + (random.nextFloat()*2);
			p.live = startLife;
		}

		public void colorInterpolator(Particle p) {
			p.col.a = (float) (p.live*1.0f) /startLife;
		}
	};

	
	public Point pos = new Point(Window.WIDTH/4, Window.HEIGHT/2);
	public int size = 20;
	public Lightmap lightmap;
	public Texture lightTex = new Texture("Fire");
	
	public FireEffect(float x, float y, Lightmap lightmap){
		pos.set(x, y);
		this.lightmap = lightmap;
	}
	
	public void start(){
		smoke.startEmitting();
		flame.startEmitting();
		spark.startEmitting();
		light.startEmitting();
	}
	
	public void tick(int dTime){
		smoke.tick(dTime);
		flame.tick(dTime);
		spark.tick(dTime);
		light.tick(dTime);
	}
	
	public void render(){
		smoke.render();
		flame.render();
		spark.render();
		light.render();
	}
	
	public void stop(){
		smoke.stopEmitting(); smoke.finalize();
		flame.stopEmitting(); flame.finalize();
		spark.stopEmitting(); spark.finalize();
		light.stopEmitting(); light.finalize();
	}
	
}
