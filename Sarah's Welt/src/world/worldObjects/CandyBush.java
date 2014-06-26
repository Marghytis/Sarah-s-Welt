package world.worldObjects;

import resources.Res;
import util.Animation;
import util.Animator;
import world.Node;
import core.geom.Vec;

public class CandyBush extends WorldObject{
	
	public CandyBush(int type, Vec pos, Node worldLink){
		super(new Animator(Res.CANDY_BUSH, new Animation(0, type)), pos, worldLink);
		this.front = random.nextInt(10) == 0;
	}
	
	public void beforeRender(){
		alignWithGround();//worldLink.p.minus(worldLink.getNext().p).angle()
	}
	
}