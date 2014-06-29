package world.worldObjects;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Node;
import core.geom.Vec;

public class CandyBush extends WorldObject{
	
	public int variant;
	
	public CandyBush(int variant, Vec pos, Node worldLink){
		this(variant, random.nextInt(10) == 0, pos, worldLink);
	}
	
	public CandyBush(int variant, boolean front, Vec pos, Node worldLink){
		super(new Animator(Res.CANDY_BUSH, new Animation(0, variant)), pos, worldLink, front, ObjectType.CANDY_BUSH);
		this.variant = variant;
	}
	
	@Override
	public void beforeRender(){
		alignWithGround();//worldLink.p.minus(worldLink.getNext().p).angle()
	}
	
	public static WorldObject createNewObject(float x, float y, Node worldLink, boolean front, String metaString){

		int variant = Integer.parseInt(metaString);
		
		return new CandyBush(variant, front, new Vec(x, y), worldLink);
	}

	@Override
	public String createMetaString() {
		return variant + "";
	}
	
}