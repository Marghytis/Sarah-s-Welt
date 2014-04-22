package world.structures;

import util.Animation;
import world.Node;
import world.Point;

public class Bush extends Structure{

	public Bush(int type, Point pos, Node worldLink, boolean front){
		super(Structure.BUSH[type], new Animation(1, 1), pos, worldLink);
		this.front = front;
	}
	
}
