package world.structures;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Node;
import core.geom.Vec;

public class Tree extends Structure{

	public static int typeId;
	
	public Tree(int type, Vec pos, Node worldLink){
		super(new Animator(Res.TREE, new Animation(0, type)), pos, worldLink);
	}
	
}
