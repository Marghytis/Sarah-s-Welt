package world.creatures;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Material;
import world.Node;
import core.geom.Vec;


public class Butterfly extends WalkingCreature{
	
	public static Animation[] flap = new Animation[]{
		new Animation(5, 0, true, 0, 1, 2, 3, 2, 1),
		new Animation(5, 1, true, 0, 1, 2, 3, 2, 1)};
	
	public int variant;

	public Butterfly(int variant, Vec p, Node worldLink){
		super(new Animator(Res.BUTTERFLY, flap[variant]), p, worldLink, true, CreatureType.BUTTERFLY);
		health = 5;
		animator.frame = random.nextInt(flap[0].sequence.length);
		this.variant = variant;
	}
	
	public void update(int dTime){
		if(!g){
			acc.shift((0.5f - random.nextFloat())*0.00003f, (0.49f - random.nextFloat())*0.00003f);
			applyFriction(Material.AIR);
		} else {
			if(random.nextInt(100) == 0){
				pos.y++;
				accelerateFromGround(new Vec(0, 0.0001f));
			}
		}
		
		if(!g) collision(false);
		
		super.update(dTime);
	}

	public static Creature createNewCreature(float x, float y, float vX, float vY, int health, Node worldLink, boolean front, String metaString){

		int variant = Integer.parseInt(metaString);
		
		Butterfly b = new Butterfly(variant, new Vec(x, y), worldLink);
		b.vel.set(vX, vY);
		b.health = health;
		return b;
	}

	public String createMetaString() {
		return variant + "";
	}
}
