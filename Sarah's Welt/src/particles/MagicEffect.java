package particles;

import world.creatures.Creature;
import core.geom.Vec;

public class MagicEffect implements ParticleEffect{

	Vec pos;
	Vec dir;
	int live = 3000;
	Creature source;
	
	public MagicEffect(Vec pos, Vec dir, Creature source){
		this.pos = new Vec(pos);
		this.dir = new Vec(dir);
		this.source = source;
	}
	
	public void tick(float delta) {
		
	}

	public void render() {
		
	}

	public boolean living() {
		return false;
	}
	
	public void finalize(){
		
	}

}
