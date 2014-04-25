package world.structures;

import resources.StackedTexture;
import util.Animation;
import world.Node;
import world.Point;
import world.Thing;

public abstract class Structure extends Thing{

	public Structure(StackedTexture tex, Animation defaultAni, Point pos, Node worldLink){
		super(tex, defaultAni, pos, worldLink);
	}
}
