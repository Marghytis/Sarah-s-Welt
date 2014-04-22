package world.structures;

import world.Node;
import world.Point;

public class Tree extends Structure{

	public Tree(int type, Point pos, Node worldLink){
		super(Structure.TREE[type], pos, worldLink);
	}
	
}
