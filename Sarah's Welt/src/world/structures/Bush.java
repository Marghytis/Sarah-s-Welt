package world.structures;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Node;
import world.Point;

public class Bush extends Structure{

	public static int typeId;
	
	public Bush(int type, Point pos, Node worldLink, boolean front){
		super(new Animator(Res.BUSH, new Animation(type, 1)), pos, worldLink);
		this.front = front;
	}
	
}
