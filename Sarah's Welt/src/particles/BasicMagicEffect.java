package particles;

import item.Item;

import java.util.List;

import particles.Particle.ParticleType;
import resources.TextureFile;
import world.World;
import world.WorldView;
import world.creatures.Creature;
import world.creatures.Gnat;
import core.geom.Vec;

public class BasicMagicEffect extends MagicEffect{
	
	public static final ParticleType LIGHT = new ParticleType(new TextureFile("particles/Fire", -0.5f, -0.5f));
	
	public ParticleEmitter light = new ParticleEmitter(1, 0, LIGHT, 1000){
			
		@Override
		public void makeParticle(Particle p) {
			p.pos.set(pos.x, pos.y);
			p.vel.set(dir);
			p.col.set(1, 0, 0);
			p.live = startLife;
			p.rad = 0.3f;
		}

		@Override
		public void velocityInterpolator(Particle p) {
			for(List<Creature> list : World.creatures) for(Creature c : list){
				if(!(c instanceof Gnat) && c.animator.box.plus(c.pos).intersects(type.tex.box.scaledBy(p.rad).plus(p.pos))){
					c.hitBy(source, Item.horn);
					p.live = 0;
					WorldView.particleEffects.add(new BasicMagicDissapperance(c.pos));
					break;
				}
			}
		}
		
		@Override
		public void colorInterpolator(Particle p){
			p.col.a = (float) p.live /startLife;
		}
		
		@Override
		public void killParticle(Particle p){
			sparkle.emitting = false;
		}
	};

	public static final ParticleType SPARKLE = new ParticleType(new TextureFile("particles/Sparkle"));
	
	public ParticleEmitter sparkle = new ParticleEmitter(100, 20, SPARKLE, 3000){

		@Override
		public void makeParticle(Particle p) {
			p.pos.set(light.particles[0].pos.x, light.particles[0].pos.y);
			float angle = random.nextFloat()*(float)(Math.PI*2);
			
			p.vel.set((float)Math.cos(angle)*(random.nextFloat()*0.3f), (float)Math.sin(angle)*(random.nextFloat()*0.3f) + 0.1f);//-0.8f
			p.col.set(1f, 0.7f, 0.0f, light.particles[0].col.a);
			p.rad = 0.3f;
			p.live = startLife;
		}

		@Override
		public void velocityInterpolator(Particle p) {
			p.vel.x *= 0.97f;
			p.vel.y *= 0.97f;
		}

		@Override
		public void colorInterpolator(Particle p) {
			p.col.a -= 0.01f;
		}

	};
	
	public BasicMagicEffect(Vec pos, Vec dir, Creature source){
		super(pos, new Vec(dir).scale(0.4f), source);
		light.emittParticle(0);
	}
	
	@Override
	public void tick(float delta) {
		light.tick(delta);
		sparkle.tick(delta);
		live -= delta;
	}

	@Override
	public void render() {
		light.render();
		sparkle.render();
	}
	
	@Override
	public void finalize(){
		light.finalize();
		sparkle.finalize();
	}

	@Override
	public boolean living() {
		return live > 0;
	}

}
