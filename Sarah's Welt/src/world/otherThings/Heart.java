package world.otherThings;

import java.util.ArrayList;
import java.util.List;

import resources.StackedTexture;
import util.Animation;
import world.Node;
import world.Point;
import world.creatures.WalkingCreature;

public class Heart extends WalkingCreature{
	
	public static List<Heart> l_i_s_t = new ArrayList<>();
	
	public static void updateAll(int dTime){
		l_i_s_t.forEach((b) -> b.tick(dTime));
	}
	
	public static void renderAll(){
		HEART.bind();
			l_i_s_t.forEach((b) -> b.render());
		HEART.release();
	}
	
	public static StackedTexture HEART = new StackedTexture("creatures/Heart", 4, 1, -0.5f, -0.2f);

	static Animation flap = new Animation(10, 0, true, 0, 1, 2, 3, 2, 1);

	public Heart(Point pos, Node worldLink) {
		super(HEART, flap, pos, worldLink);
		vel.y = -0.2f;
		health = 2000;
	}
	
	public void tick(int dTime){
		if(!g){
			collision();
		}
		health--;
		super.tick(dTime);
	}
}
