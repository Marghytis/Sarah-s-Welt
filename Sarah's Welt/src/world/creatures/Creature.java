package world.creatures;

import item.DistantWeapon;
import item.Item;
import item.Weapon;
import particles.BloodSplash;
import particles.DeathDust;
import util.Animator;
import world.Material;
import world.Node;
import world.Thing;
import world.World;
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
	
	public Creature(Animator ani, Vec pos, Node worldLink){
		super(ani, pos, worldLink);
	}
	
	public void update(int dTime){
		if(health <= 0){
			WorldView.particleEffects.add(new DeathDust(pos.plus(animator.box.middle())));
			WorldView.thingTasks.add(() -> {
				onDeath();
				World.creatures[World.creatureTypes.indexOf(getClass())].remove(this);
			});
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
			WorldView.particleEffects.add(new BloodSplash(pos.plus(animator.box.middle())));
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
