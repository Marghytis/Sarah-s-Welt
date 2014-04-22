package world.particles;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import resources.StackedTexture;
import resources.Texture;
import util.Quad;

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
			vel.x += (vel.x*type.friction) + ParticleSpawner.sideWaysAcceleration;
			vel.y += (vel.y*type.friction) + ParticleSpawner.gravityAcceleration;
			pos.x += vel.x*dTime;
			pos.y += vel.y*dTime;
		} else {
			inUse = false;
		}
	}
	
	public enum ParticleType{
		RAIN(new Quad(-2, -2, 2, 20), new Vector4f(0.8f, 0.8f, 0.8f, 0.4f), 5000, new StackedTexture("", 0, 0), new Vector2f(0, -0.8f), 0f);

		public Quad quad;
		public Vector4f color;
		public int lifeTime;//in milliseconds
		public Texture tex;
		
		public Vector2f acc;
		public float friction;
		
		
		ParticleType(Quad quad, Vector4f color, int lifeTime, StackedTexture tex, Vector2f acc, float friction){
			this.friction = friction;
			this.acc = acc;
			this.quad = quad;
			this.color = color;
			this.lifeTime = lifeTime;
			this.tex = tex;
		}
	}
}
