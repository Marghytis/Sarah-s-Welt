package world.creatures;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Material;
import world.Node;
import core.geom.Vec;


public class Bird extends WalkingCreature{

	public static int typeId;

	public static Animation sit1 = new Animation(0, 0);
	public static Animation flap1 = new Animation(5, 0, true, 1, 2, 3, 4, 3, 2, 1);
	public static Animation sit2 = new Animation(0, 1);
	public static Animation flap2 = new Animation(5, 1, true, 1, 2, 3, 4, 3, 2, 1);
	
	public int type;
	
	public Bird(int type, Vec p, Node worldLink, int frame){
		super(new Animator(Res.BIRD, type == 0 ? flap1 : flap2), p, worldLink);
		front = true;
		health = 5;
		animator.frame = frame;
		this.type = type;
	}
	
	int dir = 1;
	
	public void update(int dTime){
		if(!g){
			acc.add(dir*0.00001f, (0.5f - random.nextFloat())*0.00001f);
			applyFriction(Material.AIR);
		} else {
			if(random.nextInt(300) == 0){
				animator.setAnimation(type == 0 ? flap1 : flap2);
				pos.y++;
				accelerateFromGround(new Vec(0, 0.0001f));
				dir = random.nextBoolean() ? 1 : -1;
			}
		}
		
		if(!g){
			if(collision()){
				animator.setAnimation(type == 0 ? sit1 : sit2);
			}
		}
		
		super.update(dTime);
	}

	public boolean hitBy(Creature c){
		if(super.hitBy(c)){
			animator.setAnimation(type == 0 ? flap1 : flap2);
			return true;
		} else {
			return false;
		}
	}
}
