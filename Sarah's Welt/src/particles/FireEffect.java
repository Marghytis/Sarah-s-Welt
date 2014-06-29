package particles;

import static org.lwjgl.opengl.ARBShaderObjects.glGetUniformLocationARB;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;

import particles.Particle.ParticleType;
import resources.Lightmap;
import resources.Shader;
import resources.TextureFile;
import core.Window;
import core.geom.Vec;

public class FireEffect implements ParticleEffect{

	public static final ParticleType SMOKE = new ParticleType(new TextureFile("particles/Smoke"));

	public ParticleEmitter smoke = new ParticleEmitter(1000, 100, SMOKE, 2000){
		
		@Override
		public void makeParticle(Particle p) {
			p.pos.set(pos.x + random.nextInt(size), pos.y);
			p.vel.set((random.nextFloat() - 0.5f)*0.05f, 0.1f);
			p.col.set(0.4f, 0.4f, 0.4f, 1f);
			p.live = startLife;
		}

		@Override
		public void velocityInterpolator(Particle p) {
			p.vel.x += 0.001f;
		}

		@Override
		public void colorInterpolator(Particle p) {
			p.col.a = (float) p.live /startLife;
		}
		
		@Override
		public void radiusInterpolator(Particle p){
			p.rad = 0.1f + (2.5f*(startLife - p.live)/startLife);
		}
		
	};
	
	public static final ParticleType FLAME = new ParticleType(new TextureFile("particles/Flame"));
	
	public ParticleEmitter flame = new ParticleEmitter(10000, 50, FLAME, 1000){

		@Override
		public void makeParticle(Particle p) {
			p.pos.set(pos.x + random.nextInt(size), pos.y);
			p.vel.set((random.nextFloat() - 0.5f)*0.01f, 0.1f);
			p.col.set(0.5f, 0.5f, 0.1f, 0.5f);
			p.rot = random.nextInt(3)-1;
			p.live = startLife;
		}

		@Override
		public void velocityInterpolator(Particle p) {
			p.vel.x += 0.001f;
		}

		@Override
		public void colorInterpolator(Particle p) {
			p.col.a = p.live*0.8f /startLife;
			p.col.r = 0.5f + p.live*0.4f/startLife;
			p.col.g = 0.5f - p.live*0.2f/startLife;//0.9f, 0.3f, 0.1f, 0.5f
		}

		@Override
		public void rotationInterpolator(Particle p) {
			if(p.rot > 0){
				p.rot = ((float)Math.PI/10)*p.live;
			} else {
				p.rot = -((float)Math.PI/10)*p.live;
			}
		}
		
		@Override
		public void radiusInterpolator(Particle p){
			p.rad = 2*((float)p.live/startLife);
		}

//		public void renderParticles(){
//			type.tex.bind();
//				for(Particle p : particles){
//					if(p.live > 0){
//						renderParticle(p);
//					}
//				}
//			Texture.bindNone();
//		}

		@Override
		public void renderParticle(Particle p) {
			GL11.glColor4f(p.col.r, p.col.g, p.col.b, p.col.a);
			GL11.glPushMatrix();
				GL11.glTranslatef(p.pos.x, p.pos.y, 0);
				GL11.glRotatef(p.rot, 0, 0, 1);
				GL11.glScalef(p.rad, p.rad, 0);
					GL11.glDrawArrays(GL11.GL_QUADS, 0, 4);
			GL11.glPopMatrix();
		}
		
	};
	
	public static final ParticleType SPARK = new ParticleType(new TextureFile("particles/Spark"));
	
	public ParticleEmitter spark = new ParticleEmitter(1000, 5, SPARK, 2000){

		@Override
		public void makeParticle(Particle p) {
			p.pos.set(pos.x + random.nextInt(size), pos.y);
			p.vel.set((random.nextFloat() - 0.5f)*0.01f, 0.1f);
			p.col.set(1f, 0.6f, 0.1f, 1f);
			p.rot = random.nextInt(3)-1;
			p.rad = 0.5f;
			p.live = startLife;
		}

		@Override
		public void velocityInterpolator(Particle p) {
			p.vel.x += (random.nextFloat() - 0.5f)*0.01f + 0.001f;
		}

		@Override
		public void colorInterpolator(Particle p) {
//			p.col.a = (float) (p.live*0.8f) /startLife;
//			p.col.r = 0.5f + p.live*0.4f/startLife;
//			p.col.g = 0.5f - p.live*0.2f/startLife;//0.9f, 0.3f, 0.1f, 0.5f
			if(p.live < startLife/2){
				p.col.set(0.1f, 0.1f, 0.1f, 1);
			}
		}

		@Override
		public void rotationInterpolator(Particle p) {
			if(p.rot > 0){
				p.rot = ((float)Math.PI/10)*p.live;
			} else {
				p.rot = -((float)Math.PI/10)*p.live;
			}
		}
	};
	
	public static final ParticleType LIGHT = new ParticleType(new TextureFile("particles/Fire"));
	
	public ParticleEmitter light = new ParticleEmitter(1000, 5, LIGHT, 1000){
		
		@Override
		public void renderParticles(){
			type.tex.bind();
				lightmap.bind();
					for(Particle p : particles){
						if(p.live > 0){
							renderParticle(p);
						}
					}
				lightmap.release();
			type.tex.release();
		}
		
		@Override
		public void renderParticle(Particle p){
			ARBShaderObjects.glUniform4fARB(glGetUniformLocationARB(Shader.Test.handle, "particleColor"), p.col.r, p.col.g, p.col.b, p.col.a);
			GL11.glPushMatrix();
				GL11.glTranslatef(p.pos.x, Window.HEIGHT - p.pos.y, 0);
				GL11.glRotatef(p.rot, 0, 0, 1);
				GL11.glScalef(p.rad, p.rad, 0);
					GL11.glDrawArrays(GL11.GL_QUADS, 0, 4);
			GL11.glPopMatrix();
		}

		@Override
		public void makeParticle(Particle p) {
			p.pos.set(pos.x + random.nextInt(size), pos.y);
			p.vel.set((random.nextFloat() - 0.5f)*0.01f, 0.1f);
			p.col.set(1f, 1f, 1f, 1f);
			p.rad = 1 + (random.nextFloat()*2);
			p.live = startLife;
			
		}

		@Override
		public void colorInterpolator(Particle p) {
			p.col.a = p.live*1.0f /startLife;
		}
	};

	
	public Vec pos = new Vec(Window.WIDTH/4, Window.HEIGHT/2);
	public int size = 20;
	public Lightmap lightmap;
	
	public FireEffect(float x, float y, Lightmap lightmap){
		pos.set(x, y);
		this.lightmap = lightmap;
	}
	
	@Override
	public void tick(float dTime){
		smoke.tick(dTime);
		flame.tick(dTime);
		spark.tick(dTime);
		light.tick(dTime);
	}
	
	@Override
	public void render(){
		smoke.render();
		flame.render();
		spark.render();
		light.render();
	}
	
	@Override
	public void finalize(){
		smoke.finalize();
		flame.finalize();
		spark.finalize();
		light.finalize();
	}

	@Override
	public boolean living() {
		return true;
	}
	
}
