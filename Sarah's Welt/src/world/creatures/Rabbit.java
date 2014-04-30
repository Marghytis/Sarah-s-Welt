package world.creatures;

import core.Settings;
import resources.Res;
import util.Animation;
import util.Animator;
import world.Material;
import world.Node;
import world.Point;
import world.WorldWindow;

public class Rabbit extends WalkingCreature {

	public static int typeId;
	
	static Animation stand = new Animation(0, 0);
	static Animation hitt = new Animation(2, 0);
	static Animation walk = new Animation(10, 0, true, 1, 2, 3, 4, 3, 2);
	static Animation punch = new Animation(10, 1, true, 1, 2, 3, 4, 1);
	
	public Rabbit(Point p, Node worldLink){
		super(new Animator(Res.RABBIT, stand), p, worldLink);
		maxSpeed = 5;
		health = 10;
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
	
	public void wanderAbout(){
		if(random.nextInt(100)<1){
			dir = random.nextInt(3)-1;
		}
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
	
	protected void beforeRender(){
		super.beforeRender();
		
		if(hit > 0){
			animator.setAnimation(hitt);
			hit--;
		} else {
			if(vP != 0){
				animator.setAnimation(walk);
			} else {
				animator.setAnimation(stand);
			}
		}
	}
}
