package world.projectiles;

import particles.BasicMagicEffect;
import world.Thing;
import world.creatures.Creature;
import core.geom.Vec;

public class BasicMagic extends Thing{

	public BasicMagic(Vec pos, Creature source) {
		super(null, pos, null);
		effect = new BasicMagicEffect(pos, source);
	}

	BasicMagicEffect effect;
	
	public void tick(int delta){
		effect.tick(delta);
	}
	
	public void render(){
		effect.render();
	}
	
}
