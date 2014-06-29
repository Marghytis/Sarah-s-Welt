package world.worldObjects;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Node;
import core.geom.Vec;

public class Cactus extends WorldObject{
	
	public int variant;
	
	public Cactus(int variant, Vec pos, Node worldLink){
		super(new Animator(Res.CACTUS, new Animation(0, variant)), pos, worldLink, false, ObjectType.CACTUS);
		this.variant = variant;
	}
	
	public static WorldObject createNewObject(float x, float y, Node worldLink, boolean front, String metaString){

		int variant = Integer.parseInt(metaString);
		
		return new Cactus(variant, new Vec(x, y), worldLink);
	}

	@Override
	public String createMetaString() {
		return variant + "";
	}

}
