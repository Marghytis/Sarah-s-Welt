package world.structures;

import world.Node;
import world.Point;
import world.Structure;

public class Flower extends Structure {
	
	public Flower(int type, Point pos, Node worldLink){
		super(Structure.FLOWER[type], pos, worldLink);
	}
	
}
