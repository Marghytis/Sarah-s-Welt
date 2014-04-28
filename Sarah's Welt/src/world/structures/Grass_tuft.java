package world.structures;

import java.util.ArrayList;
import java.util.List;

import resources.StackedTexture;
import util.Animation;
import world.Node;
import world.Point;

public class Grass_tuft extends Structure{
	
	public static List<Grass_tuft> l_i_s_t = new ArrayList<>();
	
	public static void updateAll(int dTime){
		l_i_s_t.forEach((b) -> b.tick(dTime));
	}
	
	public static void renderAll(){
		GRASS_TUFT.bind();
			l_i_s_t.forEach((b) -> b.render());
		GRASS_TUFT.release();
	}
	
	public static StackedTexture GRASS_TUFT = new StackedTexture("structures/Grass_tuft", 4, 1, -0.5f, -0.2f);
	public static Animation wave = new Animation(14, 0, true, 0, 1, 2, 3, 2, 1);
	
	public Grass_tuft(Point pos, Node worldLink, int wavingPos){
		super(GRASS_TUFT, wave, pos, worldLink);
		animator.frame = wavingPos*animator.animation.speed;
	}
	
}
