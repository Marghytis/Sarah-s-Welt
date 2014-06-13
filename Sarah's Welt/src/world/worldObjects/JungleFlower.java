package world.worldObjects;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Node;
import core.geom.Vec;

public class JungleFlower extends WorldObject {

public static int typeId;
	

	
	static Animation[] anis = new Animation[]{
			new Animation(0, 0),
			new Animation(0, 1),
			new Animation(0, 2),
			new Animation(0, 3),
			new Animation(0, 4),
	};
	
	public JungleFlower(int type, Vec pos, Node worldLink){
		super(new Animator(Res.JUNGLE_FLOWER, anis[type]), pos, worldLink, typeId);

		mirrored = random.nextBoolean();
	}
	
}

