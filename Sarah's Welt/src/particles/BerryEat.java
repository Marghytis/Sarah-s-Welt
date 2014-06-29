package particles;

import particles.Particle.ParticleType;
import resources.TextureFile;
import core.geom.Vec;

public class BerryEat implements ParticleEffect{

	
	public static final ParticleType STAR = new ParticleType(new TextureFile("particles/RainbowParticle"));
	
	public ParticleEmitter stars = new ParticleEmitter(100, 1, STAR, 1000){

		@Override
		public void makeParticle(Particle p) {
			p.pos.set(pos.x, pos.y);
			p.vel.set((random.nextFloat() - 0.5f)*0.4f, (random.nextFloat() - 0.5f)*0.4f);//-0.8f
			p.col.set(0.4f, 0, 0.9f, 1);
			p.rad = 0.5f + random.nextFloat();
			p.live = startLife;
		}

		@Override
		public void velocityInterpolator(Particle p) {
			p.vel.x *= 0.99f;
		}

		@Override
		public void colorInterpolator(Particle p) {
			p.col.a *= 0.95f;
		}

		@Override
		public void rotationInterpolator(Particle p) {}

		@Override
		public void radiusInterpolator(Particle p) {
			p.rad *= 0.99f;
		}
	};
	
	public static final ParticleType SPARKLE = new ParticleType(new TextureFile("particles/Sparkle"));
	
	public ParticleEmitter sparkle = new ParticleEmitter(100, 1, SPARKLE, 1000){

		@Override
		public void makeParticle(Particle p) {
			p.pos.set(pos.x, pos.y);
			p.vel.set((random.nextFloat() - 0.5f)*0.4f, (random.nextFloat() - 0.5f)*0.4f);//-0.8f
			p.col.set(0.6f, 0, 0.7f, 1);
			p.rad = 0.5f + random.nextFloat();
			p.live = startLife;
		}

		@Override
		public void velocityInterpolator(Particle p) {
			p.vel.x *= 0.99f;
			p.vel.y *= 0.99f;
		}

		@Override
		public void colorInterpolator(Particle p) {
			p.col.a *= 0.95f;
		}

		@Override
		public void rotationInterpolator(Particle p) {}

		@Override
		public void radiusInterpolator(Particle p) {
			p.rad *= 0.99f;
		}
	};

	public Vec pos;
	public int count = 1000;

	public BerryEat(Vec pos){
		this.pos = new Vec(pos);
		for(int i = 0; i < 30; i++){
			stars.emittParticle(0);
			sparkle.emittParticle(0);
		}
		stars.emitting = false;
		sparkle.emitting = false;
	}
	
	@Override
	public void tick(float dTime){
		stars.tick(dTime);
		sparkle.tick(dTime);
		count -= dTime;
	}
	
	@Override
	public void render(){
		stars.render();
		sparkle.render();
	}
	
	@Override
	public void finalize(){
		stars.finalize();
		sparkle.finalize();
	}

	@Override
	public boolean living() {
		return count > 0;
	}
}
