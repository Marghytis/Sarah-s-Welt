package particles;

import particles.Particle.ParticleType;
import resources.TextureFile;
import core.geom.Vec;

public class BasicMagicDissapperance implements ParticleEffect {
	
	public static final ParticleType SPARKLE = new ParticleType(new TextureFile("particles/Sparkle"));
	
	public ParticleEmitter sparkle = new ParticleEmitter(100, 1, SPARKLE, 3000){

		@Override
		public void makeParticle(Particle p) {
			p.pos.set(pos.x, pos.y);
			float angle = random.nextFloat()*(float)(Math.PI*2);
			
			p.vel.set((float)Math.cos(angle)*(random.nextFloat()*0.3f), (float)Math.sin(angle)*(random.nextFloat()*0.3f) + 0.1f);//-0.8f
			p.col.set(1f, 0.7f, 0.0f, 0.8f);
			p.rad = 0.5f;
			p.live = startLife;
		}

		@Override
		public void velocityInterpolator(Particle p) {
			p.vel.x *= 0.97f;
			p.vel.y *= 0.97f;
		}

		@Override
		public void colorInterpolator(Particle p) {
			p.col.a = (float) p.live /startLife;
		}

	};

	public static final ParticleType SMOKE = new ParticleType(new TextureFile("particles/Smoke"));
	
	public ParticleEmitter smoke = new ParticleEmitter(60, 0, SMOKE, 1000){

		@Override
		public void makeParticle(Particle p) {
			p.pos.set(pos.x, pos.y);
			float angle = random.nextFloat()*(float)(Math.PI*2);
			
			p.vel.set((float)Math.cos(angle)*(random.nextFloat()*0.3f), (float)Math.sin(angle)*(random.nextFloat()*0.3f) + 0.1f);//-0.8f
			p.col.set(1, 0, 0, 0.1f);
			p.rot = random.nextBoolean() ? 0.1f : -0.1f;
			p.rad = 1;
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

	public BasicMagicDissapperance(Vec pos){
		this.pos = new Vec(pos);
		for(int i = 0; i < 60; i++){
			smoke.emittParticle(0);
		}
		for(int i = 0; i < 60; i++){
			sparkle.emittParticle(0);
		}
		smoke.emitting = false;
		sparkle.emitting = false;
	}
	
	@Override
	public void tick(float dTime){
		smoke.tick(dTime);
		sparkle.tick(dTime);
		count -= dTime;
	}
	
	@Override
	public void render(){
		smoke.render();
		sparkle.render();
	}
	
	@Override
	public void finalize(){
		smoke.finalize();
		sparkle.finalize();
	}

	@Override
	public boolean living() {
		return count > 0;
	}
}
