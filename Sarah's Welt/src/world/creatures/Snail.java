package world.creatures;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Material;
import world.Node;
import world.World;
import world.worldObjects.Cloud;
import world.worldObjects.WorldObject;
import world.worldObjects.WorldObject.ObjectType;
import core.Settings;
import core.geom.Vec;

public class Snail extends WalkingCreature {
	
	static Animation walk = new Animation(10, 0, true,	0, 1, 2, 3, 4, 3, 2, 1);
	static Animation stand = new Animation(0, 0);
	static Animation punch = new Animation(3, 1, false, 1, 2, 3, 4, 5, 6, 5);
	static Animation hitt = new Animation(0, 2);

	
	public Snail(Vec p, Node worldLink){
		super(new Animator(Res.SNAIL, stand), p, worldLink, true, CreatureType.SNAIL);
		hitradius = 50;
		animator.doOnReady = () -> donePunch();
		front = true;
		coinDrop = 7;
	}
	
	@Override
	public void update(float dTime){
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
	@Override
	public void walkingAI(float dTime){
		if((!Settings.agro || !findSarah()) && !findNextCloud())wanderAbout();
		
		applyDirection(dir);
		doStepping(velocityUnit*vP*dTime);
	}
	
	public void donePunch(){
		World.sarah.hitBy(this, null);
		animator.setAnimation(stand);
	}
	
	public boolean findSarah(){
		if(pos.minus(World.sarah.pos).lengthSqare() < 22500){
			if(World.sarah.pos.x + World.sarah.animator.tex.box.x > pos.x){
				dir = 1;
			} else if(World.sarah.pos.x + World.sarah.animator.tex.box.x + World.sarah.animator.tex.box.size.x < pos.x){
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
		for(WorldObject c : World.worldObjects[ObjectType.CLOUD.ordinal()]){
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
	
	@Override
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

	public static Creature createNewCreature(float x, float y, float vX, float vY, int health, Node worldLink, boolean front, String metaString){

		Snail s = new Snail(new Vec(x, y), worldLink);
		s.vel.set(vX, vY);
		s.health = health;
		return s;
	}

	@Override
	public String createMetaString() {
		return "";
	}
}
