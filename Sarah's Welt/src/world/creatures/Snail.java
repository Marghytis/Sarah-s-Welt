package world.creatures;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Material;
import world.Node;
import world.WorldView;
import world.worldObjects.Cloud;
import world.worldObjects.WorldObject;
import core.Settings;
import core.geom.Vec;

public class Snail extends WalkingCreature {

	public static int typeId;
	
	static Animation walk = new Animation(10, 0, true,	0, 1, 2, 3, 4, 3, 2, 1);
	static Animation stand = new Animation(0, 0);
	static Animation punch = new Animation(3, 1, false, 1, 2, 3, 4, 5, 6, 5);
	static Animation hitt = new Animation(0, 2);

	
	public Snail(Vec p, Node worldLink){
		super(new Animator(Res.SNAIL, stand), p, worldLink, typeId);
		hitradius = 50;
		animator.doOnReady = () -> donePunch();
		front = true;
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
			collision(true);
		}
		super.update(dTime);
	}
	
	int dir = 0;
	boolean agro = false;
	public void walkingAI(float dTime){
		if((!Settings.agro || !findSarah()) && !findNextCloud())wanderAbout();
		
		applyDirection(dir);
		doStepping(velocityUnit*vP*dTime);
	}
	
	public void donePunch(){
		WorldView.sarah.hitBy(this, null);
		animator.setAnimation(stand);
	}
	
	public boolean findSarah(){
		if(pos.minus(WorldView.sarah.pos).lengthSqare() < 22500){
			if(WorldView.sarah.pos.x + WorldView.sarah.animator.tex.box.x > pos.x){
				dir = 1;
			} else if(WorldView.sarah.pos.x + WorldView.sarah.animator.tex.box.x + WorldView.sarah.animator.tex.box.size.x < pos.x){
				dir = -1;
			} else {
				dir = 0;
				animator.setAnimation(punch);
			}
			maxSpeed = 6;
			return true;
		} else {
			maxSpeed = 3;
			return false;
		}
	}
	
	public boolean findNextCloud(){
		Cloud find = null; float distanceSq = 1000000;
		for(WorldObject c : WorldView.worldObjects[Cloud.typeId]){
			float dist = pos.minus(c.pos).lengthSqare();
			if(dist < distanceSq){
				find = (Cloud)c;
				distanceSq = dist; 
			}
		}
		if(find != null){
			if(find.pos.x-10 > pos.x){
				dir = 1;
			} else if(find.pos.x+10 < pos.x){
				dir = -1;
			} else {
				dir = 0;
			}
			maxSpeed = 6;
			return true;
		} else {
			maxSpeed = 3;
			return false;
		}
	}
	
	public void wanderAbout(){
		if(random.nextInt(100)<1){
			dir = random.nextInt(3)-1;
		}
	}
	
	protected void beforeRender(){
		super.beforeRender();
		
		if(hit > 0){
			animator.setAnimation(hitt);
		} else if(!animator.animation.equals(punch)){
			if(vP != 0){
				animator.setAnimation(walk);
			} else {
				animator.setAnimation(stand);
			}
		}
	}

}
