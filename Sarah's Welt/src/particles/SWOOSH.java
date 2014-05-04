package particles;

import particles.Particle.ParticleType;
import resources.Texture;
import core.geom.Vec;

public class SWOOSH {
	
public static final ParticleType SMOKE = new ParticleType(new Texture("particles/Smoke"));
	
	public ParticleEmitter smoke = new ParticleEmitter(100, 1, SMOKE, 3000){

		public void makeParticle(Particle p) {
			p.pos.set(pos.x, pos.y);
			float angle = random.nextFloat()*(float)(Math.PI*2);
			
			p.vel.set((float)Math.cos(angle)*(random.nextFloat()*0.3f), (float)Math.sin(angle)*(random.nextFloat()*0.3f) + 0.1f);//-0.8f
			p.col.set(0.7f, 0.7f, 0.5f, 0.1f);
			p.rot = random.nextBoolean() ? 0.1f : -0.1f;
			p.rad = 2;
			p.live = startLife;
		}

		public void velocityInterpolator(Particle p) {
			p.vel.x *= 0.97f;
			p.vel.y *= 0.97f;
		}

		public void colorInterpolator(Particle p) {
			p.col.a -= 0.001f;
		}

		public void rotationInterpolator(Particle p) {
			if(p.rot > 0){
				p.rot = ((float)Math.PI/100)*p.live;
			} else {
				p.rot = -((float)Math.PI/100)*p.live;
			}
		}

		public void radiusInterpolator(Particle p) {}
	};
	
	public static final ParticleType BLOOD_DROP = new ParticleType(new Texture("particles/Blood_drop"));
	
	public ParticleEmitter blood = new ParticleEmitter(100, 1, BLOOD_DROP, 1000){

		public void makeParticle(Particle p) {
			p.pos.set(pos.x, pos.y);
			p.vel.set((random.nextFloat() - 0.5f)*0.4f, (random.nextFloat() - 0.5f)*0.4f);//-0.8f
			p.col.set(1, 1, 0f, 1f);
			p.rad = 0.5f;
			p.live = startLife;
		}

		public void velocityInterpolator(Particle p) {
			p.vel.x *= 0.99f;
			p.vel.y -= 0.01f;
		}

		public void colorInterpolator(Particle p) {
			p.col.a *= 0.95f;
		}

		public void rotationInterpolator(Particle p) {}

		public void radiusInterpolator(Particle p) {
			p.rad *= 0.99f;
		}
	};

	public Vec pos;
	public int count = 1000;

	public SWOOSH(Vec pos){
		this.pos = new Vec(pos);
		for(int i = 0; i < 60; i++){
			smoke.emittParticle(0);
		}
		for(int i = 0; i < 30; i++){
			blood.emittParticle(0);
		}
		smoke.emitting = false;
		blood.emitting = false;
	}
	
	public void tick(int dTime){
		smoke.tick(dTime);
		blood.tick(dTime);
		count -= dTime;
	}
	
	public void render(){
		smoke.render();
//		blood.render();
	}
	
	public void finalize(){
		smoke.finalize();
		blood.finalize();
	}
}
