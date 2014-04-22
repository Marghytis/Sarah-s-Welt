package world.structures;

import world.Node;
import world.Point;

public class Cloud extends Structure{

	public Cloud(Point pos, Node worldLink, float xSize, float ySize){
		super(Structure.CLOUD, pos, worldLink);
		box.x*=xSize;
		box.y*=ySize;
		box.width*=xSize;
		box.width*=ySize;
	}
	
}
