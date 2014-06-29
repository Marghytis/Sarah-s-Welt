package particles;

import particles.Particle.ParticleType;
import resources.TextureFile;
import core.geom.Vec;

public class RainEffect implements ParticleEffect{
	
	public static final ParticleType RAINDROP = new ParticleType(new TextureFile("particles/Raindrop"));
	
	public ParticleEmitter drops = new ParticleEmitter(200, 50, RAINDROP, 2000){

		@Override
		public void makeParticle(Particle p) {
			p.pos.set(pos.x + random.nextInt((int)size.x), pos.y + random.nextInt((int)size.y));
			p.vel.set((random.nextFloat() - 0.5f)*0.01f, -0.2f);//-0.8f
			p.col.set(0.8f, 0.8f, 0.8f, 0.4f);
			p.rad = 1;
			p.live = startLife;
		}

		@Override
		public void velocityInterpolator(Particle p) {
			p.vel.y -= 0.01f;
			collision(p);
		}

		@Override
		public void colorInterpolator(Particle p) {}

		@Override
		public void rotationInterpolator(Particle p) {}

		@Override
		public void radiusInterpolator(Particle p) {}
		
		public void collision(Particle p){
//			for(Material mat : Material.values()){//	iterate materials
//				if(mat.solid){
//					for(Node c : WorldWindow.sectors[1].areas[mat.ordinal()].cycles){//	iterate lines
//						Node n = c;
//						 do {
//							n = n.next;
//							if(Line2D.linesIntersect(p.pos.x, p.pos.y, pos.plus(p.vel).x, pos.plus(p.vel).y, n.last.p.x, n.last.p.y, n.p.x, n.p.y)){
//								p.live = 0;//TODO destroy drop with other particles/little animation
//								p.vel.scale(-2f);
//								break;
//							}
//						} while (n != c);
//					}
//				}
//			}
		}
	};

	public Vec pos;
	public Vec size = new Vec();
	
	public RainEffect(Vec vec, float width, float height){
		this.pos = new Vec(vec);
		size.set(width, height);
	}
	
	@Override
	public void tick(float dTime){
		drops.tick(dTime);
	}
	
	@Override
	public void render(){
		drops.render();
	}
	
	@Override
	public void finalize(){
		drops.finalize();
	}

	@Override
	public boolean living() {
		return false;
	}
	
}
