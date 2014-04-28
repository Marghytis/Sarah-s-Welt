package world.creatures;

import java.util.ArrayList;
import java.util.List;

import resources.StackedTexture;
import util.Animation;
import world.Material;
import world.Node;
import world.Point;

public class Rabbit extends WalkingCreature {
	
	public static List<Rabbit> l_i_s_t = new ArrayList<>();
	
	public static void updateAll(int dTime){
		for(int i = 0; i < l_i_s_t.size(); i++){
			Rabbit c = l_i_s_t.get(i);
			c.tick(dTime);
			if(c.health <= 0){
				l_i_s_t.remove(c);
			}
		}
	}
	
	public static void renderAll(){
		RABBIT.bind();
			l_i_s_t.forEach((b) -> b.render());
		RABBIT.release();
	}
	
	public static StackedTexture RABBIT  = new StackedTexture("creatures/Rabbit", 5, 2, -0.5f, -0.2f);
		static Animation stand = new Animation(0, 0);
		static Animation hitt = new Animation(2, 0);
		static Animation walk = new Animation(10, 0, true, 1, 2, 3, 4, 3, 2);
		static Animation punch = new Animation(10, 1, true, 1, 2, 3, 4, 1);
	
	public Rabbit(Point p, Node worldLink){
		super(RABBIT, stand, p, worldLink);
		maxSpeed = 5;
		health = 10;
	}
	
	public void tick(int dTime){
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
