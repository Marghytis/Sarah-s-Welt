package particles;

import particles.Particle.ParticleType;
import resources.Texture;
import core.geom.Vec;

public class BloodSplash implements ParticleEffect{

	
	public static final ParticleType BLOOD_DROP = new ParticleType(new Texture("particles/Blood_drop"));
	
	public ParticleEmitter blood = new ParticleEmitter(100, 1, BLOOD_DROP, 1000){

		public void makeParticle(Particle p) {
			p.pos.set(pos.x, pos.y);
			p.vel.set((random.nextFloat() - 0.5f)*0.4f, (random.nextFloat() - 0.5f)*0.4f);//-0.8f
			p.col.set(0.6f, 0, 0f, 1f);
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

	public BloodSplash(Vec pos){
		this.pos = new Vec(pos);
		for(int i = 0; i < 30; i++){
			blood.emittParticle(0);
		}
		blood.emitting = false;
	}
	
	public void tick(int dTime){
		blood.tick(dTime);
		count -= dTime;
	}
	
	public void render(){
		blood.render();
	}
	
	public void finalize(){
		blood.finalize();
	}

	public boolean living() {
		return count > 0;
	}
}
