package world.structures;

import resources.StackedTexture;
import util.Animation;
import world.Node;
import world.Point;

public class Bambus extends Structure{

	public static StackedTexture BAMBUS = new StackedTexture("structures/Bambus", 1, 4, -0.5f, -0.2f);
	
	public Bambus(int type, Point pos, Node worldLink, boolean front){
		super(BAMBUS, new Animation(1, type), pos, worldLink);
		this.front = front;
	}
	
}
