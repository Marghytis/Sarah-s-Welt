package world.creatures;

import org.lwjgl.opengl.GL11;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Material;
import world.Node;
import world.World;
import core.geom.Vec;

public class Cow extends WalkingCreature{

	public static int typeId;
	
	static Animation chew = new Animation(5, 0, true, 0, 1, 2, 3, 4, 5, 6);
	
	public Cow(Vec p, Node worldLink){
		super(new Animator(Res.COW, chew), p, worldLink);
		maxSpeed = 5;
		health = 10;
		punchStrength = 1;
	}
	
	public void update(int dTime){
		if(g){
//			walkingAI(dTime);
		} else {
			acc.shift(0, -0.00005f);
			applyFriction(Material.AIR);
			
			//do movement in air
			collision();
		}
		super.update(dTime);
	}
	
	int dir = 0;
	public void walkingAI(float dTime){
		wanderAbout();
		applyDirection(dir);
		doStepping(velocityUnit*vP*dTime);
	}
	
	public boolean rightClickAction(){
		if(pos.minus(World.sarah.pos).lengthSqare() < 400){
			World.sarah.mountCow(this);
			return true;
		}
		return false;
	}
	
	public void wanderAbout(){
		if(random.nextInt(100)<1){
			dir = random.nextInt(3)-1;
		}
	}
	
	public void beforeRender(){
		if(g)GL11.glRotatef(worldLink.getPoint().minus(worldLink.getNext().getPoint()).angle()*(180/(float)Math.PI), 0, 0, 1);//worldLink.p.minus(worldLink.getNext().p).angle()
	}
}
