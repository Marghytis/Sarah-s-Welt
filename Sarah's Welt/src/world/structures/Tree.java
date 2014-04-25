package world.structures;

import resources.StackedTexture;
import util.Animation;
import world.Node;
import world.Point;

public class Tree extends Structure{

	public static StackedTexture[] TREE = {		new StackedTexture("structures/Tree1", 1, 1, -0.5f, -0.3f),
												new StackedTexture("structures/Tree2", 1, 1, -0.5f, -0.3f),
												new StackedTexture("structures/Tree3", 1, 1, -0.5f, -0.3f)};
	
	public Tree(int type, Point pos, Node worldLink){
		super(TREE[type], new Animation(1, 1), pos, worldLink);
	}
	
}
