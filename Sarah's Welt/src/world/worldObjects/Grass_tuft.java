package world.worldObjects;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Node;
import core.geom.Vec;

public class Grass_tuft extends WorldObject{
	
	public static Animation wave = new Animation(14, 0, true, 0, 1, 2, 3, 2, 1);
	
	public Grass_tuft(Vec pos, Node worldLink, int wavingPos){
		super(new Animator(Res.GRASS_TUFT, wave), pos, worldLink);
		animator.frame = wavingPos*animator.animation.speed;
	}
	
}
