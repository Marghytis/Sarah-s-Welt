package world.structures;

import resources.StackedTexture;
import util.Animation;
import world.Node;
import world.Point;

public class Bamboo extends Structure{

	public static StackedTexture BAMBUS = new StackedTexture("structures/Bambus", 1, 4, -0.5f, -0.02f);
	
	public Bamboo(int type, Point pos, Node worldLink, boolean front){
		super(BAMBUS, new Animation(1, type), pos, worldLink);
		this.front = front;
	}
	
}
