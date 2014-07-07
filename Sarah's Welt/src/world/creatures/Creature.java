package world.creatures;

import item.DistantWeapon;
import item.Item;
import item.Weapon;
import particles.BloodSplash;
import particles.DeathDust;
import util.Animator;
import world.Coin;
import world.Material;
import world.Node;
import world.Thing;
import world.World;
import world.WorldView;
import core.geom.Vec;

public abstract class Creature extends Thing {
	
	public CreatureType type;
	
	protected Vec acc = new Vec();
	public Vec vel = new Vec();
	
	//combat
	public int hit = 0;
	public int hitradius = 100; //only relevant, if its aggressive
	public int health = 20;
	public int punchStrength = 1;
	
	public Creature(Animator ani, Vec pos, Node worldLink, boolean front, CreatureType type){
		super(ani, pos, worldLink, front);
		this.type = type;
	}
	
	@Override
	public void update(float delta){
		if(health <= 0){
			WorldView.particleEffects.add(new DeathDust(pos.plus(animator.box.middle())));
			WorldView.thingTasks.add(() -> {
				onDeath();
				if(type != null) World.creatures[type.ordinal()].remove(this);
			});
		} else {
			pos.shift(vel);
			
			vel.shift(acc.scaledBy(delta).scaledBy(World.measureScale*delta));
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
	
	protected void onDeath(){
		for(int i = 0; i < 10; i++){
			World.creatures[CreatureType.COIN.ordinal()].add(new Coin(pos.x, pos.y, (0.5f - random.nextFloat())*10f, (random.nextFloat())*30f));
		}
	}
	
	public enum CreatureType {
		SNAIL(Snail::createNewCreature),
		BUTTERFLY(Butterfly::createNewCreature),
		HEART(Heart::createNewCreature),
		RABBIT(Rabbit::createNewCreature),
		BIRD(Bird::createNewCreature),
		PANDA(Panda::createNewCreature),
		COW(Cow::createNewCreature),
		GNAT(Gnat::createNewCreature),
		UNICORN(Unicorn::createNewCreature),
		SCORPION(Scorpion::createNewCreature),
		TREX(Trex::createNewCreature),
		ZOMBIE(Zombie::createNewCreature),
		GIANT_CAT(GiantCat::createNewCreature),
		COIN(Coin::createNewCreature),
		;
		
		public Creator create;
		
		CreatureType(Creator create){
			this.create = create;
		}
		
		public interface Creator {public abstract Creature create(float x, float y, float vX, float vY, int health, Node worldLink, boolean front, String metaString);}
	}
}
