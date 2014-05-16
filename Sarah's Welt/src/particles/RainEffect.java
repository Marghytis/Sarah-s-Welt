package particles;

import particles.Particle.ParticleType;
import resources.Texture;
import core.geom.Vec;

public class RainEffect implements ParticleEffect{
	
	public static final ParticleType RAINDROP = new ParticleType(new Texture("particles/Raindrop"));
	
	public ParticleEmitter drops = new ParticleEmitter(200, 50, RAINDROP, 2000){

		public void makeParticle(Particle p) {
			p.pos.set(pos.x + random.nextInt((int)size.x), pos.y + random.nextInt((int)size.y));
			p.vel.set((random.nextFloat() - 0.5f)*0.01f, -0.2f);//-0.8f
			p.col.set(0.8f, 0.8f, 0.8f, 0.4f);
			p.rad = 1;
			p.live = startLife;
		}

		public void velocityInterpolator(Particle p) {
			p.vel.y -= 0.01f;
			collision(p);
		}

		public void colorInterpolator(Particle p) {}

		public void rotationInterpolator(Particle p) {}

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
