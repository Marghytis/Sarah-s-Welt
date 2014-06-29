package world.creatures;

import item.Weapon;
import resources.Res;
import util.Animation;
import util.Animator;
import world.Material;
import world.Node;
import core.geom.Vec;


public class Bird extends WalkingCreature{

	public static int typeId;

	public static Animation sit = new Animation(0, 0);
	public static Animation flap = new Animation(5, 0, true, 1, 2, 3, 4, 3, 2, 1);
	
	public int variant;
	
	public Bird(int variant, Vec p, Node worldLink){
		super(new Animator(Res.BIRD, flap), p, worldLink, true, CreatureType.BIRD);
		health = 5;
		animator.frame = random.nextInt(flap.sequence.length);
		this.variant = variant;
	}
	
	public void beforeRender(){
		super.beforeRender();
		animator.animation.y = variant;
	}
	
	int dir = 1;
	
	public void update(int dTime){
		if(!g){
			acc.shift(dir*0.00001f, (0.5f - random.nextFloat())*0.00001f);
			applyFriction(Material.AIR);
		} else {
			if(random.nextInt(300) == 0){
				animator.setAnimation(flap);
				pos.y++;
				accelerateFromGround(new Vec(0, 0.0001f));
				dir = random.nextBoolean() ? 1 : -1;
			}
		}
		
		if(!g){
			if(collision(false)){
				animator.setAnimation(sit);
			}
		}
		
		super.update(dTime);
	}

	public boolean hitBy(Creature c, Weapon w){
		if(super.hitBy(c, w)){
			animator.setAnimation(flap);
			return true;
		} else {
			return false;
		}
	}

	public static Creature createNewCreature(float x, float y, float vX, float vY, int health, Node worldLink, boolean front, String metaString){

		int variant = Integer.parseInt(metaString);
		
		Bird b = new Bird(variant, new Vec(x, y), worldLink);
		b.vel.set(vX, vY);
		b.health = health;
		return b;
	}

	public String createMetaString() {
		return variant + "";
	}
}
