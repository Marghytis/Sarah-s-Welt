package world.structures;

import resources.StackedTexture;
import util.Animation;
import world.Node;
import world.Point;

public class Flower extends Structure {
	
	public int type;
	public static StackedTexture[] FLOWER = {	new StackedTexture("structures/Flower1", 1, 1, -0.5f, 0f),
												new StackedTexture("structures/Flower2", 1, 1, -0.5f, 0f),
												new StackedTexture("structures/Flower3", 1, 1, -0.5f, 0f)};
	
	public Flower(int type, Point pos, Node worldLink){
		super(FLOWER[type], new Animation(1, 1), pos, worldLink);
		this.type = type;
	}
	
}
