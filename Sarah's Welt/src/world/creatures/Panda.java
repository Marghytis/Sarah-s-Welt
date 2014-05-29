package world.creatures;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Material;
import world.Node;
import world.World;
import core.Settings;
import core.geom.Vec;

public class Panda extends WalkingCreature {

	public static int typeId;

	static Animation sit = new Animation(0, 0);
	static Animation punch = new Animation(3, 0, false, 1, 2, 3, 4, 5);
	static Animation hitt = new Animation(0, 1);

	
	public Panda(Vec p, Node worldLink){
		super(new Animator(Res.PANDA, sit), p, worldLink, typeId);
		hitradius = 50;
		animator.doOnReady = () -> donePunch();
		front = true;
	}
	
	public void update(int dTime){
		if(g){
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
	boolean agro = false;
	public void walkingAI(float dTime){
		if(Settings.agro) findSarah();
		
		applyDirection(dir);
		doStepping(velocityUnit*vP*dTime);
	}
	
	public void donePunch(){
		World.sarah.hitBy(this, null);
		animator.setAnimation(sit);
	}
	
	public boolean findSarah(){
		if(pos.minus(World.sarah.pos).lengthSqare() < 1600){
			animator.setAnimation(punch);
			return true;
		}
		return false;
	}
	
	protected void beforeRender(){
		super.beforeRender();
		
		if(pos.x < World.sarah.pos.x){
			mirrored = true;
		} else if(pos.x > World.sarah.pos.x){
			mirrored = false;
		}
		
		if(hit > 0){
			animator.setAnimation(hitt);
		} else if(!animator.animation.equals(punch)){
			animator.setAnimation(sit);
		}
	}

}
