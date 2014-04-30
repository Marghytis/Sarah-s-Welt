package world.creatures;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Material;
import world.Node;
import world.Point;


public class Butterfly extends WalkingCreature{

	public static int typeId;
	
	public static Animation flap1 = new Animation(5, 0, true, 0, 1, 2, 3, 2, 1);
	public static Animation flap2 = new Animation(5, 1, true, 0, 1, 2, 3, 2, 1);
	
	public Butterfly(int type, Point p, Node worldLink, int frame){
		super(new Animator(Res.BUTTERFLY, type == 0 ? flap1 : flap2), p, worldLink);
		front = true;
		health = 5;
		animator.frame = frame;
	}
	
	public void update(int dTime){
		if(!g){
			acc.add((0.5f - random.nextFloat())*0.00003f, (0.5f - random.nextFloat())*0.00003f);
			applyFriction(Material.AIR);
		} else {
			if(random.nextInt(100) == 0){
				pos.y++;
				accelerateFromGround(new Point(0, 0.0001f));
			}
		}
		
		if(!g) collision();
		
		super.update(dTime);
	}
}
