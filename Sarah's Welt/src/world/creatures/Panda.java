package world.creatures;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Node;
import world.World;
import core.Settings;
import core.geom.Vec;

public class Panda extends Creature {

	static Animation sit = new Animation(0, 0);
	static Animation punch = new Animation(3, 0, false, 1, 2, 3, 4, 5);
	static Animation hitt = new Animation(0, 1);

	
	public Panda(Vec p, Node worldLink){
		super(new Animator(Res.PANDA, sit), p, worldLink, true, CreatureType.PANDA);
		hitradius = 50;
		animator.doOnReady = () -> donePunch();
		coinDrop = 3;
	}
	
	@Override
	public void update(float dTime){
		if(Settings.agro) findSarah();
		super.update(dTime);
	}
	
	int dir = 0;
	boolean agro = false;
	
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
	
	@Override
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

	public static Creature createNewCreature(float x, float y, float vX, float vY, int health, Node worldLink, boolean front, String metaString){

		Panda p = new Panda(new Vec(x, y), worldLink);
		p.vel.set(vX, vY);
		p.health = health;
		return p;
	}

	@Override
	public String createMetaString() {
		return "";
	}
}
