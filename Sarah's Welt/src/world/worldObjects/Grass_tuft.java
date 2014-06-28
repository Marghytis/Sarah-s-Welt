package world.worldObjects;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Node;
import core.geom.Vec;

public class Grass_tuft extends WorldObject{
	
	public static Animation wave = new Animation(14, 0, true, 0, 1, 2, 3, 2, 1);
	
	public Grass_tuft(Vec pos, Node worldLink){
		super(new Animator(Res.GRASS_TUFT, wave), pos, worldLink, false, ObjectType.GRASS_TUFT);
		animator.frame = random.nextInt(wave.sequence.length)*animator.animation.speed;
	}
	
	public static WorldObject createNewObject(float x, float y, Node worldLink, boolean front, String metaString){

		return new Grass_tuft(new Vec(x, y), worldLink);
	}

	public String createMetaString() {
		return "";
	}
}
