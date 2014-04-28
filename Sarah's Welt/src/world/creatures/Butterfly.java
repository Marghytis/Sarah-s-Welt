package world.creatures;

import java.util.ArrayList;
import java.util.List;

import resources.StackedTexture;
import util.Animation;
import world.Material;
import world.Node;
import world.Point;


public class Butterfly extends WalkingCreature{
	
	public static List<Butterfly> l_i_s_t = new ArrayList<>();
	
	public static void updateAll(int dTime){
		l_i_s_t.forEach((b) -> b.tick(dTime));
	}
	
	public static void renderAll(){
		BUTTERFLY.bind();
		l_i_s_t.forEach((b) -> b.render());
		BUTTERFLY.release();
	}
	

	public static StackedTexture BUTTERFLY  = new StackedTexture("creatures/Butterfly", 5, 2, -0.5f, -0.5f);

	public static Animation flap1 = new Animation(5, 0, true, 0, 1, 2, 3, 2, 1);
	public static Animation flap2 = new Animation(5, 0, true, 0, 1, 2, 3, 2, 1);
	
	public Butterfly(int type, Point p, Node worldLink, int frame){
		super(BUTTERFLY, type == 0 ? flap1 : flap2, p, worldLink);
		front = true;
		health = 5;
		animator.frame = frame;
		l_i_s_t.add(this);
	}
	
	public void tick(int dTime){
		if(!g){
			acc.add((0.5f - random.nextFloat())*0.00003f, (0.5f - random.nextFloat())*0.00003f);
			applyFriction(Material.AIR);
		} else {
			if(random.nextInt(100) == 0){
				pos.y++;
				accelerateFromGround(new Point(0, 0.0001f));
			}
		}
		
		if(!g) collision();
		
		super.tick(dTime);
	}
}
