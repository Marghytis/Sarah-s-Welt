package world.creatures;

import particles.Hearts;
import resources.Res;
import util.Animation;
import util.Animator;
import world.Node;
import world.World;
import world.WorldView;
import core.geom.Vec;

public class Heart extends WalkingCreature{

	static Animation flap = new Animation(10, 0, true, 0, 1, 2, 3, 2, 1);
	
	int variant;

	public Heart(int variant, Vec pos, Node worldLink) {
		super(new Animator(Res.HEART, flap), pos, worldLink, false, CreatureType.HEART);
		vel.y = -0.2f;
		health = 2000;
		this.variant = variant;
	}
	
	@Override
	public void update(float dTime){
		if(!g){
			collision(false);
		}
		health--;
		super.update(dTime);
	}
	
	@Override
	public boolean rightClickAction(){
		if(pos.minus(World.sarah.pos).lengthSqare() < 10000){
			WorldView.particleEffects.add(new Hearts(pos.plus(animator.tex.box.middle())));
			World.sarah.health = Math.min(World.sarah.health += health/300, 30);
			health = 0;
			return true;
		}
		return false;
	}
	
	@Override
	public void beforeRender(){
		flap.y = variant;
	}

	public static Creature createNewCreature(float x, float y, float vX, float vY, int health, Node worldLink, boolean front, String metaString){

		int variant = Integer.parseInt(metaString);
		
		Heart h = new Heart(variant, new Vec(x, y), worldLink);
		h.vel.set(vX, vY);
		h.health = health;
		return h;
	}

	@Override
	public String createMetaString() {
		return variant + "";
	}
}
