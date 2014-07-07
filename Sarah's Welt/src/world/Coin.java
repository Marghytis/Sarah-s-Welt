package world;

import item.Inventory;
import item.Item;
import resources.Res;
import util.Animation;
import util.Animator;
import world.creatures.Creature;
import world.creatures.WalkingCreature;
import core.geom.Vec;

public class Coin extends WalkingCreature{

	public Coin(Vec pos){
		this(pos, null, true);
	}
	
	public Coin(Vec pos, Node worldLink, boolean front) {
		super(new Animator(Res.COIN, new Animation()), pos, worldLink, front, CreatureType.COIN);
	}

	public Coin(float x, float y, float vX, float vY) {
		this(new Vec(x, y));
		this.vel.set(vX, vY);
	}
	
	public boolean hitBy(Creature src, Item weapon){
		return false;
	}
	
	public void onDeath(){}//must stay to not drop more coins
	
	public void update(float delta){
		if(!g){
			acc.shift(0, -0.00003f);
			applyFriction(Material.AIR);
			collision(true);
		}
		super.update(delta);
		
		if(pos.minus(World.sarah.pos).lengthSqare() < 400){
			WorldView.thingTasks.add(() -> World.creatures[CreatureType.COIN.ordinal()].remove(this));
			Inventory.coins++;
		}
	}

	public String createMetaString() {
		return "";
	}

	public static Creature createNewCreature(float x, float y, float vX, float vY, int health, Node worldLink, boolean front, String metaString){

		Coin g = new Coin(new Vec(x, y), worldLink, front);
		g.vel.set(vX, vY);
		return g;
	}
}
