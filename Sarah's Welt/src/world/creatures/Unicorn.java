package world.creatures;

import org.lwjgl.opengl.GL11;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Material;
import world.Node;
import world.World;
import core.Settings;
import core.geom.Vec;

public class Unicorn extends WalkingCreature {

	public static int typeId;
	
	static Animation stand = new Animation(0, 0);
	static Animation hitt = new Animation(10, 2, true, 0, 1, 0);
	static Animation walk = new Animation(10, 0, true, 1, 2, 3, 4, 5, 4, 3, 2);
	static Animation punch = new Animation(5, 4, false, 0, 1, 2, 2, 2, 1, 0);
	
	public Unicorn(Vec p, Node worldLink){
		super(new Animator(Res.UNICORN, stand), p, worldLink);
//		hitradius = 50;
		maxSpeed = 5;
		animator.doOnReady = () -> donePunch();
		health = 10;
		punchStrength = 1;
	}
	
	public void update(int dTime){
		if(g){
//			pos.y++;
//			accelerateFromGround(new Point(0, 0.001f));
			
			walkingAI(dTime);
		} else {
			acc.add(0, -0.00005f);
			applyFriction(Material.AIR);
			
			//do movement in air
			collision();
		}
		super.update(dTime);
	}
	
	int dir = 0;
	public void walkingAI(float dTime){
		if((!Settings.agro || !findSarah()))wanderAbout();
		applyDirection(dir);
		doStepping(velocityUnit*vP*dTime);
	}
	
	public void donePunch(){
		World.sarah.hitBy(this);
		animator.setAnimation(stand);
	}
	
	public void wanderAbout(){
		if(random.nextInt(100)<1){
			dir = random.nextInt(3)-1;
		}
	}
	
	public boolean findSarah(){
		float headX;
		if(dir == 1){
			headX = pos.x + 50;
		} else if(dir == -1){
			headX = pos.x - 50;
		} else {
			headX = pos.x;
		}
		
		if(pos.minus(World.sarah.pos).lengthSqare() < 160000){
			if(World.sarah.pos.x + World.sarah.animator.tex.box.x > headX){
				dir = 1;
			} else if(World.sarah.pos.x + World.sarah.animator.tex.box.x + World.sarah.animator.tex.box.size.x < headX){
				dir = -1;
			} else {
				dir = 0;
				animator.setAnimation(punch);
			}
			maxSpeed = 6;
			return true;
		} else {
			maxSpeed = 3;
			return false;
		}
	}
	
	protected void beforeRender(){
		super.beforeRender();

		if(hit > 0){
			animator.setAnimation(hitt);
		} else if(!animator.animation.equals(punch)){
			if(vP != 0){
				animator.setAnimation(walk);
			} else {
				animator.setAnimation(stand);
			}
		}
	}
	
	public void afterRender(){
		animator.animation.y++;
		GL11.glColor3f(1, 0, 0);
		animator.animate(mirrored);
		GL11.glColor3f(1, 1, 1);
		animator.animation.y--;
	}
}
