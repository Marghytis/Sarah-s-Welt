package world.worldObjects;

import util.Animator;
import world.Node;
import world.Thing;
import core.geom.Vec;

public abstract class WorldObject extends Thing{

	public WorldObject(Animator ani, Vec pos, Node worldLink){
		super(ani, pos, worldLink);
	}
}
