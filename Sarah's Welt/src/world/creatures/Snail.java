package world.creatures;

import util.Quad;
import world.Material;
import world.Node;
import world.Point;
import world.WorldWindow;

public class Snail extends WalkingCreature {
	static int[] walk = {0, 1, 2, 3, 4, 3, 2, 1}; int cWalk = 0;
	
	public Snail(Point p, Node worldLink){
		super(Creature.SNAIL, p, worldLink);
		maxSpeed = 5;
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
		if(pos.minus(WorldWindow.sarah.pos).length() < 10000){
			agro = true;
		} else {
			agro = false;
		}
		if(agro){
			if(WorldWindow.sarah.pos.x > pos.x){
				dir = 1;
			} else if(WorldWindow.sarah.pos.x < pos.x){
				dir = -1;
			}
			Quad sarah = new Quad(WorldWindow.sarah.box);
			sarah.x += WorldWindow.sarah.pos.x;
			sarah.y += WorldWindow.sarah.pos.y;
			Quad me = new Quad(box);
			me.x += pos.x;
			me.y += pos.y;
			if(me.contains(sarah.x, sarah.y) || me.contains(sarah.x + sarah.width, sarah.y + sarah.height) || sarah.contains(me.x, me.y) || sarah.contains(me.x + me.width, me.y + me.height)){
				dir = 0;//TODO hit
			}
		} else if(random.nextInt(100)<1){
			dir = random.nextInt(3)-1;
		}
		
		applyDirection(dir);
		doStepping(velocityUnit*vP*dTime);
	}
	
	protected void howToRender(){
		super.howToRender();
		
		
		if(vP != 0){
			frameX = cWalk/10;
			
			cWalk++;
			if(cWalk/10 >= walk.length){
				cWalk = 0;
			}
		} else {
			frameX = 0;
			frameY = 0;
		}
	}

}
