package world.structures;

import util.Animator;
import world.Node;
import world.Thing;
import core.geom.Vec;

public abstract class Structure extends Thing{

	public Structure(Animator ani, Vec pos, Node worldLink){
		super(ani, pos, worldLink);
	}
}
