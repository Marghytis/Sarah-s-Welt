package worldObjects;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Node;
import core.geom.Vec;

public class Cactus extends WorldObject{

	public static int typeId;
	
	public Cactus(int type, Vec pos, Node worldLink){
		super(new Animator(Res.CACTUS, new Animation(0, type)), pos, worldLink, typeId);
		this.front = random.nextInt(10) == 0;
	}

}
