package world.worldObjects;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Node;
import core.geom.Vec;

public class Grave extends WorldObject{
	
	public int variant;
	
	public Grave(int variant, Vec pos, Node worldLink){
		super(new Animator(Res.GRAVE, new Animation(0, variant)), pos, worldLink, false, ObjectType.GRAVE);
		this.variant = variant;
	}
	
	public static WorldObject createNewObject(float x, float y, Node worldLink, boolean front, String metaString){

		int variant = Integer.parseInt(metaString);
		
		return new Grave(variant, new Vec(x, y), worldLink);
	}

	@Override
	public String createMetaString() {
		return variant + "";
	}

}
