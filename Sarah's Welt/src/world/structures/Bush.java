package world.structures;

import world.Node;
import world.Point;
import world.Structure;

public class Bush extends Structure{

	public Bush(int type, Point pos, Node worldLink, boolean front){
		super(Structure.BUSH[type], pos, worldLink);
		this.front = front;
	}
	
}
