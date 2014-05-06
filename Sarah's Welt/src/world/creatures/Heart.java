package world.creatures;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Node;
import world.World;
import core.geom.Vec;

public class Heart extends WalkingCreature{

	public static int typeId;
	
	static Animation flap = new Animation(10, 0, true, 0, 1, 2, 3, 2, 1);

	public Heart(Vec pos, Node worldLink) {
		super(new Animator(Res.HEART, flap), pos, worldLink);
		vel.y = -0.2f;
		health = 2000;
	}
	
	public void update(int dTime){
		if(pos.minus(World.sarah.pos).lengthSqare() < 400){
			World.sarah.health = Math.min(World.sarah.health += 5, 20);
		}
		if(!g){
			collision();
		}
		health--;
		super.update(dTime);
	}
}
