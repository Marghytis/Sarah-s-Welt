package world.structures;

import java.util.ArrayList;
import java.util.List;

import resources.StackedTexture;
import util.Animation;
import world.Node;
import world.Point;

public class Bush extends Structure{
	
	public static List<Cloud> l_i_s_t = new ArrayList<>();
	
	public static void updateAll(int dTime){
		l_i_s_t.forEach((b) -> b.tick(dTime));
	}
	
	public static void renderAll(){
		BUSH.bind();
			l_i_s_t.forEach((b) -> b.render());
		BUSH.release();
	}

	public static StackedTexture BUSH = new StackedTexture("structures/Bush1", 2, 1, -0.5f, -0.2f);
	
	public Bush(int type, Point pos, Node worldLink, boolean front){
		super(BUSH, new Animation(type, 1), pos, worldLink);
		this.front = front;
	}
	
}
