package world.creatures;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Material;
import world.Node;
import world.Point;


public class Bird extends WalkingCreature{

	public static int typeId;
	
	public static Animation sit = new Animation(0, 0);
	public static Animation flap = new Animation(5, 0, true, 1, 2, 3, 4, 3, 2, 1);
	
	public Bird(Point p, Node worldLink, int frame){
		super(new Animator(Res.BIRD, flap), p, worldLink);
		front = true;
		health = 5;
		animator.frame = frame;
	}
	
	public void update(int dTime){
		if(!g){
			acc.add((0.5f - random.nextFloat())*0.00008f, (0.5f - random.nextFloat())*0.00008f);
			applyFriction(Material.AIR);
		} else {
			if(random.nextInt(300) == 0){
				animator.setAnimation(flap);
				pos.y++;
				accelerateFromGround(new Point(0, 0.0001f));
			}
		}
		
		if(!g){
			if(collision()){
				animator.setAnimation(sit);
			}
		}
		
		super.update(dTime);
	}
}
