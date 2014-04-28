package world.structures;

import java.util.ArrayList;
import java.util.List;

import resources.StackedTexture;
import util.Animation;
import world.Node;
import world.Point;

public class Tree extends Structure{
	
	public static List<Tree> l_i_s_t = new ArrayList<>();
	
	public static void updateAll(int dTime){
		l_i_s_t.forEach((b) -> b.tick(dTime));
	}
	
	public static void renderAll(){
		TREE.bind();
			l_i_s_t.forEach((b) -> b.render());
		TREE.release();
	}

	public static StackedTexture TREE = new StackedTexture("structures/Tree", 3, 1, -0.5f, -0.3f);
	
	public Tree(int type, Point pos, Node worldLink){
		super(TREE, new Animation(type, 1), pos, worldLink);
	}
	
}
