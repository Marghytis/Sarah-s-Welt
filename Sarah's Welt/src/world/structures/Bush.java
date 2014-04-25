package world.structures;

import resources.StackedTexture;
import util.Animation;
import world.Node;
import world.Point;

public class Bush extends Structure{

	public static StackedTexture[] BUSH = {		new StackedTexture("structures/Bush1", 1, 1, -0.5f, -0.2f),
												new StackedTexture("structures/Bush2", 1, 1, -0.5f, -0.2f)};
	
	public Bush(int type, Point pos, Node worldLink, boolean front){
		super(BUSH[type], new Animation(1, 1), pos, worldLink);
		this.front = front;
	}
	
}
