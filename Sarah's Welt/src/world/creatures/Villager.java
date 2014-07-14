package world.creatures;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Material;
import world.Node;
import world.World;
import core.geom.Vec;

public class Villager extends WalkingCreature{
	
	public int variant;
	
	public Villager(Vec p, Node worldLink, int variant){
		super(new Animator(Res.VILLAGER, new Animation()), p, worldLink, false, CreatureType.VILLAGER);
		maxSpeed = 5;
		health = 10;
		punchStrength = 1;
		coinDrop = 0;
		this.variant = variant;
	}
	
	@Override
	public void update(float dTime){
		if(!g){
			acc.shift(0, -0.00005f);
			applyFriction(Material.AIR);
			
			//do movement in air
			collision(true);
		}
		super.update(dTime);
	}
	
	public void beforeRender(){
		animator.animation.y = variant;
	}
	
	public boolean rightClickAction(){
		if(pos.minus(World.sarah.pos).lengthSqare() < 400){
//			World.sarah.mountCow(this);
			return true;
		}
		return false;
	}

	public static Creature createNewCreature(float x, float y, float vX, float vY, int health, Node worldLink, boolean front, String metaString){

		Villager c = new Villager(new Vec(x, y), worldLink, Integer.parseInt(metaString));
		c.vel.set(vX, vY);
		c.health = health;
		return c;
	}

	@Override
	public String createMetaString() {
		return variant + "";
	}
}
