package world.structures;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Node;
import core.geom.Vec;

public class Bamboo extends Structure{

	public static int typeId;
	
	public Bamboo(int type, Vec pos, Node worldLink, boolean front){
		super(new Animator(Res.BAMBOO, new Animation(1, type)), pos, worldLink);
		this.front = front;
	}
	
}
