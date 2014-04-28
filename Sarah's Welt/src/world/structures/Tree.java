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
		SNAIL.bind();
			l_i_s_t.forEach((b) -> b.render());
		SNAIL.release();
	}

	public static StackedTexture[] TREE = {		new StackedTexture("structures/Tree1", 1, 1, -0.5f, -0.3f),
												new StackedTexture("structures/Tree2", 1, 1, -0.5f, -0.3f),
												new StackedTexture("structures/Tree3", 1, 1, -0.5f, -0.3f)};
	public static StackedTexture TREE = new StackedTexture("structures/Tree", 1, 1, -0.5f, -0.3f);
	
	public Tree(int type, Point pos, Node worldLink){
		super(TREE[type], new Animation(1, 1), pos, worldLink);
	}
	
}
