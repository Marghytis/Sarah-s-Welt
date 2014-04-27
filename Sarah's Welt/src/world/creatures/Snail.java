package world.creatures;

import main.Settings;
import resources.StackedTexture;
import util.Animation;
import world.Material;
import world.Node;
import world.Point;
import world.Sector;
import world.WorldWindow;
import world.structures.Cloud;
import world.structures.Structure;

public class Snail extends WalkingCreature {

	public static StackedTexture STAND_WALK  = new StackedTexture("creatures/Snail_", 5, 1, -0.5f, -0.1f);
		static Animation walk = new Animation(10, 0, true,	0, 1, 2, 3, 4, 3, 2, 1);
		static Animation stand = new Animation(0, 0);
	public static StackedTexture BEAT_HIT  = new StackedTexture("creatures/Snail_beats", 8, 1, -0.5f, -0.1f);
		static Animation punch = new Animation(3, 0, false, 1, 2, 3, 4, 5, 6, 5);
		static Animation hitt = new Animation(7, 0);

	
	public Snail(Point p, Node worldLink){
		super(STAND_WALK, stand, p, worldLink);
		hitradius = 50;
		animator.doOnReady = () -> donePunch();
		front = true;
	}
	
	public void tick(float dTime){
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
		super.tick(dTime);
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
		animator.setAnimation(stand); tex = STAND_WALK;
	}
	
	public boolean findSarah(){
		if(pos.minus(WorldWindow.sarah.pos).length() < 150){
			if(WorldWindow.sarah.pos.x + WorldWindow.sarah.tex.box.x > pos.x){
				dir = 1;
			} else if(WorldWindow.sarah.pos.x + WorldWindow.sarah.tex.box.x + WorldWindow.sarah.tex.box.width < pos.x){
				dir = -1;
			} else {
				dir = 0;
				animator.setAnimation(punch); tex = BEAT_HIT;
			}
			maxSpeed = 6;
			return true;
		} else {
			maxSpeed = 3;
			return false;
		}
	}
	
	public boolean findNextCloud(){
		Cloud c = null; float distance = 1000;
		for(Structure s : WorldWindow.structures){
			if(s instanceof Cloud){
				float dist = pos.minus(s.pos).length();
				if(dist < distance){
					c = (Cloud)s;
					distance = dist; 
				}
			}
		}
		if(c != null){
			if(c.pos.x-10 > pos.x){
				dir = 1;
			} else if(c.pos.x+10 < pos.x){
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
			tex = BEAT_HIT;
			animator.setAnimation(hitt); tex = BEAT_HIT;
		} else if(!animator.animation.equals(punch)){
			tex = STAND_WALK;
			if(vP != 0){
				animator.setAnimation(walk); tex = STAND_WALK;
			} else {
				animator.setAnimation(stand); tex = STAND_WALK;
			}
		}
	}

}
