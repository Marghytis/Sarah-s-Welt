package world.structures;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Node;
import core.geom.Vec;

public class Cactus extends Structure{

	public static int typeId;
	
	public Cactus(int type, Vec pos, Node worldLink){
		super(new Animator(Res.CACTUS, new Animation(1, type)), pos, worldLink);
		this.front = random.nextInt(10) == 0;
	}

}
