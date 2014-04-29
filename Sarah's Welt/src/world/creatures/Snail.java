package world.creatures;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Material;
import world.Node;
import world.Point;
import world.WorldWindow;
import world.structures.Cloud;
import world.structures.Structure;
import core.Settings;

public class Snail extends WalkingCreature {

	public static int typeId;
	
	static Animation walk = new Animation(10, 0, true,	0, 1, 2, 3, 4, 3, 2, 1);
	static Animation stand = new Animation(0, 0);
	static Animation punch = new Animation(3, 1, false, 1, 2, 3, 4, 5, 6, 5);
	static Animation hitt = new Animation(0, 2);

	
	public Snail(Point p, Node worldLink){
		super(new Animator(Res.SNAIL, stand), p, worldLink);
		hitradius = 50;
		animator.doOnReady = () -> donePunch();
		front = true;
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
	boolean agro = false;
	public void walkingAI(float dTime){
		if((!Settings.agro || !findSarah()) && !findNextCloud())wanderAbout();
		
		applyDirection(dir);
		doStepping(velocityUnit*vP*dTime);
	}
	
	public void donePunch(){
		WorldWindow.sarah.hitBy(this);
		animator.setAnimation(stand);
	}
	
	public boolean findSarah(){
		if(pos.minus(WorldWindow.sarah.pos).length() < 150){
			if(WorldWindow.sarah.pos.x + WorldWindow.sarah.animator.tex.box.x > pos.x){
				dir = 1;
			} else if(WorldWindow.sarah.pos.x + WorldWindow.sarah.animator.tex.box.x + WorldWindow.sarah.animator.tex.box.size.x < pos.x){
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
	
	public boolean findNextCloud(){
		Cloud find = null; float distance = 1000;
		for(Structure c : Structure.structures.get(Cloud.typeId)){
			float dist = pos.minus(c.pos).length();
			if(dist < distance){
				find = (Cloud)c;
				distance = dist; 
			}
		}
		if(find != null){
			if(find.pos.x-10 > pos.x){
				dir = 1;
			} else if(find.pos.x+10 < pos.x){
				dir = -1;
			} else {
				dir = 0;
			}
			maxSpeed = 6;
			return true;
		} else {
			maxSpeed = 3;
			return false;
		}
	}
	
	public void wanderAbout(){
		if(random.nextInt(100)<1){
			dir = random.nextInt(3)-1;
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

}
