package particles;

import particles.Particle.ParticleType;
import resources.Texture;
import util.Color;
import world.World;
import world.creatures.Unicorn;
import core.geom.Vec;

public class RainbowSpit implements ParticleEffect{
	
	public static final ParticleType RAINBOW = new ParticleType(new Texture("particles/RainbowParticle"));
	
	public ParticleEmitter rainbow = new ParticleEmitter(210, 200, RAINBOW, 1000){
		
		int color = 0;
		
		Color[] colors = {
				new Color(1, 0, 0),	
				new Color(1, 0.5f, 0),	
				new Color(1, 1, 0),	
				new Color(0, 1, 0),	
				new Color(0, 0.7f, 1),	
				new Color(0.6f, 0, 1),	
		};
		
		float[] speedY = {
				0.06f,
				0.05f,
				0.04f,
				0.03f,
				0.02f,
				0.01f,
		};
		
		public void makeParticle(Particle p) {
			p.pos.set(pos.x, pos.y + (-color*2));
			p.vel.set(source.mirrored ? 0.2f : -0.2f, speedY[color]);
			p.col.set(colors[color]);
			p.rot = random.nextInt(3)-1;
			p.live = startLife;
			color = (color+1)%6;
		}

		public void velocityInterpolator(Particle p) {
			p.vel.y -= 0.002f;
			if(World.sarah != null){
				if(World.sarah.animator.tex.box.plus(World.sarah.pos).contains(p.pos)){
					World.sarah.hitBy(source);
				}
			}
		}

		public void colorInterpolator(Particle p) {
			p.col.a = (float) (p.live) /startLife;
		}

		public void rotationInterpolator(Particle p) {
			if(p.rot > 0){
				p.rot = ((float)Math.PI/10)*p.live;
			} else {
				p.rot = -((float)Math.PI/10)*p.live;
			}
		}
		
		public void radiusInterpolator(Particle p){
			p.rad = 1 + ((float)p.live/startLife*0.5f);
		}
	};
	
	Vec pos;
	int live = 1500;
	Unicorn source;
	
	public RainbowSpit(float x, float y, Unicorn source){
		pos = new Vec(x, y);
		this.source = source;
	}
	
	public void tick(int delta) {
		if(live < 1000){
			rainbow.emitting = false;
		}
		rainbow.tick(delta);
		live -= delta;
	}

	public void render() {
		rainbow.render();
	}
	
	public void finalize(){
		rainbow.finalize();
	}

	public boolean living() {
		return live > 0;
	}

}
