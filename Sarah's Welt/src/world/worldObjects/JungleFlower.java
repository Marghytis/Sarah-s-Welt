package world.worldObjects;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Node;
import core.geom.Vec;

public class JungleFlower extends WorldObject {

	public int variant;
	
	public JungleFlower(int variant, Vec pos, Node worldLink){
		super(new Animator(Res.JUNGLE_FLOWER, new Animation()), pos, worldLink, false, ObjectType.JUNGLE_FLOWER);
		this.variant = variant;
		mirrored = random.nextBoolean();
	}
	
	@Override
	public void beforeRender(){
		animator.animation.y = variant;
	}
	
	public static WorldObject createNewObject(float x, float y, Node worldLink, boolean front, String metaString){

		String[] args = metaString.split(";");
		int variant = Integer.parseInt(args[0]);
		
		return new JungleFlower(variant, new Vec(x, y), worldLink);
	}

	@Override
	public String createMetaString() {
		return variant + "";
	}
	
}

