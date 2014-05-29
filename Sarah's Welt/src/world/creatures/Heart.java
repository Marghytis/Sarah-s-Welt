package world.creatures;

import particles.Hearts;
import resources.Res;
import util.Animation;
import util.Animator;
import world.Node;
import world.World;
import core.geom.Vec;

public class Heart extends WalkingCreature{

	public static int typeId;

	static Animation flap = new Animation(10, 0, true, 0, 1, 2, 3, 2, 1);
	
	int variant;

	public Heart(int variant, Vec pos, Node worldLink) {
		super(new Animator(Res.HEART, flap), pos, worldLink, typeId);
		vel.y = -0.2f;
		health = 2000;
		this.variant = variant;
	}
	
	public void update(int dTime){
		if(!g){
			collision();
		}
		health--;
		super.update(dTime);
	}
	
	public boolean rightClickAction(){
		if(pos.minus(World.sarah.pos).lengthSqare() < 10000){
			World.particleEffects.add(new Hearts(pos.plus(animator.tex.box.middle())));
			World.sarah.health = Math.min(World.sarah.health += health/300, 30);
			health = 0;
			return true;
		}
		return false;
	}
	
	public void beforeRender(){
		flap.y = variant;
	}
}
