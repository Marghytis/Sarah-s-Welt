package particles;

import particles.Particle.ParticleType;
import resources.TextureFile;
import core.geom.Vec;

public class DeathDust implements ParticleEffect {
	
public static final ParticleType SMOKE = new ParticleType(new TextureFile("particles/Smoke"));
	
	public ParticleEmitter smoke = new ParticleEmitter(100, 1, SMOKE, 3000){

		@Override
		public void makeParticle(Particle p) {
			p.pos.set(pos.x, pos.y);
			float angle = random.nextFloat()*(float)(Math.PI*2);
			
			p.vel.set((float)Math.cos(angle)*(random.nextFloat()*0.3f), (float)Math.sin(angle)*(random.nextFloat()*0.3f) + 0.1f);//-0.8f
			p.col.set(0.7f, 0.7f, 0.5f, 0.1f);
			p.rot = random.nextBoolean() ? 0.1f : -0.1f;
			p.rad = 2;
			p.live = startLife;
		}

		@Override
		public void velocityInterpolator(Particle p) {
			p.vel.x *= 0.97f;
			p.vel.y *= 0.97f;
		}

		@Override
		public void colorInterpolator(Particle p) {
			p.col.a -= 0.001f;
		}

		@Override
		public void rotationInterpolator(Particle p) {
			if(p.rot > 0){
				p.rot = ((float)Math.PI/100)*p.live;
			} else {
				p.rot = -((float)Math.PI/100)*p.live;
			}
		}

	};

	public Vec pos;
	public int count = 1000;

	public DeathDust(Vec pos){
		this.pos = new Vec(pos);
		for(int i = 0; i < 60; i++){
			smoke.emittParticle(0);
		}
		smoke.emitting = false;
	}
	
	@Override
	public void tick(float dTime){
		smoke.tick(dTime);
		count -= dTime;
	}
	
	@Override
	public void render(){
		smoke.render();
	}
	
	@Override
	public void finalize(){
		smoke.finalize();
	}

	@Override
	public boolean living() {
		return count > 0;
	}
}
