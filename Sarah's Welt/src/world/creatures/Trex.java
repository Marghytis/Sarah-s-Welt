package world.creatures;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Material;
import world.Node;
import world.World;
import core.Settings;
import core.geom.Vec;

public class Trex extends WalkingCreature {

	public static int typeId;
	
	static Animation stand = new Animation(0, 0);
	static Animation walk = new Animation(8, 0, true, 0, 1, 2, 3, 4, 5, 6, 7);
	static Animation punch1 = new Animation(5, 1 , false, 0, 1, 2, 3, 4, 3, 2, 1);
//	static Animation punch2 = new Animation(5, 2, false, 0, 1, 2, 3, 4, 5, 6, 7, 8);
//	static Animation chew = new Animation(5, 3, false, 0, 1, 2, 3);
	
	public Trex(Vec p, Node worldLink){
		super(new Animator(Res.TREX, stand), p, worldLink, typeId);
//		hitradius = 50;
		maxSpeed = 2;
		animator.doOnReady = () -> donePunch();
		health = 30;
		punchStrength = 5;
	}
	
	public void update(int dTime){
		if(g){
//			pos.y++;
//			accelerateFromGround(new Point(0, 0.001f));
			
			walkingAI(dTime);
		} else {
			acc.shift(0, -0.00005f);
			applyFriction(Material.AIR);
			
			//do movement in air
			collision();
		}
		super.update(dTime);
	}
	
	int dir = 0;
	public void walkingAI(float dTime){
		if((!Settings.agro || !findSarah()))wanderAbout();
		applyDirection(dir);
		doStepping(velocityUnit*vP*dTime);
	}
	
	public void donePunch(){
		World.sarah.hitBy(this);
		animator.setAnimation(stand);
	}
	
	public void wanderAbout(){
		if(random.nextInt(100)<1){
			dir = random.nextInt(3)-1;
		}
	}
	
	public boolean findSarah(){
		if(pos.minus(World.sarah.pos).lengthSqare() < 90000){
			if(World.sarah.pos.x + World.sarah.animator.tex.box.x > pos.x){
				dir = 1;
			} else if(World.sarah.pos.x + World.sarah.animator.tex.box.x + World.sarah.animator.tex.box.size.x < pos.x){
				dir = -1;
			} else {
				dir = 0;
				animator.setAnimation(punch1);
			}
			maxSpeed = 4;
			return true;
		} else {
			maxSpeed = 2;
			return false;
		}
	}
	
	protected void beforeRender(){
		super.beforeRender();
		
		if(hit > 0){
//			animator.setAnimation(hitt);
		} else if(!animator.animation.equals(punch1)){
			if(vP != 0){
				animator.setAnimation(walk);
			} else {
				animator.setAnimation(stand);
			}
		}
	}
}
