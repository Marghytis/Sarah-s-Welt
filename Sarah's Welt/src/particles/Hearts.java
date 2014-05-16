package particles;

import particles.Particle.ParticleType;
import resources.Texture;
import core.geom.Vec;

public class Hearts implements ParticleEffect{

	
	public static final ParticleType HEART = new ParticleType(new Texture("particles/Heart"));
	
	public ParticleEmitter hearts = new ParticleEmitter(100, 1, HEART, 1000){

		public void makeParticle(Particle p) {
			p.pos.set(pos.x, pos.y);
			p.vel.set((random.nextFloat() - 0.5f)*0.4f, (random.nextFloat() - 0.5f)*0.4f);//-0.8f
			p.col.set(0.8f, 0, 0, 1);
			p.rad = 0.5f + random.nextFloat();
			p.live = startLife;
		}

		public void velocityInterpolator(Particle p) {
			p.vel.x *= 0.99f;
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

	public Hearts(Vec pos){
		this.pos = new Vec(pos);
		for(int i = 0; i < 30; i++){
			hearts.emittParticle(0);
		}
		hearts.emitting = false;
	}
	
	public void tick(int dTime){
		hearts.tick(dTime);
		count -= dTime;
	}
	
	public void render(){
		hearts.render();
	}
	
	public void finalize(){
		hearts.finalize();
	}
}
