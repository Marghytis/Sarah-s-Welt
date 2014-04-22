package world.structures;

import util.Animation;
import world.Node;
import world.Point;

public class Tree extends Structure{

	public Tree(int type, Point pos, Node worldLink){
		super(Structure.TREE[type], new Animation(1, 1), pos, worldLink);
	}
	
}
