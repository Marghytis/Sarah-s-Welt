package world.creatures;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Material;
import world.Node;
import world.World;
import core.Settings;
import core.geom.Vec;

public class Rabbit extends WalkingCreature {
	
	static Animation stand = new Animation(0, 0);
	static Animation hitt = new Animation(2, 0);
	static Animation walk = new Animation(10, 0, true, 1, 2, 3, 4, 3, 2);
	static Animation punch = new Animation(5, 1, false, 1, 2, 3, 4, 1);
	
	public Rabbit(Vec p, Node worldLink){
		super(new Animator(Res.RABBIT, stand), p, worldLink, false, CreatureType.RABBIT);
//		hitradius = 50;
		maxSpeed = 5;
		animator.doOnReady = () -> donePunch();
		health = 10;
		punchStrength = 1;
		coinDrop = 6;
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
	@Override
	public void walkingAI(float dTime){
		if((!Settings.agro || !findSarah()))wanderAbout();
		applyDirection(dir);
		doStepping(velocityUnit*vP*dTime);
	}
	
	public void donePunch(){
		World.sarah.hitBy(this, null);
		animator.setAnimation(stand);
	}
	
	public void wanderAbout(){
		if(random.nextInt(100)<1){
			dir = random.nextInt(3)-1;
		}
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
		if(g)alignWithGround();
	}

	public static Creature createNewCreature(float x, float y, float vX, float vY, int health, Node worldLink, boolean front, String metaString){

		Rabbit r = new Rabbit(new Vec(x, y), worldLink);
		r.vel.set(vX, vY);
		r.health = health;
		return r;
	}

	@Override
	public String createMetaString() {
		return "";
	}
}
