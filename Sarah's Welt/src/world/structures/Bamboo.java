package world.structures;

import java.util.ArrayList;
import java.util.List;

import resources.StackedTexture;
import util.Animation;
import world.Node;
import world.Point;

public class Bamboo extends Structure{
	
	public static List<Bamboo> l_i_s_t = new ArrayList<>();
	
	public static void updateAll(int dTime){
		l_i_s_t.forEach((b) -> b.tick(dTime));
	}
	
	public static void renderAll(){
		BAMBOO.bind();
			l_i_s_t.forEach((b) -> b.render());
		BAMBOO.release();
	}

	public static StackedTexture BAMBOO = new StackedTexture("structures/Bamboo", 1, 4, -0.5f, -0.02f);
	
	public Bamboo(int type, Point pos, Node worldLink, boolean front){
		super(BAMBOO, new Animation(1, type), pos, worldLink);
		this.front = front;
	}
	
}
