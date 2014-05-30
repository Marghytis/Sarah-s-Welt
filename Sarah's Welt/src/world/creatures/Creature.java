package world.creatures;

import item.DistantWeapon;
import item.Item;
import item.Weapon;
import util.Animator;
import world.Material;
import world.Node;
import world.Thing;
import world.WorldView;
import core.geom.Vec;

public abstract class Creature extends Thing {
	
	protected Vec acc = new Vec();
	public Vec vel = new Vec();
	
	//combat
	public int hit = 0;
	public int hitradius = 100; //only relevant, if its aggressive
	public int health = 20;
	public int punchStrength = 1;
	public int id;
	
	public Creature(Animator ani, Vec pos, Node worldLink, int id){
		super(ani, pos, worldLink);
		this.id = id;
	}
	
	public void update(int dTime){
		if(health <= 0){
//			World.particleEffects.add(new DeathDust(pos.plus(animator.box.middle())));
//			World.thingTasks.add(() -> {
//				onDeath();
//				World.creatures[id].remove(this);
//			});
		} else {
			pos.shift(vel);
			
			vel.shift(acc.scaledBy(dTime).scaledBy(WorldView.measureScale*dTime));
			acc.set(0, 0);
			
			if(hit > 0) hit--;
		}
	}
	
	public boolean hitBy(Creature c, Item weapon){
		if(hit == 0 && (c.pos.minus(pos).length() < c.hitradius || weapon instanceof DistantWeapon)){
			hit = 40;
			if((c instanceof Sarah)){
				health -= weapon instanceof DistantWeapon ? ((DistantWeapon)weapon).distPower : ((Weapon)weapon).power;
			} else {
				health -= c.punchStrength;
			}
//			World.particleEffects.add(new BloodSplash(pos.plus(animator.box.middle())));
			return true;
		}
		return false;
	}
	
	/**
	 * Applies the friction of the specified material to the acceleration
	 * @param mat
	 */
	public void applyFriction(Material mat){
		acc.x -= mat.decelerationFactor*(vel.x*vel.x) * (vel.x > 0 ? 1 : -1);
		acc.y -= mat.decelerationFactor*(vel.y*vel.y) * (vel.y > 0 ? 1 : -1);
	}
	
	@Override
	protected void beforeRender(){
		if(vel.x > 0){
			mirrored = true;
		} else if(vel.x < 0){
			mirrored = false;
		}
	}
	
	protected void onDeath(){}
}
