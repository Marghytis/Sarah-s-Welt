package world.otherThings;

import resources.StackedTexture;
import world.Node;
import world.Point;
import world.creatures.WalkingCreature;

public class Heart extends WalkingCreature{
	
	public static StackedTexture tex = new StackedTexture("heart", 4, 1, -0.5f, -0.2f);

	static int[] flap = {0, 1, 2, 3, 2, 1}; int cFlap = 0;

	public Heart(Point pos, Node worldLink) {
		super(tex, pos, worldLink);
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

	protected void howToRender(){
		super.howToRender();
		
		frameX = flap[cFlap/10];
		
		cFlap++;
		if(cFlap/10 >= flap.length){
			cFlap = 0;
		}
	}
}
