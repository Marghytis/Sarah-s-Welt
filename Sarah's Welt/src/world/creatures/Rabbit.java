package world.creatures;

import resources.StackedTexture;
import world.Material;
import world.Node;
import world.Point;

public class Rabbit extends WalkingCreature {

	public static StackedTexture RABBIT  = new StackedTexture("rabbit", 5, 2, -0.5f, -0.2f);
	
	static int[] walk = {1, 2, 3, 4, 3, 2}; int cWalk = 0;
	
	public Rabbit(Point p, Node worldLink){
		super(RABBIT, p, worldLink);
		maxSpeed = 5;
		health = 10;
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
	public void walkingAI(float dTime){
		if(random.nextInt(100)<1){
			dir = random.nextInt(3)-1;
		}
		applyDirection(dir);
		doStepping(velocityUnit*vP*dTime);
	}
	
	protected void howToRender(){
		super.howToRender();
		
		if(hit > 0){
			frameX = 0;
			frameY = 1;
			hit--;
		} else {
			if(vP != 0){
				frameX = cWalk/10;
				
				cWalk++;
				if(cWalk/10 >= walk.length){
					cWalk = 0;
				}
			} else {
				frameX = 0;
			}
			frameY = 0;
		}
	}
}
