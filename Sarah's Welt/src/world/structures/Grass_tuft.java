package world.structures;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Node;
import world.Point;

public class Grass_tuft extends Structure{

	public static int typeId;
	
	public static Animation wave = new Animation(14, 0, true, 0, 1, 2, 3, 2, 1);
	
	public Grass_tuft(Point pos, Node worldLink, int wavingPos){
		super(new Animator(Res.GRASS_TUFT, wave), pos, worldLink);
		animator.frame = wavingPos*animator.animation.speed;
	}
	
}
