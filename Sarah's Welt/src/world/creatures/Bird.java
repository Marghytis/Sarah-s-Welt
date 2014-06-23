package world.creatures;

import item.Weapon;
import resources.Res;
import util.Animation;
import util.Animator;
import world.Material;
import world.Node;
import core.geom.Vec;


public class Bird extends WalkingCreature{

	public static int typeId;

	public static Animation sit = new Animation(0, 0);
	public static Animation flap = new Animation(5, 0, true, 1, 2, 3, 4, 3, 2, 1);
	
	public int variant;
	
	public Bird(int variant, Vec p, Node worldLink, int frame){
		super(new Animator(Res.BIRD, flap), p, worldLink, typeId);
		front = true;
		health = 5;
		animator.frame = frame;
		this.variant = variant;
	}
	
	public void beforeRender(){
		super.beforeRender();
		animator.animation.y = variant;
	}
	
	int dir = 1;
	
	public void update(int dTime){
		if(!g){
			acc.shift(dir*0.00001f, (0.5f - random.nextFloat())*0.00001f);
			applyFriction(Material.AIR);
		} else {
			if(random.nextInt(300) == 0){
				animator.setAnimation(flap);
				pos.y++;
				accelerateFromGround(new Vec(0, 0.0001f));
				dir = random.nextBoolean() ? 1 : -1;
			}
		}
		
		if(!g){
			if(collision()){
				animator.setAnimation(sit);
			}
		}
		
		super.update(dTime);
	}

	public boolean hitBy(Creature c, Weapon w){
		if(super.hitBy(c, w)){
			animator.setAnimation(flap);
			return true;
		} else {
			return false;
		}
	}
}
