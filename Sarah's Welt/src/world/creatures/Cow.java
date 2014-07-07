package world.creatures;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Material;
import world.Node;
import world.World;
import core.geom.Vec;

public class Cow extends WalkingCreature{
	
	static Animation chew = new Animation(5, 0, true, 0, 1, 2, 3, 4, 5, 6);
	
	public Cow(Vec p, Node worldLink){
		super(new Animator(Res.COW, chew), p, worldLink, false, CreatureType.COW);
		maxSpeed = 5;
		health = 10;
		punchStrength = 1;
		coinDrop = 0;
	}
	
	@Override
	public void update(float dTime){
		if(g){
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
//		wanderAbout();
		applyDirection(dir);
		doStepping(velocityUnit*vP*dTime);
	}
	
	@Override
	public boolean rightClickAction(){
		if(pos.minus(World.sarah.pos).lengthSqare() < 400){
			World.sarah.mountCow(this);
			return true;
		}
		return false;
	}
	
	public void wanderAbout(){
		if(random.nextInt(100)<1){
			dir = random.nextInt(3)-1;
		}
	}
	
	@Override
	public void beforeRender(){
		if(g)alignWithGround();//worldLink.p.minus(worldLink.getNext().p).angle()
	}

	public static Creature createNewCreature(float x, float y, float vX, float vY, int health, Node worldLink, boolean front, String metaString){

		Cow c = new Cow(new Vec(x, y), worldLink);
		c.vel.set(vX, vY);
		c.health = health;
		return c;
	}

	@Override
	public String createMetaString() {
		return "";
	}
}
