package world.creatures;

import resources.StackedTexture;
import world.Material;
import world.Node;
import world.Point;
import world.Sector;
import world.WorldWindow;
import world.structures.Cloud;
import world.structures.Structure;

public class Snail extends WalkingCreature {

	public static StackedTexture STAND_WALK  = new StackedTexture("snail_", 5, 1, -0.5f, -0.1f);
	public static StackedTexture BEAT_HIT  = new StackedTexture("snail_beats", 8, 1, -0.5f, -0.1f);
	
	static int[] walk = {0, 1, 2, 3, 4, 3, 2, 1}; int cWalk = 0;
	
	public Snail(Point p, Node worldLink){
		super(STAND_WALK, p, worldLink);
		hitradius = 50;
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
		if(!findSarah() && !findNextCloud())wanderAbout();
		
		applyDirection(dir);
		doStepping(velocityUnit*vP*dTime);
	}
	
	public boolean findSarah(){
		if(pos.minus(WorldWindow.sarah.pos).length() < 150){
			if(WorldWindow.sarah.pos.x + WorldWindow.sarah.box.x > pos.x){
				dir = 1;
			} else if(WorldWindow.sarah.pos.x + WorldWindow.sarah.box.x + WorldWindow.sarah.box.width < pos.x){
				dir = -1;
			} else {
				dir = 0;//TODO punch
				WorldWindow.sarah.hitBy(this);
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
		for(Sector sec: WorldWindow.sectors){
			for(Structure s : sec.structures){
				if(s instanceof Cloud){
					float dist = pos.minus(s.pos).length();
					if(dist < distance){
						c = (Cloud)s;
						distance = dist; 
					}
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
	
	protected void howToRender(){
		super.howToRender();
		
		if(hit > 0){
			tex = BEAT_HIT;
			hit--;
			frameX = 7;
			frameY = 0;
		} else {
			tex = STAND_WALK;
			frameY = 0;
			if(vP != 0){
				frameX = cWalk/10;
				
				cWalk++;
				if(cWalk/10 >= walk.length){
					cWalk = 0;
				}
			} else {
				frameX = 0;
			}
		}
	}

}
