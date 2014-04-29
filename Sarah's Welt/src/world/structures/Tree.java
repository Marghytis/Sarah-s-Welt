package world.structures;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Node;
import world.Point;

public class Tree extends Structure{

	public static int typeId;
	
	public Tree(int type, Point pos, Node worldLink){
		super(new Animator(Res.TREE, new Animation(type, 1)), pos, worldLink);
	}
	
}
