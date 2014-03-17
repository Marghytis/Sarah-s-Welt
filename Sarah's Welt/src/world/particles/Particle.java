package world.particles;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import resources.ResLoader;

public class Particle {

	public boolean inUse = true;
	
	public Vector2f pos;
	public Vector2f vel;
	public int life;//in milliseconds
	
	public Particle(){
		pos = new Vector2f();
		vel = new Vector2f();
		inUse = false;
	}
	
	public void set(int lifeTime, Vector2f pos){
		this.pos = pos;
		this.life = lifeTime;
		inUse = true;
	}
	
	public void set(int lifeTime, Vector2f pos, Vector2f vel){
		this.pos = pos;
		this.vel = vel;
		this.life = lifeTime;
		inUse = true;
	}
	
	public void update(int dTime, ParticleType type){
		if(life > 0){
			life -= dTime;
			vel.x = (vel.x*type.friction) + ParticleSpawner.sideWaysAcceleration;
			vel.y = (vel.y*type.friction) + ParticleSpawner.gravityAcceleration;
			pos.x += vel.x*dTime;
			pos.y += vel.y*dTime;
		} else {
			inUse = false;
		}
	}
	
	public enum ParticleType{
		RAIN(2, new Vector4f(0, 0.5f, 1f, 0.5f), 5000, "", new Vector2f(0, -0.8f), 0.99f),
		SNOW(4, new Vector4f(0.8f, 0.8f, 0.8f, 0.5f), 8000, "Snow2", new Vector2f(0.125f, -0.1f), 0.2f);

		public float size;
		public Vector4f color;
		public int lifeTime;//in milliseconds
		public int texture;
		
		public Vector2f acc;
		public float friction;
		
		
		ParticleType(float size, Vector4f color, int lifeTime, String textureName, Vector2f acc, float friction){
			this.friction = friction;
			this.acc = acc;
			this.size = size;
			this.color = color;
			this.lifeTime = lifeTime;
			texture = ResLoader.loadPNGTexture(textureName);//TODO create a new Texture, not a handle
		}
		
		public void applyRenderInformation(){
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glColor4f(color.x, color.y, color.z, color.w);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		}
	}
}
