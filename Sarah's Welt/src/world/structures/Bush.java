package world.structures;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Node;
import core.geom.Vec;

public class Bush extends Structure{

	public static int typeId;
	
	public Bush(int type, Vec pos, Node worldLink){
		super(new Animator(Res.BUSH, new Animation(1, type)), pos, worldLink);
		this.front = random.nextInt(10) == 0;
	}
	
}
