package particles;

import resources.Texture;
import world.Point;

public class RainEffect {
	
	public ParticleEmitter drops = new ParticleEmitter(200, 50, new Texture("particles/Raindrop"), 2000){

		public void makeParticle(Particle p) {
			p.pos.set(pos.x + random.nextInt((int)size.x), pos.y + random.nextInt((int)size.y));
			p.vel.set((random.nextFloat() - 0.5f)*0.01f, -0.2f);//-0.8f
			p.col.set(0.8f, 0.8f, 0.8f, 0.4f);
			p.rad = 1;
			p.live = startLife;
		}

		public void velocityInterpolator(Particle p) {
			p.vel.y -= 0.01f;
		}

		public void colorInterpolator(Particle p) {}

		public void rotationInterpolator(Particle p) {}

		public void radiusInterpolator(Particle p) {}
		
	};

	public Point pos;
	public Point size = new Point();
	
	public RainEffect(Point pos, float width, float height){
		this.pos = new Point(pos);
		size.set(width, height);
	}
	
	public void tick(int dTime){
		drops.tick(dTime);
	}
	
	public void render(){
		drops.render();
	}
	
	public void finalize(){
		drops.finalize();
	}
	
}
