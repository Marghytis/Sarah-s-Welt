package world.creatures;

import item.Inventory;
import item.Item;
import resources.Res;
import util.Animation;
import util.Animator;
import world.Material;
import world.Node;
import world.World;
import core.Settings;
import core.geom.Vec;

public class GiantCat extends WalkingCreature {
	
	static Animation stand = new Animation(0, 1);
	static Animation walk = new Animation(5, 0, true, 0, 1, 2, 3, 4);
	static Animation punch1 = new Animation(3, 1 , false, 2, 0, 1, 2);
	
	public GiantCat(Vec p, Node worldLink){
		super(new Animator(Res.GIANT_CAT, stand), p, worldLink, false, CreatureType.GIANT_CAT);
//		hitradius = 50;
		maxSpeed = 5;
		animator.doOnReady = () -> donePunch();
		health = 30;
		punchStrength = 5;
	}
	
	@Override
	public void update(float delta){
		if(g){
//			pos.y++;
//			accelerateFromGround(new Point(0, 0.001f));
			
			walkingAI(delta);
		} else {
			acc.shift(0, -0.00005f);
			applyFriction(Material.AIR);
			
			//do movement in air
			collision(true);
		}
		super.update(delta);
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
		if(pos.minus(World.sarah.pos).lengthSqare() < 90000){
			if(World.sarah.pos.x + World.sarah.animator.tex.box.x > pos.x){
				dir = 1;
			} else if(World.sarah.pos.x + World.sarah.animator.tex.box.x + World.sarah.animator.tex.box.size.x < pos.x){
				dir = -1;
			} else {
				dir = 0;
				animator.setAnimation(punch1);
			}
			maxSpeed = 4;
			return true;
		} else {
			maxSpeed = 2;
			return false;
		}
	}
	
	@Override
	protected void beforeRender(){
		super.beforeRender();
		
		if(hit > 0){
//			animator.setAnimation(hitt);
		} else if(!animator.animation.equals(punch1)){
			if(vP != 0){
				animator.setAnimation(walk);
			} else {
				animator.setAnimation(stand);
			}
		}
		alignWithGround();
	}
	
	public void onDeath(){
		Inventory.addItem(Item.axe);
	}

	public static Creature createNewCreature(float x, float y, float vX, float vY, int health, Node worldLink, boolean front, String metaString){

		GiantCat t = new GiantCat(new Vec(x, y), worldLink);
		t.vel.set(vX, vY);
		t.health = health;
		return t;
	}

	@Override
	public String createMetaString() {
		return "";
	}
}
