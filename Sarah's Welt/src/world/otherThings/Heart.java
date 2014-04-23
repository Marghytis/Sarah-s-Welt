package world.otherThings;

import resources.StackedTexture;
import util.Animation;
import world.Node;
import world.Point;
import world.creatures.WalkingCreature;

public class Heart extends WalkingCreature{
	
	public static StackedTexture tex = new StackedTexture("heart", 4, 1, -0.5f, -0.2f);

	static Animation flap = new Animation(10, 0, true, 0, 1, 2, 3, 2, 1);

	public Heart(Point pos, Node worldLink) {
		super(tex, flap, pos, worldLink);
		vel.y = -0.2f;
		health = 2000;
	}
	
	public void tick(float dTime){
		if(!g){
			collision();
		}
		health--;
		super.tick(dTime);
	}
}
