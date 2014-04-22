package world.structures;

import util.Animation;
import world.Node;
import world.Point;

public class Grass_tuft extends Structure{
	
	public static Animation wave = new Animation(14, 0, 0, 1, 2, 3, 2, 1);
	
	public Grass_tuft(Point pos, Node worldLink, int wavingPos){
		super(Structure.GRASS_TUFT, wave, pos, worldLink);
		animator.frame = wavingPos*animator.animation.speed;
	}
	
}
