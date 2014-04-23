package world.structures;

import util.Animation;
import world.Node;
import world.Point;

public class Flower extends Structure {
	
	int type;
	
	public Flower(int type, Point pos, Node worldLink){
		super(Structure.FLOWER[type], new Animation(1, 1), pos, worldLink);
		this.type = type;
	}
	
}
